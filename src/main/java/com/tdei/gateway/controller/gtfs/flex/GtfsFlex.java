package com.tdei.gateway.controller.gtfs.flex;

import com.tdei.gateway.controller.gtfs.flex.contract.IGtfsFlex;
import com.tdei.gateway.model.dto.common.VersionList;
import com.tdei.gateway.model.dto.gtfs.flex.GtfsFlexDownload;
import com.tdei.gateway.model.dto.gtfs.flex.GtfsFlexFile;
import com.tdei.gateway.model.dto.gtfs.flex.GtfsFlexUpload;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/gtfs-flex")
@Tag(name = "GTFS Flex", description = "GTFS flex operations")
@Slf4j
public class GtfsFlex implements IGtfsFlex {

    @Override
    public ResponseEntity<GtfsFlexDownload> getFlexFile(String tdeiRecordId) {
        return null;
    }

    @Override
    public ResponseEntity<GtfsFlexDownload> getLatestFlexFile(Integer tdeiAgencyId, Integer confidence, String schemaVersion) {
        return null;
    }

    @Override
    public ResponseEntity<List<GtfsFlexFile>> listFlexFiles(String bbox, Integer confidenceLevel, String version) {
        return null;
    }

    @Override
    public ResponseEntity<Void> updateFlexFile(String tdeiRecordId, GtfsFlexUpload body) {
        return null;
    }

    @Override
    public ResponseEntity<Integer> uploadFlexFile(GtfsFlexUpload body) {
        return null;
    }

    @Override
    public ResponseEntity<VersionList> listFlexVersions() {
        return null;
    }
}
