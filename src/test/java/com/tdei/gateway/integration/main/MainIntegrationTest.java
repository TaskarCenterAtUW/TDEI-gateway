package com.tdei.gateway.integration.main;

import com.tdei.gateway.core.middleware.AuthInterceptor;
import com.tdei.gateway.core.service.auth.AuthService;
import com.tdei.gateway.main.controller.CommonController;
import com.tdei.gateway.main.model.common.dto.Organization;
import com.tdei.gateway.main.model.common.dto.Pageable;
import com.tdei.gateway.main.model.common.dto.PageableResponse;
import com.tdei.gateway.main.service.CommonService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.security.Principal;
import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(properties = {
        // "spring.jpa.hibernate.ddl-auto=create-drop",
})
@ActiveProfiles("dev")
@AutoConfigureMockMvc
public class MainIntegrationTest {

    @InjectMocks
    CommonController controller;
    @Autowired
    AuthService authService;
    @Mock
    CommonService commonService;
    @Autowired
    private MockMvc mockMvc;


    @BeforeEach
    public void setup() {
        //MyService service = new MyService(client, factory);
        controller = new CommonController(authService, commonService);
        this.mockMvc = MockMvcBuilders.standaloneSetup(controller).addFilter(new AuthInterceptor(authService)).build();
    }

    @Test
    @DisplayName("Authorize agency call with valid API Key, should return success")
    void agencyAuthorizedRequestWithAPIKey() throws Exception {
        Principal mockPrincipal = Mockito.mock(Principal.class);

        PageableResponse response = new PageableResponse();
        Organization agency = new Organization();
        agency.setOrgName("SDOT");
        response.setList(Arrays.asList(agency));
        Pageable pg = new Pageable();
        pg.setCurrentPage(1);
        pg.setNumPages(1);
        pg.setTotalItems(1);
        pg.setTotalPages(1);
        response.setPageable(pg);

        when(commonService.listOrganizations(any(Principal.class))).thenReturn(response);

        mockMvc.perform(get("/api/v1/agencies")
                        .header("x-api-key", "x-api-key")
                        .contentType(MediaType.APPLICATION_JSON).principal(mockPrincipal)
                )
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Authorize agency call with valid API Key, should return valid agency details")
    void agencyAuthorizedRequestWithAPIKeyDataCheck() throws Exception {
        Principal mockPrincipal = Mockito.mock(Principal.class);

        PageableResponse response = new PageableResponse();
        Organization agency = new Organization();
        agency.setOrgName("SDOT");
        response.setList(Arrays.asList(agency));
        Pageable pg = new Pageable();
        pg.setCurrentPage(1);
        pg.setNumPages(1);
        pg.setTotalItems(1);
        pg.setTotalPages(1);
        response.setPageable(pg);

        when(commonService.listOrganizations(any(Principal.class))).thenReturn(response);

        mockMvc.perform(get("/api/v1/agencies")
                        .header("x-api-key", "x-api-key")
                        .contentType(MediaType.APPLICATION_JSON).principal(mockPrincipal)
                )
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.list[0].agency_name").value("SDOT"));
    }

    @Test
    @DisplayName("Unauthorized Agency Call, should return unauthorized")
    void agencyRequestUnauthorized() throws Exception {
        mockMvc.perform(get("/api/v1/agencies")).andExpect(status().isUnauthorized());
    }

}
