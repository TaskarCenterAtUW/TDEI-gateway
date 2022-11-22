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
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.FileNotFoundException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class GtfsPathwaysService implements IGtfsPathwaysService {
    private final ApplicationProperties applicationProperties;


    @Override
    public String uploadPathwaysFile(Principal principal, String tdeiOrgId, GtfsPathwaysUpload body, MultipartFile file) throws FileUploadException {
        UserProfile user = (UserProfile) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();

        try {
            MultipartBodyBuilder builder = new MultipartBodyBuilder();
            builder.part("file", new ByteArrayResource(file.getBytes())).filename(file.getOriginalFilename());
            builder.part("meta", body);
            builder.part("tdeiOrgId", tdeiOrgId);
            builder.part("userId", user.getId());

            WebClient webClient = WebClient.builder().baseUrl(applicationProperties.getGtfsPathways().getUploadUrl()).build();

            Flux<String> flux = webClient.post()
                    .contentType(MediaType.MULTIPART_FORM_DATA)
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.MULTIPART_FORM_DATA.toString())
                    .accept(MediaType.APPLICATION_JSON)
                    .body(BodyInserters.fromMultipartData(builder.build()))
                    .retrieve()
                    .bodyToFlux(String.class);
            String filePath = flux.single().block();
            log.info(filePath);
            return filePath;
        } catch (Exception ex) {
            log.error("Error while uploading file ", ex);
            throw new FileUploadException("Error while uploading file");
        }
    }

    @Override
    public ResponseEntity<Flux<DataBuffer>> getPathwaysFile(Principal principal, String tdeiRecordId) throws FileNotFoundException {

        try {

            WebClient webClient = WebClient.builder().baseUrl(applicationProperties.getGtfsPathways().getBaseUrl() + "/" + tdeiRecordId).build();

            Mono<ResponseEntity<Flux<DataBuffer>>> flux = webClient.get()
                    .accept(MediaType.APPLICATION_OCTET_STREAM)
                    .retrieve()
                    .toEntityFlux(DataBuffer.class);

            return flux.single().block();
        } catch (Exception ex) {
            log.error("Error while uploading file ", ex);
            throw new FileNotFoundException("Error while uploading file");
        }
    }

    @Override
    public List<GtfsPathwaysDownload> listPathwaysFiles(Principal principal,
                                                        String servletPath,
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

            UriComponentsBuilder uri = UriComponentsBuilder.fromUriString(applicationProperties.getGtfsPathways().getBaseUrl());
            uri.queryParam("page_no", pageNo);
            uri.queryParam("page_size", pageSize);
            if (tdeiStationId.isPresent())
                uri.queryParam("tdei_station_id", tdeiStationId.get());
            if (confidenceLevel.isPresent())
                uri.queryParam("confidence_level", confidenceLevel.get());
            if (pathwaysSchemaVersion.isPresent())
                uri.queryParam("pathways_schema_version", pathwaysSchemaVersion.get());
            if (dateTime.isPresent())
                uri.queryParam("date_time", dateTime.get());
            if (tdeiOrgId.isPresent())
                uri.queryParam("tdei_org_id", tdeiOrgId.get());
            if (tdeiRecordId.isPresent())
                uri.queryParam("tdei_record_id", tdeiRecordId.get());

            Mono<ResponseEntity<List<GtfsPathwaysDownload>>> entity = webClient.get()
                    .uri(uriBuilder -> uri
                            .build().toUri())
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .toEntityList(GtfsPathwaysDownload.class);

            var response = entity.single().block().getBody();

            response.stream().forEach(x -> {
                x.setDownloadUrl(servletPath + "/" + x.getTdeiRecordId());
            });
            return response;
        } catch (Exception ex) {
            log.error("Error while listing pathways file ", ex);
            throw new FileNotFoundException("Error while listing pathways file");
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
