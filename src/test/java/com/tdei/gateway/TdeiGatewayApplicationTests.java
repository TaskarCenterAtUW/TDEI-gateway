package com.tdei.gateway;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.RequestPostProcessor;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class TdeiGatewayApplicationTests {

    public static RequestPostProcessor authentication() {
        return request -> {
            request.addHeader("Authorization", "sss");
            return request;
        };
    }

    @Test
    void agencyRequestUnauthorized(@Autowired MockMvc mvc) throws Exception {
        mvc.perform(get("/api/v1/agencies")).andExpect(status().isUnauthorized());
    }

    @Test
    void authenticateRequestValid(@Autowired MockMvc mvc) throws Exception {
        mvc.perform(post("/api/v1/authenticate").servletPath("/api/v1/authenticate")
                        .content("{\n" +
                                "  \"username\": \"mahesh\",\n" +
                                "  \"password\": \"password\"\n" +
                                "}")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk());
    }

    @Test
    void authenticateRequestWrongCreds(@Autowired MockMvc mvc) throws Exception {
        mvc.perform(post("/api/v1/authenticate").servletPath("/api/v1/authenticate")
                        .content("{\n" +
                                "  \"username\": \"mahesh\",\n" +
                                "  \"password\": \"password123\"\n" +
                                "}")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isUnauthorized());
    }

    @Test
    void authenticateRequestDataCheck(@Autowired MockMvc mvc) throws Exception {
        mvc.perform(post("/api/v1/authenticate").servletPath("/api/v1/authenticate")
                        .content("{\n" +
                                "  \"username\": \"mahesh\",\n" +
                                "  \"password\": \"password\"\n" +
                                "}")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.access_token").exists());
    }

    @Test
    void getTokenAndUploadGTFS(@Autowired MockMvc mvc) throws Exception {
        MvcResult result = mvc.perform(post("/api/v1/authenticate").servletPath("/api/v1/authenticate")
                        .content("{\n" +
                                "  \"username\": \"mahesh\",\n" +
                                "  \"password\": \"password\"\n" +
                                "}")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn();

        String response = result.getResponse().getContentAsString();
        final JSONObject obj = new JSONObject(response);
        final String token = obj.getString("access_token");
//        response = response.replace("{\"access_token\": \"", "");
//        String token = response.replace("\"}", "");

        mvc.perform(get("/api/v1/agencies").header("Authorization", "Bearer " + token)).andExpect(status().isOk());
    }

    @Test
    void agencyAuthorizedRequestWithAPIKey(@Autowired MockMvc mvc) throws Exception {
        mvc.perform(get("/api/v1/agencies")
                        .header("x-api-key", "x-api-key")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk());
    }

    @Test
    void agencyAuthorizedRequestWithAPIKeyDataCheck(@Autowired MockMvc mvc) throws Exception {
        mvc.perform(get("/api/v1/agencies")
                        .header("x-api-key", "x-api-key")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.list[0].agency_name").value("SDOT"));
    }
}
