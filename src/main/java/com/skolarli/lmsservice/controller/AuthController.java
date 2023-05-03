package com.skolarli.lmsservice.controller;

import com.skolarli.lmsservice.contexts.TenantContext;
import com.skolarli.lmsservice.models.AuthenticationRequest;
import com.skolarli.lmsservice.models.AuthenticationResponse;
import com.skolarli.lmsservice.models.db.LmsUser;
import com.skolarli.lmsservice.services.LMSUserDetailsService;
import com.skolarli.lmsservice.services.LmsUserService;
import com.skolarli.lmsservice.utils.JwtUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class AuthController {

    Logger logger = LoggerFactory.getLogger(AuthController.class);
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private LMSUserDetailsService userDetailsService;
    @Autowired
    LmsUserService lmsUserService;
    @Autowired
    TenantContext tenantContext;
    @Autowired
    private JwtUtils jwtUtil;

    @RequestMapping( value = "/authenticate", method = RequestMethod.POST)
    public ResponseEntity<?> authenticate(
            @Valid @RequestBody AuthenticationRequest authenticationRequest)
            throws  Exception{
        logger.info("Received Authentication Request for User: "
                + authenticationRequest.getUsername());
        String userName = authenticationRequest.getUsername();
        LmsUser lmsUser = lmsUserService.getLmsUserByEmailAndTenantId(
                userName, tenantContext.getTenantId());
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
        } catch(Exception e) {
            logger.error("Authentication failed for user: " + authenticationRequest.getUsername());
            logger.error(e.getMessage());
            return new ResponseEntity<>("{\"error\" : \"Incorrect username or password\"}",
                    HttpStatus.UNAUTHORIZED);
        }
        final UserDetails userDetails = userDetailsService.loadUserByUsername(
                authenticationRequest.getUsername());
        final String jwt = jwtUtil.generateToken(userDetails);

        logger.info("Authenticated User: " + userDetails.getUsername());
        return ResponseEntity.ok(new AuthenticationResponse(jwt));
    }
}
