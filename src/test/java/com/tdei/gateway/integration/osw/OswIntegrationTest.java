package com.tdei.gateway.integration.osw;

import com.tdei.gateway.osw.controller.OswController;
import com.tdei.gateway.osw.service.OswService;
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
public class OswIntegrationTest {

    @InjectMocks
    OswController controller;
    @Mock
    OswService oswService;
    @Autowired
    private MockMvc mockMvc;


    @BeforeEach
    public void setup() {
        //MyService service = new MyService(client, factory);
        controller = new OswController(oswService);
        this.mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }
}
