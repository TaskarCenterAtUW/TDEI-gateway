package com.tdei.gateway;

import com.tdei.gateway.model.dto.common.Polygon;
import com.tdei.gateway.model.dto.gtfs.flex.GtfsFlexUpload;
import com.tdei.gateway.utils.Utils;
import org.json.JSONObject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.RequestPostProcessor;

import java.time.OffsetDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("local")
class TdeiGatewayApplicationTests {

    public static RequestPostProcessor authentication() {
        return request -> {
            request.addHeader("Authorization", "sss");
            return request;
        };
    }

    @Test
    @DisplayName("Unauthorized Agency Call, should return unauthorized")
    void agencyRequestUnauthorized(@Autowired MockMvc mvc) throws Exception {
        mvc.perform(get("/api/v1/agencies")).andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Authenticate valid credentials, should return success")
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
    @DisplayName("Authenticate invalid credentials, should return unauthorized")
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
    @DisplayName("Authenticate valid credentials, should return valid access token")
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
    @DisplayName("Authenticate valid credentials & Upload GTFS file, should return success")
    void uploadGTFSFlex(@Autowired MockMvc mvc) throws Exception {
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

        var body = new GtfsFlexUpload();
        body.setTdeiAgencyId("101");
        body.setCollectionMethod("test");
        body.setCollectedBy("test");
        body.setPolygon(new Polygon());
        body.setValidFrom(OffsetDateTime.now());
        body.setFlexSchemaVersion("1");
        body.setDataSource("test");
        body.setCollectionDate(OffsetDateTime.now());
        mvc.perform(post("/api/v1/gtfs-flex/101").content(Utils.asJsonString(body)).contentType(MediaType.APPLICATION_JSON).header("Authorization", "Bearer " + token)).andExpect(status().isOk());
    }

    @Test
    @DisplayName("Upload GTFS flex file for wrong agency id, should return access denied.")
    void uploadGTFSFlexWrongAgencyId(@Autowired MockMvc mvc) throws Exception {
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

        var body = new GtfsFlexUpload();
        body.setTdeiAgencyId("101");
        body.setCollectionMethod("test");
        body.setCollectedBy("test");
        body.setPolygon(new Polygon());
        body.setValidFrom(OffsetDateTime.now());
        body.setFlexSchemaVersion("1");
        body.setDataSource("test");
        body.setCollectionDate(OffsetDateTime.now());
        mvc.perform(post("/api/v1/gtfs-flex/102").content(Utils.asJsonString(body)).contentType(MediaType.APPLICATION_JSON).header("Authorization", "Bearer " + token)).andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Authorize agency call with valid API Key, should return success")
    void agencyAuthorizedRequestWithAPIKey(@Autowired MockMvc mvc) throws Exception {
        mvc.perform(get("/api/v1/agencies")
                        .header("x-api-key", "x-api-key")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Authorize agency call with valid API Key, should return valid agency details")
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
