package com.skolarli.lmsservice.controller.core;

import com.skolarli.lmsservice.authentications.TenantAuthenticationToken;
import com.skolarli.lmsservice.models.db.core.LmsUser;
import com.skolarli.lmsservice.models.db.core.Tenant;
import com.skolarli.lmsservice.models.dto.core.PasswordResetRequest;
import com.skolarli.lmsservice.models.dto.core.PasswordResetTokenResponse;
import com.skolarli.lmsservice.services.core.LmsUserService;
import com.skolarli.lmsservice.services.core.TenantService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import javax.validation.Valid;


@RestController
@RequestMapping(value = "/passwordreset")
public class PasswordResetController {

    final Logger logger = LoggerFactory.getLogger(PasswordResetController.class);

    final LmsUserService lmsUserService;

    final TenantService tenantService;


    public PasswordResetController(LmsUserService lmsUserService,
                                   TenantService tenantService) {
        this.lmsUserService = lmsUserService;
        this.tenantService = tenantService;
    }

    @GetMapping(value = "/request")
    public ResponseEntity<PasswordResetTokenResponse> requestPasswordReset(
            @RequestParam String emailId,
            @RequestHeader("Domain") String domainName) {

        UUID uuid = UUID.randomUUID();
        MDC.put("requestId", uuid.toString());
        logger.info("Received password reset request for emailId: " + emailId);

        Tenant savedTenant = tenantService.getTenantByDomainName(domainName);
        SecurityContextHolder.getContext().setAuthentication(
                new TenantAuthenticationToken("", savedTenant.getId()));
        LmsUser user = lmsUserService.getLmsUserByEmailAndTenantId(emailId, savedTenant.getId());

        return new ResponseEntity<>(lmsUserService.createAndGetPasswordResetToken(user),
                HttpStatus.OK);
    }

    @GetMapping(value = "/doreset")
    public ResponseEntity<String> verifyToken(@Valid @RequestBody PasswordResetRequest request,
                                              @RequestHeader("Domain") String domainName) {

        UUID uuid = UUID.randomUUID();
        MDC.put("requestId", uuid.toString());
        logger.info("Received password reset verification request Token: " + request.getToken());

        try {
            lmsUserService.resetPassword(request.getToken(), request.getNewPassword());
        } catch (Exception e) {
            logger.error("Error while resetting password", e);
            return new ResponseEntity<>("Error while resetting password",
                    HttpStatus.BAD_REQUEST);
        }

        MDC.remove("requestId");
        return new ResponseEntity<>("OK", HttpStatus.OK);
    }
}
