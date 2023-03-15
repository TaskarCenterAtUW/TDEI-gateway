package com.tdei.gateway.unit.auth;

import com.tdei.gateway.core.config.exception.handler.exceptions.InvalidCredentialsException;
import com.tdei.gateway.core.model.authclient.LoginModel;
import com.tdei.gateway.core.model.authclient.TokenResponse;
import com.tdei.gateway.core.service.auth.AuthService;
import com.tdei.gateway.main.controller.CommonController;
import com.tdei.gateway.main.model.common.dto.Organization;
import com.tdei.gateway.main.model.common.dto.Pageable;
import com.tdei.gateway.main.model.common.dto.PageableResponse;
import com.tdei.gateway.main.model.common.dto.VersionSpec;
import com.tdei.gateway.main.service.CommonService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletRequest;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
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
    @DisplayName("Authenticate valid credentials, should return success")
    void authenticate() {
        LoginModel model = new LoginModel();
        model.setUsername("username");
        model.setPassword("password");

        when(authService.authenticate(any(LoginModel.class))).thenReturn(new TokenResponse());
        var result = commonController.authenticate(model);

        assertThat(result.getStatusCode().value()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    @DisplayName("Authenticate invalid credentials, should return unauthorized")
    void authenticateRequestWrongCreds() throws Exception {
        LoginModel model = new LoginModel();
        model.setUsername("username");
        model.setPassword("password123");

        when(authService.authenticate(any(LoginModel.class))).thenThrow(InvalidCredentialsException.class);
        Throwable exception = assertThrows(InvalidCredentialsException.class, () -> commonController.authenticate(model));
    }

    @Test
    @DisplayName("Authenticate valid credentials, should return valid access token")
    void authenticateRequestDataCheck() throws Exception {
        LoginModel model = new LoginModel();
        model.setUsername("username");
        model.setPassword("password");

        TokenResponse tokenResponse = new TokenResponse();
        tokenResponse.setToken("testtoken");
        when(authService.authenticate(any(LoginModel.class))).thenReturn(tokenResponse);
        var result = commonController.authenticate(model);
        assertThat(result.getBody().getToken()).isNotBlank();

    }

    @Test
    void listAgencies() {
        Principal mockPrincipal = mock(Principal.class);
        MockHttpServletRequest request = new MockHttpServletRequest();

        List<Organization> response = new ArrayList<>();
        Organization agency = new Organization();
        agency.setOrgName("SDOT");
        response.add(agency);

        when(commonService.listOrganizations(any(Principal.class), any(), anyInt(), anyInt())).thenReturn(response);
        var result = commonController.listOrganizations(mockPrincipal, request, 1, 1);

        assertThat(result.getStatusCode().value()).isEqualTo(HttpStatus.OK.value());
        assertThat(result.getBody().stream().findFirst().get().getOrgName()).isEqualTo("SDOT");
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
