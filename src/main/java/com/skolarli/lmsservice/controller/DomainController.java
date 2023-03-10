package com.skolarli.lmsservice.controller;


import com.skolarli.lmsservice.exception.OperationNotSupportedException;
import com.skolarli.lmsservice.models.db.LmsUser;
import com.skolarli.lmsservice.models.db.Tenant;
import com.skolarli.lmsservice.services.TenantService;
import com.skolarli.lmsservice.utils.UserUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

/*
Controller for changing the data related to a domain for a logged in tenant
 */
@RestController
@RequestMapping(value="/domain")
public class DomainController {
    Logger logger = LoggerFactory.getLogger(DomainController.class);

    private final UserUtils userUtils;

    final TenantService tenantService;

    public DomainController(TenantService tenantService, UserUtils userUtils) {
        this.tenantService = tenantService;
        this.userUtils = userUtils;
    }

    @PutMapping
    public ResponseEntity<Tenant> updateDomain(@RequestBody Tenant tenant) {

        LmsUser currentUser = userUtils.getCurrentUser();
        if (currentUser.getIsAdmin() != true) {
            throw new ResponseStatusException( HttpStatus.FORBIDDEN, "This user does not have permission to update the domain");
        }

        try {
            return new ResponseEntity<>(tenantService.updateTenant(tenant), HttpStatus.OK);
        } catch (OperationNotSupportedException e) {
            logger.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
