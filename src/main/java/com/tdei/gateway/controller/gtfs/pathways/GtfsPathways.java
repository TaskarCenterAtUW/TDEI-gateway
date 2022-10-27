package com.tdei.gateway.controller.gtfs.pathways;

import com.tdei.gateway.controller.gtfs.pathways.contract.IGtfsPathways;
import com.tdei.gateway.model.dto.common.PageableResponse;
import com.tdei.gateway.model.dto.common.VersionSpec;
import com.tdei.gateway.model.dto.gtfs.pathways.GtfsPathwaysDownload;
import com.tdei.gateway.model.dto.gtfs.pathways.GtfsPathwaysUpload;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.time.OffsetDateTime;

@RestController
@RequestMapping("/api/v1/gtfs-pathways")
@Tag(name = "GTFS-Pathways", description = "GTFS pathways operations")
@Slf4j
public class GtfsPathways implements IGtfsPathways {

    @Override
    public ResponseEntity<Void> getPathwaysFile(Principal principal, String tdeiRecordId) {
        return null;
    }

    @Override
    public ResponseEntity<PageableResponse<GtfsPathwaysDownload>> listPathwaysFiles(Principal principal, String bbox, Integer confidenceLevel, String pathwaysSchemaVersion, OffsetDateTime dateTime, String tdeiAgencyId, String tdeiRecordId, Integer pageNo, Integer pageSize) {
        return null;
    }

    @Override
    public ResponseEntity<PageableResponse<VersionSpec>> listPathwaysVersions(Principal principal) {
        return null;
    }

    @Override
    public ResponseEntity<String> uploadPathwaysFile(Principal principal, String agencyId, GtfsPathwaysUpload body) {
        return ResponseEntity.ok("File uploaded successfully !");
    }
}
