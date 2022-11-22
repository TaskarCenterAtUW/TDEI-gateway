package com.tdei.gateway.osw.service;

import com.tdei.gateway.core.config.ApplicationProperties;
import com.tdei.gateway.core.model.authclient.UserProfile;
import com.tdei.gateway.main.model.common.dto.PageableResponse;
import com.tdei.gateway.main.model.common.dto.VersionSpec;
import com.tdei.gateway.osw.model.dto.OswDownload;
import com.tdei.gateway.osw.model.dto.OswUpload;
import com.tdei.gateway.osw.service.contract.IOswService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.security.Principal;

@Service
@RequiredArgsConstructor
@Slf4j
public class OswService implements IOswService {
    private final ApplicationProperties applicationProperties;

    @Override
    public String uploadOswFile(Principal principal, String tdeiOrgId, OswUpload body, MultipartFile file) throws FileUploadException {
        UserProfile user = (UserProfile) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();
        try {
            MultipartBodyBuilder builder = new MultipartBodyBuilder();
            builder.part("file", new ByteArrayResource(file.getBytes())).filename(file.getOriginalFilename());
            builder.part("meta", body);
            builder.part("tdeiOrgId", tdeiOrgId);
            builder.part("userId", user.getId());

            WebClient webClient = WebClient.builder().baseUrl(applicationProperties.getOsw().getUploadUrl()).build();

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
    public String getOswFile(Principal principal, String tdeiRecordId) {
        return null;
    }

    @Override
    public PageableResponse<OswDownload> listOswFiles(Principal principal, String bbox, Integer confidenceLevel, String flexSchemaVersion, String tdeiRecordId, Integer pageNo, Integer pageSize) {
        return null;
    }

    @Override
    public PageableResponse<VersionSpec> listOswVersions(Principal principal) {
        return null;
    }
}
