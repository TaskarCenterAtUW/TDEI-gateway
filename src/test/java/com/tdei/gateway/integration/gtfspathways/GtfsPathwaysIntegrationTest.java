package com.tdei.gateway.integration.gtfspathways;

import com.tdei.gateway.gtfspathways.controller.GtfsPathwaysController;
import com.tdei.gateway.gtfspathways.service.GtfsPathwaysService;
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
public class GtfsPathwaysIntegrationTest {

    @InjectMocks
    GtfsPathwaysController controller;
    @Mock
    GtfsPathwaysService gtfsPathwaysService;
    @Autowired
    private MockMvc mockMvc;


    @BeforeEach
    public void setup() {
        //MyService service = new MyService(client, factory);
        controller = new GtfsPathwaysController(gtfsPathwaysService);
        this.mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }
}
