package com.skolarli.lmsservice.unittests.controller;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.skolarli.lmsservice.models.db.LmsUser;
import com.skolarli.lmsservice.models.db.Tenant;
import com.skolarli.lmsservice.models.db.VerificationCode;
import com.skolarli.lmsservice.models.dto.core.NewDomainRequest;
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

import java.time.Instant;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class NewDomainControllerTests {

    @Autowired
    MockMvc mockMvc;
    @MockBean
    TenantService tenantService;
    @MockBean
    LmsUserService lmsUserService;
    @MockBean
    VerificationService verificationService;

    @Autowired
    private ObjectMapper mapper;
    private NewDomainRequest newDomainRequest;
    private LmsUser lmsUser;
    private Tenant tenant;

    private VerificationCode code;

    @BeforeEach
    public void setup() {
        newDomainRequest = new NewDomainRequest("mydomainname", "MyAwesomeCompany", "+91",
                "1234561234", "INR", null, "myawesomewebsite.com", "Jaya",
                "Nair", "jaya@skolarli.com", "mymockpassword");
        lmsUser = new LmsUser();
        lmsUser.setId(1);
        lmsUser.setFirstName("Jaya");
        lmsUser.setLastName("Nair");
        lmsUser.setEmail("jaya@skolarli.com");
        lmsUser.setPassword("mymockpassword");
        lmsUser.setIsAdmin(true);
        lmsUser.setIsInstructor(false);
        lmsUser.setIsStudent(false);

        tenant = new Tenant();
        tenant.setId(1);
        tenant.setDomainName("mydomainname");
        tenant.setCompanyName("MyAwesomeCompany");
        tenant.setCountryCode("+91");
        tenant.setPhoneNumber("1234561234");
        tenant.setCurrency("INR");
        tenant.setWebsite("myawesomewebsite.com");

        code = new VerificationCode();
        code.setId(1L);
        code.setUser(lmsUser);
        code.setToken("123456");
        code.setExpiryDate(Instant.now().plusSeconds(VerificationCode.EXPIRY_HOURS * 60 * 60));
    }

    @Test
    void newDomainTestSuccess() throws Exception {
        when(tenantService.saveTenant(any())).thenReturn(tenant);
        when(lmsUserService.saveLmsUser(any())).thenReturn(lmsUser);
        when(verificationService.generateAndSaveVerificationCode(any())).thenReturn(code);
        String requestJson = mapper.writeValueAsString(newDomainRequest);

        mockMvc.perform(
                        MockMvcRequestBuilders.post("/newdomain/add")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestJson)
                                .accept(MediaType.APPLICATION_JSON)
                ).andExpect(status().isCreated())
                .andExpect(jsonPath("userId", is(1)))
                .andExpect(jsonPath("tenantId", is(1)));
        Mockito.verify(lmsUserService).saveLmsUser(any());
        Mockito.verify(tenantService).saveTenant(any());

    }

    @Test
    void updateDomainTestFailureUpdateException() throws Exception {
        when(tenantService.saveTenant(any())).thenThrow(new RuntimeException("Error"));
        String requestJson = mapper.writeValueAsString(newDomainRequest);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/newdomain/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson)
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isInternalServerError());
        Mockito.verify(tenantService).saveTenant(any());
    }

    @Test
    void isUniqueTestSuccess() throws Exception {
        when(tenantService.isUniqueDomainName("mydomainname")).thenReturn(true);

        mockMvc.perform(
                        MockMvcRequestBuilders.get("/newdomain/isunique")
                                .contentType(MediaType.APPLICATION_JSON)
                                .param("domainName", "mydomainname")
                                .accept(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andExpect(jsonPath("result", is("true")));
    }
}
