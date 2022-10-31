package com.tdei.gateway.gtfsflex.controller;

import com.tdei.gateway.gtfsflex.controller.contract.IGtfsFlex;
import com.tdei.gateway.gtfsflex.model.dto.GtfsFlexDownload;
import com.tdei.gateway.gtfsflex.model.dto.GtfsFlexUpload;
import com.tdei.gateway.gtfsflex.service.GtfsFlexService;
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
@RequestMapping("/api/v1/gtfs-flex")
@Tag(name = "GTFS-Flex", description = "GTFS flex operations")
@Slf4j
public class GtfsFlexController implements IGtfsFlex {
    private final GtfsFlexService gtfsFlexService;

    @Override
    public ResponseEntity<String> getFlexFile(Principal principal, String tdeiRecordId) {
        return ResponseEntity.ok(gtfsFlexService.getFlexFile(principal, tdeiRecordId));
    }

    @Override
    public ResponseEntity<PageableResponse<GtfsFlexDownload>> listFlexFiles(Principal principal, String bbox, Integer confidenceLevel, String flexSchemaVersion, String tdeiAgencyId, OffsetDateTime dateTime, String tdeiRecordId, Integer pageNo, Integer pageSize) {
        return ResponseEntity.ok(gtfsFlexService.listFlexFiles(principal, bbox, confidenceLevel, flexSchemaVersion, tdeiAgencyId, dateTime, tdeiRecordId, pageNo, pageSize));
    }

    @Override
    public ResponseEntity<PageableResponse<VersionSpec>> listFlexVersions(Principal principal) {
        return ResponseEntity.ok(gtfsFlexService.listFlexVersions(principal));
    }

    @Override
    public ResponseEntity<String> uploadFlexFile(Principal principal, String agencyId, GtfsFlexUpload body) {
        return ResponseEntity.ok(gtfsFlexService.uploadFlexFile(principal, agencyId, body));
    }
}