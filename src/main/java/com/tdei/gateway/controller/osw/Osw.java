package com.tdei.gateway.controller.osw;

import com.tdei.gateway.controller.osw.contract.IOsw;
import com.tdei.gateway.model.dto.common.VersionList;
import com.tdei.gateway.model.dto.gtfs.pathways.GtfsPathwaysUpload;
import com.tdei.gateway.model.dto.osw.OswDownload;
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
    public ResponseEntity<OswDownload> getOswFile(String tdeiRecordId) {
        return null;
    }

    @Override
    public ResponseEntity<OswDownload> listOswFiles(String boundingBox, Integer confidenceLevel, String schemaVersion) {
        return null;
    }

    @Override
    public ResponseEntity<VersionList> listOswVersions() {
        return null;
    }

    @Override
    public ResponseEntity<Void> updateOswFile(String tdeiRecordId, GtfsPathwaysUpload body) {
        return null;
    }
}
