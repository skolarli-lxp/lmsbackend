package com.skolarli.lmsservice.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.skolarli.lmsservice.AbstractContainerBaseTest;
import com.skolarli.lmsservice.models.dto.core.NewDomainRequest;
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

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class NewDomainControllerTests extends AbstractContainerBaseTest {

    @Autowired
    MockMvc mockMvc;

    private NewDomainRequest newDomainRequest;

    @Autowired
    private ObjectMapper mapper;

    @BeforeAll
    public static void setUp() {
        container.withReuse(true);
        container.withInitScript("tenantservicetestdata.sql");

        //container.withInitScript("src/main/resources/db.sql");
        container.start();
    }

    @BeforeEach
    public void setup() {
        newDomainRequest = new NewDomainRequest();
        newDomainRequest.setDomainName("mydomainname3");
        newDomainRequest.setCompanyName("MyAwesomeCompany");
        newDomainRequest.setCountryCode("+91");
        newDomainRequest.setPhoneNumber("1234561234");
        newDomainRequest.setCurrency("INR");
        newDomainRequest.setWebsite("myawesomewebsite.com");
        newDomainRequest.setFirstName("Jaya");
        newDomainRequest.setLastName("Nair");
        newDomainRequest.setEmail("jaya@skolarli.com");
        newDomainRequest.setPassword("mymockpassword");
        newDomainRequest.setTimeZone("Asia/Kolkata");
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
