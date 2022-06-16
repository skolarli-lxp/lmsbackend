package com.skolarli.lmsservice.controller;

import com.skolarli.lmsservice.authentications.TenantAuthenticationToken;
import com.skolarli.lmsservice.models.NewDomainRequest;
import com.skolarli.lmsservice.models.NewDomainResponse;
import com.skolarli.lmsservice.models.db.LmsUser;
import com.skolarli.lmsservice.models.db.Tenant;
import com.skolarli.lmsservice.services.LmsUserService;
import com.skolarli.lmsservice.services.TenantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value="domain")
public class DomainController {

    @Autowired
    private TenantService tenantService;

    @Autowired
    private LmsUserService lmsUserService;

    @PostMapping
    public ResponseEntity<NewDomainResponse> addNewDomain(@RequestBody NewDomainRequest newDomainRequest) {
        Tenant tenant = new Tenant(newDomainRequest.getDomainName(), newDomainRequest.getCompanyName(), newDomainRequest.getWebsite());
        Tenant savedTenant = tenantService.saveTenant(tenant);

        LmsUser lmsUser = new LmsUser(newDomainRequest.getFirstName(), newDomainRequest.getLastName(),
                newDomainRequest.getEmail(), newDomainRequest.getPassword(), true);
        SecurityContextHolder.getContext().setAuthentication(
                new TenantAuthenticationToken(newDomainRequest.getEmail(), savedTenant.getId()));
        LmsUser savedLmsUser = lmsUserService.saveLmsUser(lmsUser);


        return new ResponseEntity<NewDomainResponse> (new NewDomainResponse(savedTenant, savedLmsUser),HttpStatus.OK );
    }
}
