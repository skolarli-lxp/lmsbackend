package com.skolarli.lmsservice.controller;

import com.skolarli.lmsservice.authentications.TenantAuthenticationToken;
import com.skolarli.lmsservice.models.db.LmsUser;
import com.skolarli.lmsservice.models.db.Tenant;
import com.skolarli.lmsservice.models.db.VerificationCode;
import com.skolarli.lmsservice.services.LmsUserService;
import com.skolarli.lmsservice.services.TenantService;
import com.skolarli.lmsservice.services.VerificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;


@RestController
@RequestMapping(value = "/verify")
public class VerificationController {

    final Logger logger = LoggerFactory.getLogger(VerificationController.class);

    final VerificationService verificationService;

    final LmsUserService lmsUserService;

    final TenantService tenantService;

    public VerificationController(VerificationService verificationService,
                                  LmsUserService lmsUserService,
                                  TenantService tenantService) {
        this.verificationService = verificationService;
        this.lmsUserService = lmsUserService;
        this.tenantService = tenantService;
    }

    @GetMapping(value = "/verifytoken")
    public ResponseEntity<String> verifyToken(@RequestParam String token) {
        logger.info("Received user verification request Token: " + token);
        Boolean result = verificationService.verifyCode(token);
        if (result) {
            return new ResponseEntity<>("OK", HttpStatus.OK);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Verification token expired");
        }
    }

    @GetMapping(value = "/gettoken")
    public ResponseEntity<VerificationCode> getToken(@RequestParam Long userId) {
        VerificationCode code = verificationService.getVerificationCodeByUser(userId);
        return new ResponseEntity<>(code, HttpStatus.OK);
    }

    @PostMapping(value = "/regeneratetoken")
    public ResponseEntity<VerificationCode> generateToken(@RequestParam Long userId,
                                                          @RequestHeader("Domain")
                                                          String domainName) {
        Tenant savedTenant = tenantService.getTenantByDomainName(domainName);
        SecurityContextHolder.getContext().setAuthentication(
                new TenantAuthenticationToken("", savedTenant.getId()));
        LmsUser user = lmsUserService.getLmsUserById(userId);
        VerificationCode code = verificationService.generateAndSaveVerificationCode(user);
        return new ResponseEntity<>(code, HttpStatus.OK);
    }
}
