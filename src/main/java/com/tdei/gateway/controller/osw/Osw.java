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

@RestController
@RequestMapping("/api/v1/osw")
@Tag(name = "OSW", description = "OSW operations")
@Slf4j
public class Osw implements IOsw {

    @Override
    public ResponseEntity<Void> getOswFile(String tdeiRecordId) {
        return null;
    }

    @Override
    public ResponseEntity<PageableResponse<OswDownload>> listOswFiles(String boundingBox, Integer confidenceLevel, String oswSchemaVersion, String tdeiRecordId, Integer pageNo, Integer pageSize) {
        return null;
    }

    @Override
    public ResponseEntity<PageableResponse<VersionSpec>> listOswVersions() {
        return null;
    }

    @Override
    public ResponseEntity<String> uploadOswFile(OswUpload body) {
        return null;
    }
}
