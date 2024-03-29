package com.skolarli.lmsservice.controller.core;


import com.skolarli.lmsservice.exception.OperationNotSupportedException;
import com.skolarli.lmsservice.models.db.core.LmsUser;
import com.skolarli.lmsservice.models.db.core.Tenant;
import com.skolarli.lmsservice.services.core.TenantService;
import com.skolarli.lmsservice.utils.UserUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

/*
Controller for changing the data related to a domain for a logged in tenant
 */
@RestController
@RequestMapping(value = "/domain")
public class DomainController {
    final Logger logger = LoggerFactory.getLogger(DomainController.class);
    final TenantService tenantService;
    private final UserUtils userUtils;

    public DomainController(TenantService tenantService, UserUtils userUtils) {
        this.tenantService = tenantService;
        this.userUtils = userUtils;
    }

    @GetMapping
    public ResponseEntity<Tenant> getDomain() {
        UUID uuid = UUID.randomUUID();
        MDC.put("requestId", uuid.toString());
        logger.info("Received request for getCurrentDomain:");

        Tenant currentTenant = tenantService.getCurrentTenant();
        return new ResponseEntity<>(currentTenant, HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<Tenant> updateDomain(@RequestBody Tenant tenant) {

        UUID uuid = UUID.randomUUID();
        MDC.put("requestId", uuid.toString());
        logger.info("Received request for updateDomain: " + tenant.getDomainName());

        LmsUser currentUser = userUtils.getCurrentUser();
        if (!currentUser.getIsAdmin()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                "This user does not have permission to update the domain");
        }

        try {
            return new ResponseEntity<>(tenantService.updateTenant(tenant), HttpStatus.OK);
        } catch (OperationNotSupportedException e) {
            logger.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        } finally {
            MDC.remove("requestId");
        }
    }
}
