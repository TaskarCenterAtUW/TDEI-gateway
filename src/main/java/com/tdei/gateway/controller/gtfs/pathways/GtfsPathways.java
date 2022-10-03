package com.tdei.gateway.controller.gtfs.pathways;

import com.tdei.gateway.controller.gtfs.pathways.contract.IGtfsPathways;
import com.tdei.gateway.model.dto.common.VersionList;
import com.tdei.gateway.model.dto.gtfs.pathways.GtfsPathwaysDownload;
import com.tdei.gateway.model.dto.gtfs.pathways.GtfsPathwaysFile;
import com.tdei.gateway.model.dto.gtfs.pathways.GtfsPathwaysUpload;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/gtfs-pathways")
@Tag(name = "GTFS Pathways", description = "GTFS pathways operations")
@Slf4j
public class GtfsPathways implements IGtfsPathways {

    @Override
    public ResponseEntity<GtfsPathwaysDownload> getLatestPathwaysFile(Integer tdeiStationId, Integer confidence, String schemaVersion) {
        return null;
    }

    @Override
    public ResponseEntity<GtfsPathwaysDownload> getPathwaysFile(String tdeiRecordId) {
        return null;
    }

    @Override
    public ResponseEntity<List<GtfsPathwaysFile>> listPathwaysFiles(String bbox, Integer confidenceLevel, String version) {
        return null;
    }

    @Override
    public ResponseEntity<VersionList> listPathwaysVersions() {
        return null;
    }

    @Override
    public ResponseEntity<Void> updatePathwaysFile(String tdeiRecordId, GtfsPathwaysUpload body) {
        return null;
    }

    @Override
    public ResponseEntity<Integer> uploadPathwaysFile(GtfsPathwaysUpload body) {
        return null;
    }
}
