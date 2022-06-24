package com.skolarli.lmsservice.controller;


import com.skolarli.lmsservice.exception.OperationNotSupportedException;
import com.skolarli.lmsservice.models.db.Tenant;
import com.skolarli.lmsservice.services.TenantService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;

/*
Controller for changing the data related to a domain for a logged in tenant
 */
@RestController
@RequestMapping(value="/domain")
public class DomainController {
    Logger logger = LoggerFactory.getLogger(DomainController.class);

    final TenantService tenantService;

    public DomainController(TenantService tenantService) {
        this.tenantService = tenantService;
    }

    @PutMapping
    public ResponseEntity<Tenant> updateDomain(@RequestBody Tenant tenant) {
        try {
            return new ResponseEntity<>(tenantService.updateTenant(tenant), HttpStatus.OK);
        } catch (OperationNotSupportedException e) {
            logger.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
}
