package com.skolarli.lmsservice.controller;

import com.skolarli.lmsservice.models.AuthenticationRequest;
import com.skolarli.lmsservice.models.AuthenticationResponse;
import com.skolarli.lmsservice.services.LMSUserDetailsService;
import com.skolarli.lmsservice.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private LMSUserDetailsService userDetailsService;
    @Autowired
    private JwtUtils jwtUtil;

    @RequestMapping(value = "/authenticate", method = RequestMethod.POST)
    public ResponseEntity<?> authenticate(@RequestBody AuthenticationRequest authenticationRequest) throws  Exception{
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword())
            );
        } catch(Exception e) {
            throw new Exception("Incorrect username or password", e);
        }
        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
        final String jwt = jwtUtil.generateToken(userDetails);

        return ResponseEntity.ok(new AuthenticationResponse(jwt));
    }
}
