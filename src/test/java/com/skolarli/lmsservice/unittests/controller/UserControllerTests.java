package com.skolarli.lmsservice.unittests.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.skolarli.lmsservice.exception.OperationNotSupportedException;
import com.skolarli.lmsservice.models.db.LmsUser;
import com.skolarli.lmsservice.models.db.Tenant;
import com.skolarli.lmsservice.services.TenantService;
import com.skolarli.lmsservice.utils.UserUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class UserControllerTests {

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
    private Tenant existingTenant;
    private Tenant newTenant;


    @BeforeEach
    public  void setup() throws Exception {
        currentUser1 = new LmsUser();
        currentUser1.setId(1);
        currentUser1.setFirstName("Jaya");
        currentUser1.setLastName("Nair");
        currentUser1.setEmail("jaya@skolarli.com");
        currentUser1.setPassword("mymockpassword");
        currentUser1.setIsAdmin(true);
        currentUser1.setIsInstructor(false);
        currentUser1.setIsStudent(false);

        currentUser2 = new  LmsUser();
        currentUser2.setId(2);
        currentUser2.setFirstName("Jaya");
        currentUser2.setLastName("Nair");
        currentUser2.setPassword("mymockpassword");
        currentUser2.setEmail("jaya@skolarli.com");
        currentUser2.setPassword("mymockpassword");
        currentUser2.setIsAdmin(false);
        currentUser2.setIsInstructor(false);
        currentUser2.setIsStudent(false);

        existingTenant = new Tenant();
        existingTenant.setId(1);
        existingTenant.setDomainName("mydomainname");
        existingTenant.setCompanyName("MyAwesomeCompany");
        existingTenant.setCountryCode("+91");
        existingTenant.setPhoneNumber("1234561234");
        existingTenant.setCurrency("INR");
        existingTenant.setWebsite("myawesomewebsite.com");


        newTenant = new Tenant();
        newTenant.setId(1);
        newTenant.setDomainName("mydomainname");
        newTenant.setCompanyName("MyAwesomeCompany");
        newTenant.setCountryCode("+91");
        newTenant.setPhoneNumber("1234561234");
        newTenant.setCurrency("INR");
        newTenant.setWebsite("myawesomewebsite.com");
        newTenant.setAddress("my changed address");

    }

    @Test
    void updateDomainTestSuccess() throws Exception{
        when(userUtils.getCurrentUser()).thenReturn(currentUser1);
        when(tenantService.updateTenant(existingTenant)).thenReturn(newTenant);
        String tenantJson = mapper.writeValueAsString(newTenant);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/domain")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(tenantJson)
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());

    }

    @Test
    void updateDomainTestFailureNotAdmin() throws Exception{
        when(userUtils.getCurrentUser()).thenReturn(currentUser2);
        String tenantJson = mapper.writeValueAsString(newTenant);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/domain")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(tenantJson)
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isForbidden());
    }

    @Test
    void updateDomainTestFailureUpdateException() throws Exception{
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
    }
}
