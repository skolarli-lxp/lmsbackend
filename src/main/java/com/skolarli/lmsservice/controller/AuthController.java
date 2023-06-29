package com.skolarli.lmsservice.controller;

import com.skolarli.lmsservice.authentications.TenantAuthenticationToken;
import com.skolarli.lmsservice.contexts.TenantContext;
import com.skolarli.lmsservice.models.db.LmsUser;
import com.skolarli.lmsservice.models.db.Tenant;
import com.skolarli.lmsservice.models.dto.core.AuthenticationRequest;
import com.skolarli.lmsservice.models.dto.core.AuthenticationResponse;
import com.skolarli.lmsservice.services.LmsUserDetailsService;
import com.skolarli.lmsservice.services.LmsUserService;
import com.skolarli.lmsservice.services.TenantService;
import com.skolarli.lmsservice.utils.JwtUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import javax.validation.Valid;

@RestController
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
    @Autowired
    LmsUserService lmsUserService;
    @Autowired
    TenantService tenantService;
    @Autowired
    TenantContext tenantContext;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private LmsUserDetailsService userDetailsService;
    @Autowired
    private JwtUtils jwtUtil;

    @RequestMapping(value = "/authenticate", method = RequestMethod.POST)
    public ResponseEntity<?> authenticate(
            @Valid @RequestBody AuthenticationRequest authenticationRequest,
            @RequestHeader(value = "Domain") String domainName) {

        UUID uuid = UUID.randomUUID();
        MDC.put("requestId", uuid.toString());
        logger.info("Received Authentication Request for User: "
                + authenticationRequest.getUsername()
                + " for domain: " + domainName);

        logger.info("Received Authentication Request for User: "
                + authenticationRequest.getUsername());

        Tenant tenant = tenantService.getTenantByDomainName(domainName);
        String userName = authenticationRequest.getUsername();

        TenantAuthenticationToken tenantAuthenticationToken = new TenantAuthenticationToken(
                "", tenant.getId());
        SecurityContextHolder.getContext().setAuthentication(tenantAuthenticationToken);

        LmsUser lmsUser = lmsUserService.getLmsUserByEmailAndTenantId(
                userName, tenant.getId());
        if (!lmsUser.getEmailVerified()) {
            logger.error("User Email " + userName + " is not verified");
            return new ResponseEntity<>("{\"error\" : \"User Email not verified\"}",
                    HttpStatus.UNAUTHORIZED);
        }

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(userName,
                            authenticationRequest.getPassword())
            );
        } catch (Exception e) {
            logger.error("Authentication failed for user: " + authenticationRequest.getUsername());
            logger.error(e.getMessage());
            return new ResponseEntity<>("{\"error\" : \"Incorrect username or password\"}",
                    HttpStatus.UNAUTHORIZED);
        }
        final UserDetails userDetails = userDetailsService.loadUserByUsername(
                authenticationRequest.getUsername());
        final String jwt = jwtUtil.generateToken(userDetails, tenant.getId());

        logger.info("Authenticated User: " + userDetails.getUsername());
        MDC.remove("requestId");

        return ResponseEntity.ok(new AuthenticationResponse(jwt));
    }
}
