package com.tdei.gateway.integration.gtfsflex;

import com.tdei.gateway.gtfsflex.controller.GtfsFlexController;
import com.tdei.gateway.gtfsflex.service.GtfsFlexService;
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
public class GtfsFlexIntegrationTest {

    @InjectMocks
    GtfsFlexController controller;
    @Mock
    GtfsFlexService gtfsFlexService;
    @Autowired
    private MockMvc mockMvc;


    @BeforeEach
    public void setup() {
        //MyService service = new MyService(client, factory);
        controller = new GtfsFlexController(gtfsFlexService);
        this.mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }


//    @Test
//    @DisplayName("Authenticate valid credentials & Upload GTFS file, should return success")
//    void uploadGTFSFlex() throws Exception {
//        MvcResult result = mockMvc.perform(post("/api/v1/authenticate").servletPath("/api/v1/authenticate")
//                        .content("{\n" +
//                                "  \"username\": \"mahesh\",\n" +
//                                "  \"password\": \"password\"\n" +
//                                "}")
//                        .contentType(MediaType.APPLICATION_JSON)
//                )
//                .andExpect(status().isOk())
//                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
//                .andReturn();
//
//        String response = result.getResponse().getContentAsString();
//        final JSONObject obj = new JSONObject(response);
//        final String token = obj.getString("access_token");
//
//        var body = new GtfsFlexUpload();
//        body.setTdeiAgencyId("101");
//        body.setCollectionMethod("test");
//        body.setCollectedBy("test");
//        body.setPolygon(new Polygon());
//        body.setValidFrom(OffsetDateTime.now());
//        body.setFlexSchemaVersion("1");
//        body.setDataSource("test");
//        body.setCollectionDate(OffsetDateTime.now());
//        mockMvc.perform(post("/api/v1/gtfs-flex/101").content(Utils.asJsonString(body)).contentType(MediaType.APPLICATION_JSON).header("Authorization", "Bearer " + token)).andExpect(status().isOk());
//    }
//
//    @Test
//    @DisplayName("Upload GTFS flex file for wrong agency id, should return access denied.")
//    void uploadGTFSFlexWrongAgencyId() throws Exception {
//        MvcResult result = mockMvc.perform(post("/api/v1/authenticate").servletPath("/api/v1/authenticate")
//                        .content("{\n" +
//                                "  \"username\": \"mahesh\",\n" +
//                                "  \"password\": \"password\"\n" +
//                                "}")
//                        .contentType(MediaType.APPLICATION_JSON)
//                )
//                .andExpect(status().isOk())
//                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
//                .andReturn();
//
//        String response = result.getResponse().getContentAsString();
//        final JSONObject obj = new JSONObject(response);
//        final String token = obj.getString("access_token");
//
//        var body = new GtfsFlexUpload();
//        body.setTdeiAgencyId("101");
//        body.setCollectionMethod("test");
//        body.setCollectedBy("test");
//        body.setPolygon(new Polygon());
//        body.setValidFrom(OffsetDateTime.now());
//        body.setFlexSchemaVersion("1");
//        body.setDataSource("test");
//        body.setCollectionDate(OffsetDateTime.now());
//        mockMvc.perform(post("/api/v1/gtfs-flex/102").content(Utils.asJsonString(body)).contentType(MediaType.APPLICATION_JSON).header("Authorization", "Bearer " + token)).andExpect(status().isUnauthorized());
//    }
}
