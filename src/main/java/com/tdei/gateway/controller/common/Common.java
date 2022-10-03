package com.tdei.gateway.controller.common;

import com.tdei.gateway.controller.common.contract.ICommon;
import com.tdei.gateway.model.dto.common.Agency;
import com.tdei.gateway.model.dto.common.Station;
import com.tdei.gateway.model.dto.common.VersionList;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@Tag(name = "General", description = "Common operations")
public class Common implements ICommon {

    @Override
    public ResponseEntity<List<Agency>> listAgencies() {
        return null;
    }

    @Override
    public ResponseEntity<VersionList> listApiVersions() {
        return null;
    }

    @Override
    public ResponseEntity<List<Station>> listStations() {
        return null;
    }
}
