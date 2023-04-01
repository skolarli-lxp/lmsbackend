package com.skolarli.lmsservice.integration.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.skolarli.lmsservice.integration.AbstractContainerBaseTest;
import com.skolarli.lmsservice.models.NewDomainRequest;
import com.skolarli.lmsservice.models.db.LmsUser;
import com.skolarli.lmsservice.models.db.Tenant;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class NewDomainControllerTests extends AbstractContainerBaseTest {

    @Autowired
    MockMvc mockMvc;

    private NewDomainRequest newDomainRequest;
    private LmsUser lmsUser;
    private Tenant tenant;
    @Autowired
    private ObjectMapper mapper;

    @BeforeAll
    public static void setUp(){
        container.withReuse(true);
        //container.withInitScript("src/main/resources/db.sql");
        container.start();
    }

    @BeforeEach
    public void setup() throws Exception {
        newDomainRequest = new NewDomainRequest("mydomainname3", "MyAwesomeCompany", "+91",
                "1234561234", "INR", null, "myawesomewebsite.com", "Jaya",
                "Nair", "jaya@skolarli.com", "mymockpassword");

    }

    @Test
    void newDomainTestSuccess() throws Exception {
        String requestJson = mapper.writeValueAsString(newDomainRequest);
        mockMvc.perform(
                        MockMvcRequestBuilders.post("/newdomain/add")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestJson)
                                .accept(MediaType.APPLICATION_JSON)
                ).andExpect(status().isCreated());

    }
}
