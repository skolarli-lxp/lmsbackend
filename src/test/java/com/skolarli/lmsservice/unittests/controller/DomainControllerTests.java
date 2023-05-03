package com.skolarli.lmsservice.unittests.controller;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.skolarli.lmsservice.exception.OperationNotSupportedException;
import com.skolarli.lmsservice.models.db.LmsUser;
import com.skolarli.lmsservice.models.db.Tenant;
import com.skolarli.lmsservice.services.TenantService;
import com.skolarli.lmsservice.utils.UserUtils;
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

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class DomainControllerTests {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    TenantService tenantService;

    @MockBean
    private UserUtils userUtils;

    private LmsUser currentUser1;
    private LmsUser currentUser2;
    private Tenant newTenant;


    @BeforeEach
    public void setup() {
        currentUser1 = new LmsUser();
        currentUser1.setId(1);
        currentUser1.setFirstName("Jaya");
        currentUser1.setLastName("Nair");
        currentUser1.setEmail("jaya@skolarli.com");
        currentUser1.setPassword("mymockpassword");
        currentUser1.setIsAdmin(true);
        currentUser1.setIsInstructor(false);
        currentUser1.setIsStudent(false);

        currentUser2 = new LmsUser();
        currentUser2.setId(2);
        currentUser2.setFirstName("Jaya");
        currentUser2.setLastName("Nair");
        currentUser2.setPassword("mymockpassword");
        currentUser2.setEmail("jaya@skolarli.com");
        currentUser2.setPassword("mymockpassword");
        currentUser2.setIsAdmin(false);
        currentUser2.setIsInstructor(false);
        currentUser2.setIsStudent(false);

        newTenant = new Tenant();
        newTenant.setId(1);
        newTenant.setDomainName("mydomainname");
        newTenant.setCompanyName("MyNewAwesomerCompany");
        newTenant.setCountryCode("+91");
        newTenant.setPhoneNumber("1234561234");
        newTenant.setCurrency("INR");
        newTenant.setWebsite("myawesomewebsite.com");
        newTenant.setAddress("my changed address");

    }

    @Test
    void updateDomainTestSuccess() throws Exception {
        when(userUtils.getCurrentUser()).thenReturn(currentUser1);
        when(tenantService.updateTenant(newTenant)).thenReturn(newTenant);
        String tenantJson = mapper.writeValueAsString(newTenant);

        mockMvc.perform(
                        MockMvcRequestBuilders.put("/domain")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(tenantJson)
                                .accept(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andExpect(jsonPath("id", is(1)))
                .andExpect(jsonPath("companyName", is("MyNewAwesomerCompany")));
        Mockito.verify(userUtils).getCurrentUser();
        Mockito.verify(tenantService).updateTenant(newTenant);

    }

    @Test
    void updateDomainTestFailureNotAdmin() throws Exception {
        when(userUtils.getCurrentUser()).thenReturn(currentUser2);
        String tenantJson = mapper.writeValueAsString(newTenant);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/domain")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(tenantJson)
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isForbidden());
        Mockito.verify(userUtils).getCurrentUser();
    }

    @Test
    void updateDomainTestFailureUpdateException() throws Exception {
        when(userUtils.getCurrentUser()).thenReturn(currentUser1);
        when(tenantService.updateTenant(newTenant)).thenThrow(
                new OperationNotSupportedException("Operation not supported"));

        String tenantJson = mapper.writeValueAsString(newTenant);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/domain")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(tenantJson)
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isBadRequest());
        Mockito.verify(userUtils).getCurrentUser();
        Mockito.verify(tenantService).updateTenant(newTenant);
    }
}
