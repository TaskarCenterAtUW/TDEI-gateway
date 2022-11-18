package com.tdei.gateway.gtfspathways.service;

import com.tdei.gateway.core.config.ApplicationProperties;
import com.tdei.gateway.core.model.authclient.UserProfile;
import com.tdei.gateway.gtfspathways.model.dto.GtfsPathwaysDownload;
import com.tdei.gateway.gtfspathways.model.dto.GtfsPathwaysUpload;
import com.tdei.gateway.gtfspathways.service.contract.IGtfsPathwaysService;
import com.tdei.gateway.main.model.common.dto.PageableResponse;
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
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.FileNotFoundException;
import java.security.Principal;
import java.util.Date;

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

            WebClient webClient = WebClient.builder().baseUrl(applicationProperties.getGtfsPathways().getDownloadUrl() + "/" + tdeiRecordId).build();

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
    public PageableResponse<GtfsPathwaysDownload> listPathwaysFiles(Principal principal, String tdeiStationId, Integer confidenceLevel, String flexSchemaVersion, String tdeiOrgId, Date dateTime, String tdeiRecordId, Integer pageNo, Integer pageSize) {
        return null;
    }

    @Override
    public PageableResponse<VersionSpec> listPathwaysVersions(Principal principal) {
        return null;
    }

    @Override
    public PageableResponse<Station> listStations(Principal principal) {
        return new PageableResponse<>();
    }
}
