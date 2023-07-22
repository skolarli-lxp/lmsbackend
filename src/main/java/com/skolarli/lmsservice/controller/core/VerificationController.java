package com.skolarli.lmsservice.controller.core;

import com.skolarli.lmsservice.authentications.TenantAuthenticationToken;
import com.skolarli.lmsservice.models.db.core.LmsUser;
import com.skolarli.lmsservice.models.db.core.Tenant;
import com.skolarli.lmsservice.models.db.core.VerificationCode;
import com.skolarli.lmsservice.services.core.LmsUserService;
import com.skolarli.lmsservice.services.core.TenantService;
import com.skolarli.lmsservice.services.core.VerificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;


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

        UUID uuid = UUID.randomUUID();
        MDC.put("requestId", uuid.toString());
        logger.info("Received user verification request Token: " + token);

        Boolean result = verificationService.verifyCode(token);

        MDC.remove("requestId");
        if (result) {
            return new ResponseEntity<>("OK", HttpStatus.OK);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Verification Failed");
        }
    }

    @GetMapping(value = "/gettoken")
    public ResponseEntity<VerificationCode> getToken(@RequestParam Long userId) {

        UUID uuid = UUID.randomUUID();
        MDC.put("requestId", uuid.toString());
        logger.info("Received get token request for user: " + userId);

        VerificationCode code = verificationService.getVerificationCodeByUser(userId);

        MDC.remove("requestId");
        return new ResponseEntity<>(code, HttpStatus.OK);
    }

    @PostMapping(value = "/regeneratetoken")
    public ResponseEntity<VerificationCode> generateToken(@RequestParam Long userId,
                                                          @RequestHeader("Domain")
                                                          String domainName) {

        UUID uuid = UUID.randomUUID();
        MDC.put("requestId", uuid.toString());
        logger.info("Received regenerate token request for user: " + userId);

        Tenant savedTenant = tenantService.getTenantByDomainName(domainName);
        SecurityContextHolder.getContext().setAuthentication(
                new TenantAuthenticationToken("", savedTenant.getId()));
        LmsUser user = lmsUserService.getLmsUserById(userId);
        VerificationCode code = verificationService.generateAndSaveVerificationCode(user);

        MDC.remove("requestId");
        return new ResponseEntity<>(code, HttpStatus.OK);
    }
}
