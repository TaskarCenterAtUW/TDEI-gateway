package com.tdei.gateway.controller.common;

import com.tdei.gateway.controller.common.contract.ICommon;
import com.tdei.gateway.model.authclient.LoginModel;
import com.tdei.gateway.model.authclient.TokenResponse;
import com.tdei.gateway.model.dto.common.*;
import com.tdei.gateway.service.AuthService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

@RestController
@RequestMapping("/api/v1")
@Tag(name = "General", description = "Common operations")
public class Common implements ICommon {
    @Autowired
    AuthService authService;

    @Override
    public ResponseEntity<TokenResponse> authenticate(LoginModel loginModel) {
        return ResponseEntity.ok(authService.authenticate(loginModel));
    }

    @Override
    public ResponseEntity<PageableResponse<Agency>> listAgencies() {

        PageableResponse response = new PageableResponse();
        Agency agency = new Agency();
        agency.setAgencyName("SDOT");
        response.setList(Arrays.asList(agency));
        Pageable pg = new Pageable();
        pg.setCurrentPage(1);
        pg.setNumPages(1);
        pg.setTotalItems(1);
        pg.setTotalPages(1);
        response.setPageable(pg);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<PageableResponse<VersionSpec>> listApiVersions() {

        PageableResponse response = new PageableResponse();
        VersionSpec vspec = new VersionSpec();
        vspec.setVersion("v1");
        response.setList(Arrays.asList(vspec));
        Pageable pg = new Pageable();
        pg.setCurrentPage(1);
        pg.setNumPages(1);
        pg.setTotalItems(1);
        pg.setTotalPages(1);
        response.setPageable(pg);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<PageableResponse<Station>> listStations() {

        PageableResponse response = new PageableResponse();
        Station station = new Station();
        station.setStationName("TACOMA");
        response.setList(Arrays.asList(station));
        Pageable pg = new Pageable();
        pg.setCurrentPage(1);
        pg.setNumPages(1);
        pg.setTotalItems(1);
        pg.setTotalPages(1);
        response.setPageable(pg);
        return ResponseEntity.ok(response);

    }
}
