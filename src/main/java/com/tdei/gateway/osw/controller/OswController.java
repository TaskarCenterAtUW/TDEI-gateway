package com.tdei.gateway.osw.controller;

import com.tdei.gateway.main.model.common.dto.VersionList;
import com.tdei.gateway.osw.controller.contract.IOsw;
import com.tdei.gateway.osw.model.dto.OswDownload;
import com.tdei.gateway.osw.model.dto.OswUpload;
import com.tdei.gateway.osw.service.OswService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/osw")
@Tag(name = "OSW")
@Slf4j
public class OswController implements IOsw {
    private final OswService oswService;

    @Override
    public ResponseEntity<?> getOswFile(Principal principal, String tdeiRecordId, HttpServletResponse response) throws IOException {
        var clientResponse = oswService.getOswFile(principal, tdeiRecordId);
        response.setHeader("Content-Type", clientResponse.getT2().get("Content-Type").get(0));
        response.setHeader("Content-disposition", clientResponse.getT2().get("Content-disposition").get(0));

        InputStreamResource resource = new InputStreamResource(clientResponse.getT1());
        return ResponseEntity.ok().body(resource);
    }

    @Override
    public ResponseEntity<List<OswDownload>> listOswFiles(Principal principal, HttpServletRequest req, Optional<Double[]> bbox, Optional<String> oswSchemaVersion, Optional<String> tdeiOrgId, Optional<String> dateTime, Optional<String> tdeiRecordId, Integer pageNo, Integer pageSize) throws FileNotFoundException {
        return ResponseEntity.ok(oswService.listOswFiles(principal, req.getServletPath(), Optional.empty(), bbox, oswSchemaVersion, dateTime, tdeiOrgId, tdeiRecordId, pageNo, pageSize));
    }

    @Override
    public ResponseEntity<VersionList> listOswVersions(Principal principal) {

        return ResponseEntity.ok(oswService.listOswVersions(principal));
    }

    @Override
    public ResponseEntity<String> uploadOswFile(Principal principal, OswUpload meta, MultipartFile file, HttpServletRequest httpServletRequest) throws FileUploadException {
        return ResponseEntity.accepted().body(oswService.uploadOswFile(principal, meta, file));
    }
}
