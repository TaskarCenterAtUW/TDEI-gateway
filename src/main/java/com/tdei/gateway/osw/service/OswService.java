package com.tdei.gateway.osw.service;

import com.tdei.gateway.core.config.ApplicationProperties;
import com.tdei.gateway.core.config.exception.handler.exceptions.ApplicationException;
import com.tdei.gateway.core.config.exception.handler.exceptions.ResourceNotFoundException;
import com.tdei.gateway.core.model.authclient.UserProfile;
import com.tdei.gateway.main.model.common.dto.VersionList;
import com.tdei.gateway.main.model.common.dto.VersionSpec;
import com.tdei.gateway.osw.model.dto.OswDownload;
import com.tdei.gateway.osw.model.dto.OswUpload;
import com.tdei.gateway.osw.service.contract.IOswService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.SequenceInputStream;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

import static org.springframework.http.MediaType.ALL;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@Service
@RequiredArgsConstructor
@Slf4j
public class OswService implements IOswService {
    private final ApplicationProperties applicationProperties;

    @Override
    public String uploadOswFile(Principal principal, OswUpload body, MultipartFile file) throws FileUploadException {
        UserProfile user = (UserProfile) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();
        try {
            MultipartBodyBuilder builder = new MultipartBodyBuilder();
            builder.part("file", new ByteArrayResource(file.getBytes())).filename(file.getOriginalFilename());
            builder.part("meta", body);
            builder.part("tdeiOrgId", body.getTdeiOrgId());
            builder.part("userId", user.getId());

            WebClient webClient = WebClient.builder().baseUrl(applicationProperties.getOsw().getUploadUrl()).build();

            Mono<String> flux = webClient.post()
                    .contentType(MediaType.MULTIPART_FORM_DATA)
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.MULTIPART_FORM_DATA.toString())
                    .accept(ALL)
                    .body(BodyInserters.fromMultipartData(builder.build()))
                    .exchangeToMono(response -> {
                        if (response.statusCode().equals(HttpStatus.OK)) {
                            return response.bodyToMono(String.class);
                        } else {
                            // Turn to error
                            return response.createException().flatMap(Mono::error);
                        }
                    });
            String filePath = flux.single().block();
            log.info(filePath);
            return filePath;
        } catch (Exception ex) {
            log.error("Error while uploading file ", ex);
            throw new FileUploadException("Error while uploading file");
        }
    }

    @Override
    public Tuple2<InputStream, HttpHeaders> getOswFile(Principal principal, String tdeiRecordId) {
        try {

            WebClient webClient = WebClient.builder().baseUrl(applicationProperties.getOsw().getDataUrl() + "/" + tdeiRecordId).build();

            var clientResponse = webClient.get()
                    .accept(MediaType.APPLICATION_OCTET_STREAM)
                    .exchangeToMono(response -> {
                        if (response.statusCode().equals(HttpStatus.OK)) {
                            var stream = response.bodyToFlux(DataBuffer.class)
                                    .map(b -> b.asInputStream(true))
                                    .reduce(SequenceInputStream::new).map(x -> Tuples.of(x, response.headers().asHttpHeaders()));
                            return stream;
                        } else {
                            // Turn to error
                            return response.createException().flatMap(Mono::error);
                        }
                    });

            return clientResponse.block();
        } catch (WebClientResponseException ex) {
            if (ex.getStatusCode().value() == 404) {
                throw new ResourceNotFoundException("A file with the specified tdei_record_id was not found. Use the /api/v1/osw endpoints to find valid file ids.");
            }
            throw ex;
        } catch (Exception ex) {
            log.error("Error while fetching the osw file", ex);
            throw new ApplicationException("Error while fetching the osw file.");
        }
    }

    @Override
    public List<OswDownload> listOswFiles(Principal principal,
                                          String servletPath,
                                          Optional<Integer> confidenceLevel,
                                          Optional<Double[]> bbox,
                                          Optional<String> oswSchemaVersion,
                                          Optional<String> dateTime,
                                          Optional<String> tdeiOrgId,
                                          Optional<String> tdeiRecordId,
                                          Integer pageNo,
                                          Integer pageSize) throws FileNotFoundException {
        try {

            WebClient webClient = WebClient.builder()
                    .build();

            UriComponentsBuilder uri = UriComponentsBuilder.fromUriString(applicationProperties.getOsw().getDataUrl());
            uri.queryParam("page_no", pageNo);
            uri.queryParam("page_size", pageSize);
            if (confidenceLevel.isPresent())
                uri.queryParam("confidence_level", confidenceLevel.get());
            if (oswSchemaVersion.isPresent())
                uri.queryParam("osw_schema_version", oswSchemaVersion.get());
            if (dateTime.isPresent())
                uri.queryParam("date_time", dateTime.get());
            if (tdeiOrgId.isPresent())
                uri.queryParam("tdei_org_id", tdeiOrgId.get());
            if (tdeiRecordId.isPresent())
                uri.queryParam("tdei_record_id", tdeiRecordId.get());
            if (bbox.isPresent()) {
                for (double point : bbox.get()) {
                    uri.queryParam("bbox", point);
                }
            }
            Mono<ResponseEntity<List<OswDownload>>> entity = webClient.get()
                    .uri(uriBuilder -> uri
                            .build().toUri())
                    .accept(APPLICATION_JSON)
                    .retrieve()
                    .toEntityList(OswDownload.class);

            var response = entity.single().block().getBody();

            response.stream().forEach(x -> {
                x.setDownloadUrl(servletPath + "/" + x.getTdeiRecordId());
            });
            return response;
        } catch (Exception ex) {
            log.error("Error while listing osw file ", ex);
            throw new ApplicationException("Error while listing osw file");
        }
    }

    @Override
    public VersionList listOswVersions(Principal principal) {
        var result = new VersionList();
        var version = new VersionSpec();
        version.setVersion("v1.0");
        version.setDocumentation("https://tdei-gateway-dev.azurewebsites.net/");
        version.setSpecification("https://github.com/OpenSidewalks/OpenSidewalks-Schema");
        result.setVersions(List.of(version));
        return result;
    }
}
