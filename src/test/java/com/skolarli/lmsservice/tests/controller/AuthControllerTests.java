package com.skolarli.lmsservice.tests.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.skolarli.lmsservice.exception.OperationNotSupportedException;
import com.skolarli.lmsservice.models.AuthenticationRequest;
import com.skolarli.lmsservice.models.db.LmsUser;
import com.skolarli.lmsservice.models.db.Tenant;
import com.skolarli.lmsservice.services.LMSUserDetailsService;
import com.skolarli.lmsservice.services.TenantService;
import com.skolarli.lmsservice.utils.JwtUtils;
import com.skolarli.lmsservice.utils.UserUtils;
import org.apache.tomcat.websocket.AuthenticationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
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

import static org.hamcrest.Matchers.any;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTests {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private AuthenticationManager authenticationManager;
    @MockBean
    private LMSUserDetailsService userDetailsService;
    @MockBean
    private JwtUtils jwtUtil;

    @MockBean
    UserDetails userDetails;

    AuthenticationRequest authenticationRequest;


    @BeforeEach
    public  void setup() throws Exception {
        authenticationRequest = new AuthenticationRequest("mymockusername","mymockpassword");


    }

    @Test
    void updateDomainTestSuccess() throws Exception{
        when(authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                authenticationRequest.getUsername(), authenticationRequest.getPassword()))).thenReturn(null);
        when(userDetailsService.loadUserByUsername(authenticationRequest.getUsername())).thenReturn(userDetails);
        when(jwtUtil.generateToken(userDetails)).thenReturn("my-mock-jwt-token");
        String requestJson = mapper.writeValueAsString(authenticationRequest);
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                authenticationRequest.getUsername(), authenticationRequest.getPassword());

        mockMvc.perform(
                        MockMvcRequestBuilders.post("/authenticate")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestJson)
                                .accept(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andExpect(jsonPath("jwt", is("my-mock-jwt-token")));
        Mockito.verify(authenticationManager).authenticate(token);
        Mockito.verify(userDetailsService).loadUserByUsername(authenticationRequest.getUsername());
        Mockito.verify(jwtUtil).generateToken(ArgumentMatchers.any(UserDetails.class));
    }

    @Test
    void updateDomainTestFailureAuthFailure() throws Exception{
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                authenticationRequest.getUsername(), authenticationRequest.getPassword());
        String requestJson = mapper.writeValueAsString(authenticationRequest);


        when(authenticationManager.authenticate(token)).thenThrow(
                new RuntimeException("Incorrect username or password"));
        when(userDetailsService.loadUserByUsername(authenticationRequest.getUsername()))
                .thenReturn(userDetails);

        mockMvc.perform(
                        MockMvcRequestBuilders.post("/authenticate")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestJson)
                                .accept(MediaType.APPLICATION_JSON)
                ).andExpect(status().isUnauthorized())
                .andExpect(jsonPath("error", is("Incorrect username or password")));
        Mockito.verify(authenticationManager).authenticate(token);
    }
}
