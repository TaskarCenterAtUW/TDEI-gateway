package com.tdei.gateway.gtfspathways.service;

import com.tdei.gateway.core.config.ApplicationProperties;
import com.tdei.gateway.core.model.authclient.UserProfile;
import com.tdei.gateway.gtfspathways.model.dto.GtfsPathwaysDownload;
import com.tdei.gateway.gtfspathways.model.dto.GtfsPathwaysUpload;
import com.tdei.gateway.gtfspathways.service.contract.IGtfsPathwaysService;
import com.tdei.gateway.main.model.common.dto.Station;
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
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.SequenceInputStream;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static com.tdei.gateway.core.utils.Utils.formatDate;
import static org.springframework.http.MediaType.ALL;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@Service
@RequiredArgsConstructor
@Slf4j
public class GtfsPathwaysService implements IGtfsPathwaysService {
    private final ApplicationProperties applicationProperties;


    @Override
    public String uploadPathwaysFile(Principal principal, GtfsPathwaysUpload body, MultipartFile file) throws FileUploadException {
        UserProfile user = (UserProfile) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();

        try {
            MultipartBodyBuilder builder = new MultipartBodyBuilder();
            builder.part("file", new ByteArrayResource(file.getBytes())).filename(file.getOriginalFilename());
            builder.part("meta", body);
            builder.part("tdeiOrgId", body.getTdeiOrgId());
            builder.part("userId", user.getId());

            WebClient webClient = WebClient.builder().baseUrl(applicationProperties.getGtfsPathways().getUploadUrl()).build();

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
    public Tuple2<InputStream, HttpHeaders> getPathwaysFile(Principal principal, String tdeiRecordId) throws FileNotFoundException {
        try {

            WebClient webClient = WebClient.builder().baseUrl(applicationProperties.getGtfsPathways().getDataUrl() + "/" + tdeiRecordId).build();

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

    @Override
    public List<GtfsPathwaysDownload> listPathwaysFiles(Principal principal,
                                                        String servletPath,
                                                        Optional<Double[]> bbox,
                                                        Optional<String> tdeiStationId,
                                                        Optional<Integer> confidenceLevel,
                                                        Optional<String> pathwaysSchemaVersion,
                                                        Optional<Date> dateTime,
                                                        Optional<String> tdeiOrgId,
                                                        Optional<String> tdeiRecordId,
                                                        Integer pageNo,
                                                        Integer pageSize) throws FileNotFoundException {
        try {

            WebClient webClient = WebClient.builder()
                    .build();

            UriComponentsBuilder uri = UriComponentsBuilder.fromUriString(applicationProperties.getGtfsPathways().getDataUrl());
            uri.queryParam("page_no", pageNo);
            uri.queryParam("page_size", pageSize);
            if (tdeiStationId.isPresent())
                uri.queryParam("tdei_station_id", tdeiStationId.get());
            if (confidenceLevel.isPresent())
                uri.queryParam("confidence_level", confidenceLevel.get());
            if (pathwaysSchemaVersion.isPresent())
                uri.queryParam("pathways_schema_version", pathwaysSchemaVersion.get());
            if (dateTime.isPresent())
                uri.queryParam("date_time", formatDate(dateTime.get(), Optional.empty()));
            if (tdeiOrgId.isPresent())
                uri.queryParam("tdei_org_id", tdeiOrgId.get());
            if (tdeiRecordId.isPresent())
                uri.queryParam("tdei_record_id", tdeiRecordId.get());
            if (bbox.isPresent()) {
                for (double point : bbox.get()) {
                    uri.queryParam("bbox", point);
                }
            }
            Mono<ResponseEntity<List<GtfsPathwaysDownload>>> entity = webClient.get()
                    .uri(uriBuilder -> uri
                            .build().toUri())
                    .accept(APPLICATION_JSON)
                    .retrieve()
                    .toEntityList(GtfsPathwaysDownload.class);

            var response = entity.single().block().getBody();

            response.stream().forEach(x -> {
                x.setDownloadUrl(servletPath + "/" + x.getTdeiRecordId());
            });
            return response;
        } catch (Exception ex) {
            log.error("Error while listing pathways file ", ex);
            throw new InternalError("Error while listing pathways file");
        }
    }

    @Override
    public List<VersionSpec> listPathwaysVersions(Principal principal) {
        return null;
    }

    @Override
    public List<Station> listStations(Principal principal) {
        return new ArrayList<>();
    }
}
