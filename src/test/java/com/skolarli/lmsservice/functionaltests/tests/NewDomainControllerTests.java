package com.skolarli.lmsservice.functionaltests.tests;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.skolarli.lmsservice.models.NewDomainRequest;
import com.skolarli.lmsservice.models.db.LmsUser;
import com.skolarli.lmsservice.models.db.Tenant;
import com.skolarli.lmsservice.models.db.VerificationCode;
import com.skolarli.lmsservice.services.LmsUserService;
import com.skolarli.lmsservice.services.TenantService;
import com.skolarli.lmsservice.services.VerificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Calendar;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
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

    private VerificationCode code;

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
