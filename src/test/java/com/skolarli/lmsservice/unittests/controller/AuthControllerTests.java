package com.skolarli.lmsservice.unittests.controller;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.skolarli.lmsservice.contexts.TenantContext;
import com.skolarli.lmsservice.models.db.core.LmsUser;
import com.skolarli.lmsservice.models.db.core.Tenant;
import com.skolarli.lmsservice.models.dto.core.AuthenticationRequest;
import com.skolarli.lmsservice.services.LmsUserDetailsService;
import com.skolarli.lmsservice.services.LmsUserService;
import com.skolarli.lmsservice.services.TenantService;
import com.skolarli.lmsservice.utils.JwtUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTests {

    @Autowired
    MockMvc mockMvc;
    @MockBean
    UserDetails userDetails;
    AuthenticationRequest authenticationRequest;
    LmsUser lmsUser;
    Tenant tenant;
    @Autowired
    private ObjectMapper mapper;
    @MockBean
    private AuthenticationManager authenticationManager;
    @MockBean
    private LmsUserDetailsService userDetailsService;
    @MockBean
    private JwtUtils jwtUtil;
    @MockBean
    private TenantContext tenantContext;
    @MockBean
    private LmsUserService lmsUserService;
    @MockBean
    private TenantService tenantService;

    @BeforeEach
    public void setup() {
        lmsUser = new LmsUser();
        lmsUser.setEmailVerified(true);

        tenant = new Tenant();
        tenant.setId(1L);

        authenticationRequest = new AuthenticationRequest("mymockusername",
                "mymockpassword");


    }

    @Test
    void updateDomainTestSuccess() throws Exception {

        when(tenantService.getTenantByDomainName(anyString())).thenReturn(tenant);
        when(authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                authenticationRequest.getUsername(), authenticationRequest.getPassword())))
                .thenReturn(null);
        when(userDetailsService.loadUserByUsername(authenticationRequest.getUsername()))
                .thenReturn(userDetails);
        when(jwtUtil.generateToken(userDetails, 1L)).thenReturn("my-mock-jwt-token");
        when(tenantContext.getTenantId()).thenReturn(1L);
        when(lmsUserService.getLmsUserByEmailAndTenantId(anyString(), anyLong()))
                .thenReturn(lmsUser);

        String requestJson = mapper.writeValueAsString(authenticationRequest);
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                authenticationRequest.getUsername(), authenticationRequest.getPassword());

        mockMvc.perform(
                        MockMvcRequestBuilders.post("/authenticate")
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("domain", "mydomain.com")
                                .content(requestJson)
                                .accept(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andExpect(jsonPath("jwt", is("my-mock-jwt-token")));
        Mockito.verify(authenticationManager).authenticate(token);
        Mockito.verify(userDetailsService).loadUserByUsername(authenticationRequest.getUsername());
        Mockito.verify(jwtUtil).generateToken(any(UserDetails.class), anyLong());
    }

    @Test
    void updateDomainTestFailureAuthFailure() throws Exception {

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                authenticationRequest.getUsername(), authenticationRequest.getPassword());
        final String requestJson = mapper.writeValueAsString(authenticationRequest);

        when(tenantService.getTenantByDomainName(anyString())).thenReturn(tenant);
        when(authenticationManager.authenticate(token)).thenThrow(
                new RuntimeException("Incorrect username or password"));
        when(userDetailsService.loadUserByUsername(authenticationRequest.getUsername()))
                .thenReturn(userDetails);
        when(tenantContext.getTenantId()).thenReturn(1L);
        when(lmsUserService.getLmsUserByEmailAndTenantId(anyString(), anyLong()))
                .thenReturn(lmsUser);


        mockMvc.perform(
                        MockMvcRequestBuilders.post("/authenticate")
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("domain", "mydomain.com")
                                .content(requestJson)
                                .accept(MediaType.APPLICATION_JSON)
                ).andExpect(status().isUnauthorized())
                .andExpect(jsonPath("error", is("Incorrect username or password")));
        Mockito.verify(authenticationManager).authenticate(token);
    }
}
