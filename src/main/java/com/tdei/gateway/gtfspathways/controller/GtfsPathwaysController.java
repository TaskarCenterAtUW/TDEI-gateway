package com.tdei.gateway.gtfspathways.controller;

import com.tdei.gateway.gtfspathways.controller.contract.IGtfsPathways;
import com.tdei.gateway.gtfspathways.model.dto.GtfsPathwaysDownload;
import com.tdei.gateway.gtfspathways.model.dto.GtfsPathwaysUpload;
import com.tdei.gateway.gtfspathways.service.GtfsPathwaysService;
import com.tdei.gateway.main.model.common.dto.PageableResponse;
import com.tdei.gateway.main.model.common.dto.VersionSpec;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.time.OffsetDateTime;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/gtfs-pathways")
@Tag(name = "GTFS-Pathways", description = "GTFS pathways operations")
@Slf4j
public class GtfsPathwaysController implements IGtfsPathways {
    private final GtfsPathwaysService gtfsPathwaysService;

    @Override
    public ResponseEntity<String> getPathwaysFile(Principal principal, String tdeiRecordId) {

        return ResponseEntity.ok(gtfsPathwaysService.getPathwaysFile(principal, tdeiRecordId));
    }

    @Override
    public ResponseEntity<PageableResponse<GtfsPathwaysDownload>> listPathwaysFiles(Principal principal, String bbox, Integer confidenceLevel, String pathwaysSchemaVersion, OffsetDateTime dateTime, String tdeiAgencyId, String tdeiRecordId, Integer pageNo, Integer pageSize) {
        return ResponseEntity.ok(gtfsPathwaysService.listPathwaysFiles(principal, bbox, confidenceLevel, pathwaysSchemaVersion, tdeiAgencyId, dateTime, tdeiRecordId, pageNo, pageSize));
    }

    @Override
    public ResponseEntity<PageableResponse<VersionSpec>> listPathwaysVersions(Principal principal) {

        return ResponseEntity.ok(gtfsPathwaysService.listPathwaysVersions(principal));
    }

    @Override
    public ResponseEntity<String> uploadPathwaysFile(Principal principal, String agencyId, GtfsPathwaysUpload body) {
        return ResponseEntity.ok(gtfsPathwaysService.uploadPathwaysFile(principal, agencyId, body));
    }
}
