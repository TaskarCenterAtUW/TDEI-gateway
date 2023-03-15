package com.tdei.gateway.gtfsflex.controller;

import com.tdei.gateway.gtfsflex.controller.contract.IGtfsFlex;
import com.tdei.gateway.gtfsflex.model.GtfsFlexServiceModel;
import com.tdei.gateway.gtfsflex.model.dto.GtfsFlexDownload;
import com.tdei.gateway.gtfsflex.model.dto.GtfsFlexUpload;
import com.tdei.gateway.gtfsflex.service.GtfsFlexService;
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
@RequestMapping("/api/v1/gtfs-flex")
@Tag(name = "GTFS-Flex", description = "GTFS flex operations")
@Slf4j
public class GtfsFlexController implements IGtfsFlex {
    private final GtfsFlexService gtfsFlexService;

    @Override
    public ResponseEntity<?> getFlexFile(Principal principal, String tdeiRecordId, HttpServletResponse response) throws IOException {

        var clientResponse = gtfsFlexService.getFlexFile(principal, tdeiRecordId);
        response.setHeader("Content-Type", clientResponse.getT2().get("Content-Type").get(0));
        response.setHeader("Content-disposition", clientResponse.getT2().get("Content-disposition").get(0));

        InputStreamResource resource = new InputStreamResource(clientResponse.getT1());
        return ResponseEntity.ok().body(resource);
    }

    public ResponseEntity<List<GtfsFlexDownload>> listFlexFiles(Principal principal, HttpServletRequest req, Optional<String> tdeiServiceId, Optional<Double[]> bbox, Optional<String> flexSchemaVersion, Optional<String> tdeiOrgId, Optional<Date> dateTime, Optional<String> tdeiRecordId, Integer pageNo, Integer pageSize) throws FileNotFoundException {
        return ResponseEntity.ok(gtfsFlexService.listFlexFiles(principal, req.getServletPath(), tdeiServiceId, bbox, Optional.of(0), flexSchemaVersion, dateTime, tdeiOrgId, tdeiRecordId, pageNo, pageSize));
    }

    @Override
    public ResponseEntity<List<VersionSpec>> listFlexVersions(Principal principal) {
        return ResponseEntity.ok(gtfsFlexService.listFlexVersions(principal));
    }

    @Override
    public ResponseEntity<String> uploadGtfsFlexFile(Principal principal, GtfsFlexUpload meta, MultipartFile file, HttpServletRequest httpServletRequest) throws FileUploadException {
        return ResponseEntity.accepted().body(gtfsFlexService.uploadFlexFile(principal, meta, file));
    }
    
    @Override
    public ResponseEntity<List<GtfsFlexServiceModel>> listFlexServices(Principal principal, HttpServletRequest httpServletRequest, Optional<String> ownerOrg, Integer pageNo, Integer pageSize) {
        List<GtfsFlexServiceModel> response = gtfsFlexService.listFlexServices(principal, httpServletRequest, ownerOrg, pageNo, pageSize);
        return ResponseEntity.ok(response);
    }
}
