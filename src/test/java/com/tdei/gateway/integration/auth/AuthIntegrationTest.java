package com.tdei.gateway.integration.auth;

import com.tdei.gateway.core.service.auth.AuthService;
import com.tdei.gateway.main.controller.CommonController;
import com.tdei.gateway.main.service.CommonService;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@SpringBootTest(properties = {
        // "spring.jpa.hibernate.ddl-auto=create-drop",
})
@ActiveProfiles("dev")
@AutoConfigureMockMvc
public class AuthIntegrationTest {

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
        this.mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

//    @Test
//    @DisplayName("Authenticate valid credentials, should return success")
//    void authenticateRequestValid() throws Exception {
//        mockMvc.perform(post("/api/v1/authenticate").servletPath("/api/v1/authenticate")
//                        .content("{\n" +
//                                "  \"username\": \"mahesh\",\n" +
//                                "  \"password\": \"password\"\n" +
//                                "}")
//                        .contentType(MediaType.APPLICATION_JSON)
//                )
//                .andExpect(status().isOk());
//    }

//    @Test
//    @DisplayName("Authenticate invalid credentials, should return unauthorized")
//    void authenticateRequestWrongCreds() throws Exception {
//        mockMvc.perform(post("/api/v1/authenticate").servletPath("/api/v1/authenticate")
//                        .content("{\n" +
//                                "  \"username\": \"mahesh\",\n" +
//                                "  \"password\": \"password123\"\n" +
//                                "}")
//                        .contentType(MediaType.APPLICATION_JSON)
//                )
//                .andExpect(status().isUnauthorized());
//    }

//    @Test
//    @DisplayName("Authenticate valid credentials, should return valid access token")
//    void authenticateRequestDataCheck() throws Exception {
//        mockMvc.perform(post("/api/v1/authenticate").servletPath("/api/v1/authenticate")
//                        .content("{\n" +
//                                "  \"username\": \"mahesh\",\n" +
//                                "  \"password\": \"password\"\n" +
//                                "}")
//                        .contentType(MediaType.APPLICATION_JSON)
//                )
//                .andExpect(status().isOk())
//                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$.access_token").exists());
//    }
}
