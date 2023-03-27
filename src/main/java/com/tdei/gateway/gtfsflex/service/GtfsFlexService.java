package com.tdei.gateway.gtfsflex.service;

import com.tdei.gateway.core.config.ApplicationProperties;
import com.tdei.gateway.core.config.exception.handler.exceptions.ApplicationException;
import com.tdei.gateway.core.config.exception.handler.exceptions.ResourceNotFoundException;
import com.tdei.gateway.core.model.authclient.UserProfile;
import com.tdei.gateway.gtfsflex.model.GtfsFlexServiceModel;
import com.tdei.gateway.gtfsflex.model.dto.GtfsFlexDownload;
import com.tdei.gateway.gtfsflex.model.dto.GtfsFlexUpload;
import com.tdei.gateway.gtfsflex.service.contract.IGtfsFlexService;
import com.tdei.gateway.main.model.common.dto.VersionSpec;
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

import javax.servlet.http.HttpServletRequest;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.SequenceInputStream;
import java.security.Principal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.springframework.http.MediaType.ALL;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@Service
@RequiredArgsConstructor
@Slf4j
public class GtfsFlexService implements IGtfsFlexService {
    private final ApplicationProperties applicationProperties;


    @Override
    public String uploadFlexFile(Principal principal, GtfsFlexUpload body, MultipartFile file) throws FileUploadException {
        UserProfile user = (UserProfile) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();
        try {
            MultipartBodyBuilder builder = new MultipartBodyBuilder();
            builder.part("file", new ByteArrayResource(file.getBytes())).filename(file.getOriginalFilename());
            builder.part("meta", body);
            builder.part("tdeiOrgId", body.getTdeiOrgId());
            builder.part("userId", user.getId());

            WebClient webClient = WebClient.builder().baseUrl(applicationProperties.getGtfsFlex().getUploadUrl()).build();

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
        } catch (WebClientResponseException ex) {
            if (ex.getStatusCode().value() == 404) {
                throw new ResourceNotFoundException("File not found, Uploaded file might have been invalidated due to possible validations issues.");
            }
            throw ex;
        } catch (Exception ex) {
            log.error("Error while fetching the flex file", ex);
            throw new ApplicationException("Error while fetching the flex file.");
        }
    }

    @Override
    public Tuple2<InputStream, HttpHeaders> getFlexFile(Principal principal, String tdeiRecordId) throws FileNotFoundException {
        try {

            WebClient webClient = WebClient.builder().baseUrl(applicationProperties.getGtfsFlex().getDataUrl() + "/" + tdeiRecordId).build();

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
        } catch (Exception ex) {
            log.error("File not found", ex);
            throw new FileNotFoundException("File not found, Uploaded file might have been invalidated due to possible validations issues.");
        }
    }

    public List<GtfsFlexDownload> listFlexFiles(Principal principal,
                                                String servletPath,
                                                Optional<String> tdeiServiceId,
                                                Optional<Double[]> bbox,
                                                Optional<Integer> confidenceLevel,
                                                Optional<String> flexSchemaVersion,
                                                Optional<String> dateTime,
                                                Optional<String> tdeiOrgId,
                                                Optional<String> tdeiRecordId,
                                                Integer pageNo,
                                                Integer pageSize) throws FileNotFoundException {
        try {

            WebClient webClient = WebClient.builder()
                    .build();

            UriComponentsBuilder uri = UriComponentsBuilder.fromUriString(applicationProperties.getGtfsFlex().getDataUrl());
            uri.queryParam("page_no", pageNo);
            uri.queryParam("page_size", pageSize);
            if (tdeiServiceId.isPresent())
                uri.queryParam("tdei_service_id", tdeiServiceId.get());
            if (bbox.isPresent()) {
                for (double point : bbox.get()) {
                    uri.queryParam("bbox", point);
                }
            }
            if (confidenceLevel.isPresent())
                uri.queryParam("confidence_level", confidenceLevel.get());
            if (flexSchemaVersion.isPresent())
                uri.queryParam("flex_schema_version", flexSchemaVersion.get());
            if (dateTime.isPresent())
                uri.queryParam("date_time", dateTime.get());
            if (tdeiOrgId.isPresent())
                uri.queryParam("tdei_org_id", tdeiOrgId.get());
            if (tdeiRecordId.isPresent())
                uri.queryParam("tdei_record_id", tdeiRecordId.get());

            Mono<ResponseEntity<List<GtfsFlexDownload>>> entity = webClient.get()
                    .uri(uriBuilder -> uri
                            .build().toUri())
                    .accept(APPLICATION_JSON)
                    .retrieve()
                    .toEntityList(GtfsFlexDownload.class);

            var response = entity.single().block().getBody();

            response.stream().forEach(x -> {
                x.setDownloadUrl(servletPath + "/" + x.getTdeiRecordId());
            });
            return response;
        } catch (Exception ex) {
            log.error("Error while listing flex file ", ex);
            throw new ApplicationException("Error while listing flex file");
        }
    }

    @Override
    public List<VersionSpec> listFlexVersions(Principal principal) {
        return Arrays.asList();
    }

    @Override
    public List<GtfsFlexServiceModel> listFlexServices(Principal principal, HttpServletRequest httpServletRequest, Optional<String> ownerOrg, Integer pageNo, Integer pageSize) {
        try {
            //Get auth info
            String apiKey = httpServletRequest.getHeader("x-api-key");
            String authToken = httpServletRequest.getHeader("Authorization");

            WebClient webClient = WebClient.builder()
                    .build();
            UriComponentsBuilder uri = UriComponentsBuilder.fromUriString(applicationProperties.getManagementSvc().getServiceUrl());
            uri.queryParam("page_no", pageNo);
            uri.queryParam("page_size", pageSize);
            if (ownerOrg.isPresent())
                uri.queryParam("owner_org", ownerOrg.get());

            Mono<ResponseEntity<List<GtfsFlexServiceModel>>> entity = webClient.get()
                    .uri(uriBuilder -> uri
                            .build().toUri())
                    .headers(httpHeaders -> {
                        if (apiKey != null) httpHeaders.set("x-api-key", apiKey);
                        if (authToken != null) httpHeaders.set("Authorization", authToken);
                    })
                    .accept(APPLICATION_JSON)
                    .retrieve()
                    .toEntityList(GtfsFlexServiceModel.class);

            var response = entity.single().block().getBody();

            return response;
        } catch (Exception ex) {
            log.error("Error while listing stations ", ex);
            throw new ApplicationException("Error while listing stations");
        }
    }
}
