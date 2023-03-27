package com.tdei.gateway.gtfspathways.controller;

import com.tdei.gateway.core.config.exception.handler.exceptions.MetadataValidationException;
import com.tdei.gateway.gtfspathways.controller.contract.IGtfsPathways;
import com.tdei.gateway.gtfspathways.model.Station;
import com.tdei.gateway.gtfspathways.model.dto.GtfsPathwaysDownload;
import com.tdei.gateway.gtfspathways.model.dto.GtfsPathwaysUpload;
import com.tdei.gateway.gtfspathways.service.GtfsPathwaysService;
import com.tdei.gateway.main.model.common.dto.VersionSpec;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.Principal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/gtfs-pathways")
@Tag(name = "GTFS-Pathways", description = "GTFS pathways operations")
@Slf4j
public class GtfsPathwaysController implements IGtfsPathways {
    private final GtfsPathwaysService gtfsPathwaysService;

    public ResponseEntity<?> getPathwaysFile(Principal principal, String tdeiRecordId, HttpServletResponse response) throws IOException {
        var clientResponse = gtfsPathwaysService.getPathwaysFile(principal, tdeiRecordId);
        response.setHeader("Content-Type", clientResponse.getT2().get("Content-Type").get(0));
        response.setHeader("Content-disposition", clientResponse.getT2().get("Content-disposition").get(0));

        InputStreamResource resource = new InputStreamResource(clientResponse.getT1());
        return ResponseEntity.ok().body(resource);
    }

    @Override
    public ResponseEntity<List<GtfsPathwaysDownload>> listPathwaysFiles(Principal principal, HttpServletRequest req, Optional<Double[]> bbox, Optional<String> tdeiStationId,
//                                                                        Optional<Integer> confidenceLevel,
                                                                        Optional<String> pathwaysSchemaVersion, Optional<Date> dateTime, Optional<String> tdeiOrgId, Optional<String> tdeiRecordId, Integer pageNo, Integer pageSize) throws FileNotFoundException {

        return ResponseEntity.ok(gtfsPathwaysService.listPathwaysFiles(principal, req.getServletPath(), bbox, tdeiStationId, Optional.empty(), pathwaysSchemaVersion, dateTime, tdeiOrgId, tdeiRecordId, pageNo, pageSize));
    }

    @Override
    public ResponseEntity<List<VersionSpec>> listPathwaysVersions(Principal principal) {

        return ResponseEntity.ok(gtfsPathwaysService.listPathwaysVersions(principal));
    }

    @Override
    public ResponseEntity<String> uploadPathwaysFile(Principal principal, GtfsPathwaysUpload meta, MultipartFile file, HttpServletRequest httpServletRequest) throws FileUploadException, MetadataValidationException {
        return ResponseEntity.accepted().body(gtfsPathwaysService.uploadPathwaysFile(principal, meta, file));
    }

    @Override
    public ResponseEntity<List<Station>> listStations(Principal principal, HttpServletRequest httpServletRequest, Optional<String> ownerOrg, Integer pageNo, Integer pageSize) {
        List<Station> response = gtfsPathwaysService.listStations(principal, httpServletRequest, ownerOrg, pageNo, pageSize);
        return ResponseEntity.ok(response);
    }
}
