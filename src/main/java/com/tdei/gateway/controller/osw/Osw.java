package com.tdei.gateway.controller.osw;

import com.tdei.gateway.controller.osw.contract.IOsw;
import com.tdei.gateway.model.dto.common.PageableResponse;
import com.tdei.gateway.model.dto.common.VersionSpec;
import com.tdei.gateway.model.dto.osw.OswDownload;
import com.tdei.gateway.model.dto.osw.OswUpload;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/osw")
@Tag(name = "OSW", description = "OSW operations")
@Slf4j
public class Osw implements IOsw {

    @Override
    public ResponseEntity<Void> getOswFile(Principal principal, String tdeiRecordId) {
        return null;
    }

    @Override
    public ResponseEntity<PageableResponse<OswDownload>> listOswFiles(Principal principal, String boundingBox, Integer confidenceLevel, String oswSchemaVersion, String tdeiRecordId, Integer pageNo, Integer pageSize) {
        return null;
    }

    @Override
    public ResponseEntity<PageableResponse<VersionSpec>> listOswVersions(Principal principal) {
        return null;
    }

    @Override
    public ResponseEntity<String> uploadOswFile(Principal principal, String agencyId, OswUpload body) {

        return ResponseEntity.ok("File uploaded successfully !");
    }
}
