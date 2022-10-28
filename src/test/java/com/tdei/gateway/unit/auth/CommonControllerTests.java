package com.tdei.gateway.unit.auth;

import com.tdei.gateway.core.model.authclient.LoginModel;
import com.tdei.gateway.core.model.authclient.TokenResponse;
import com.tdei.gateway.core.service.auth.AuthService;
import com.tdei.gateway.main.controller.CommonController;
import com.tdei.gateway.main.model.common.dto.*;
import com.tdei.gateway.main.service.CommonService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.security.Principal;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CommonControllerTests {

    @Mock
    private AuthService authService;
    @Mock
    private CommonService commonService;

    @InjectMocks
    private CommonController commonController;

    @Test
    void authenticate() {
        LoginModel model = new LoginModel();
        model.setUsername("username");
        model.setPassword("password");

        when(authService.authenticate(any(LoginModel.class))).thenReturn(new TokenResponse());
        var result = commonController.authenticate(model);

        assertThat(result.getStatusCode().value()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    void listAgencies() {
        Principal mockPrincipal = mock(Principal.class);

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

        when(commonService.listAgencies(any(Principal.class))).thenReturn(response);
        var result = commonController.listAgencies(mockPrincipal);

        assertThat(result.getStatusCode().value()).isEqualTo(HttpStatus.OK.value());
        assertThat(result.getBody().getList().stream().findFirst().get().getAgencyName()).isEqualTo("SDOT");
    }

    @Test
    void listStations() {
        Principal mockPrincipal = mock(Principal.class);

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

        when(commonService.listStations(mockPrincipal)).thenReturn(response);
        var result = commonController.listStations(mockPrincipal);

        assertThat(result.getStatusCode().value()).isEqualTo(HttpStatus.OK.value());
        assertThat(result.getBody().getList().stream().findFirst().get().getStationName()).isEqualTo("TACOMA");
    }

    @Test
    void listApiVersions() {
        Principal mockPrincipal = mock(Principal.class);

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

        when(commonService.listApiVersions(mockPrincipal)).thenReturn(response);
        var result = commonController.listApiVersions(mockPrincipal);

        assertThat(result.getStatusCode().value()).isEqualTo(HttpStatus.OK.value());
        assertThat(result.getBody().getList().stream().findFirst().get().getVersion()).isEqualTo("v1");
    }
}
