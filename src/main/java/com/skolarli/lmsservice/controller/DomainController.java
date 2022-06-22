package com.skolarli.lmsservice.controller;


import com.skolarli.lmsservice.models.db.Tenant;
import com.skolarli.lmsservice.services.TenantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/*
Controller for changing the data related to a domain for a logged in tenant
 */
@RestController
@RequestMapping(value="/domain")
public class DomainController {

    final
    TenantService tenantService;
    public DomainController(TenantService tenantService) {
        this.tenantService = tenantService;
    }

    @PutMapping
    public Tenant updateDomain(@RequestBody Tenant tenant) {
        // There is no need of validation for request body since we are not expecting all values here
        return  null;
    }
}
