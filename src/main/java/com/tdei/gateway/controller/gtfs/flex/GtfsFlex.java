package com.tdei.gateway.controller.gtfs.flex;

import com.tdei.gateway.controller.gtfs.flex.contract.IGtfsFlex;
import com.tdei.gateway.model.dto.common.PageableResponse;
import com.tdei.gateway.model.dto.common.VersionSpec;
import com.tdei.gateway.model.dto.gtfs.flex.GtfsFlexDownload;
import com.tdei.gateway.model.dto.gtfs.flex.GtfsFlexUpload;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.OffsetDateTime;

@RestController
@RequestMapping("/api/v1/gtfs-flex")
@Tag(name = "GTFS-Flex", description = "GTFS flex operations")
@Slf4j
public class GtfsFlex implements IGtfsFlex {

    @Override
    public ResponseEntity<Void> getFlexFile(String tdeiRecordId) {
        return null;
    }

    @Override
    public ResponseEntity<PageableResponse<GtfsFlexDownload>> listFlexFiles(String bbox, Integer confidenceLevel, String flexSchemaVersion, String tdeiAgencyId, OffsetDateTime dateTime, String tdeiRecordId, Integer pageNo, Integer pageSize) {
        return null;
    }

    @Override
    public ResponseEntity<PageableResponse<VersionSpec>> listFlexVersions() {
        return null;
    }

    @Override
    public ResponseEntity<String> uploadFlexFile(GtfsFlexUpload body) {
        return null;
    }
}
