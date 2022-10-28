package com.tdei.gateway.osw.controller;

import com.tdei.gateway.main.model.common.dto.PageableResponse;
import com.tdei.gateway.main.model.common.dto.VersionSpec;
import com.tdei.gateway.osw.controller.contract.IOsw;
import com.tdei.gateway.osw.model.dto.OswDownload;
import com.tdei.gateway.osw.model.dto.OswUpload;
import com.tdei.gateway.osw.service.OswService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/osw")
@Tag(name = "OSW", description = "OSW operations")
@Slf4j
public class OswController implements IOsw {
    private final OswService oswService;

    @Override
    public ResponseEntity<String> getOswFile(Principal principal, String tdeiRecordId) {
        return ResponseEntity.ok(oswService.getOswFile(principal, tdeiRecordId));
    }

    @Override
    public ResponseEntity<PageableResponse<OswDownload>> listOswFiles(Principal principal, String boundingBox, Integer confidenceLevel, String oswSchemaVersion, String tdeiRecordId, Integer pageNo, Integer pageSize) {
        return ResponseEntity.ok(oswService.listOswFiles(principal, boundingBox, confidenceLevel, oswSchemaVersion, tdeiRecordId, pageNo, pageSize));
    }

    @Override
    public ResponseEntity<PageableResponse<VersionSpec>> listOswVersions(Principal principal) {

        return ResponseEntity.ok(oswService.listOswVersions(principal));
    }

    @Override
    public ResponseEntity<String> uploadOswFile(Principal principal, String agencyId, OswUpload body) {

        return ResponseEntity.ok(oswService.uploadOswFile(principal, agencyId, body));
    }
}
