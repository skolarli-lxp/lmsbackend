package com.skolarli.lmsservice.controller;

import com.skolarli.lmsservice.authentications.TenantAuthenticationToken;
import com.skolarli.lmsservice.exception.OperationNotSupportedException;
import com.skolarli.lmsservice.models.NewDomainRequest;
import com.skolarli.lmsservice.models.NewDomainResponse;
import com.skolarli.lmsservice.models.db.LmsUser;
import com.skolarli.lmsservice.models.db.Tenant;
import com.skolarli.lmsservice.models.db.VerificationCode;
import com.skolarli.lmsservice.services.LmsUserService;
import com.skolarli.lmsservice.services.TenantService;
import com.skolarli.lmsservice.services.VerificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;

/*
Controller for Domain functions which are not authenticated
Like creating a new domain for new sign up
 */

@RestController
@RequestMapping(value = "/newdomain")
public class NewDomainController {

    Logger logger = LoggerFactory.getLogger(NewDomainController.class);
    @Autowired
    private TenantService tenantService;

    @Autowired
    private LmsUserService lmsUserService;

    @Autowired
    private VerificationService verificationService;

    @PostMapping(value = "/add")
    public ResponseEntity<NewDomainResponse> addNewDomain(
            @Valid @RequestBody NewDomainRequest newDomainRequest) {
        logger.info("Received new domain request. DomainName: "
                + newDomainRequest.getDomainName()
                + " Email: " + newDomainRequest.getEmail());
        Tenant savedTenant;
        LmsUser savedLmsUser;
        VerificationCode code;

        Tenant tenant = new Tenant(newDomainRequest);
        try {
            savedTenant = tenantService.saveTenant(tenant);
        } catch (DataIntegrityViolationException e) {
            logger.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Domain name is not unique");
        } catch (OperationNotSupportedException e) {
            logger.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
        logger.info("Added new tenant DomainName: " + savedTenant.getDomainName());

        LmsUser lmsUser = new LmsUser(newDomainRequest);
        SecurityContextHolder.getContext().setAuthentication(
                new TenantAuthenticationToken(newDomainRequest.getEmail(), savedTenant.getId()));
        try {
            savedLmsUser = lmsUserService.saveLmsUser(lmsUser);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
        try {
            code = verificationService.generateAndSaveVerificationCode(savedLmsUser);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
        logger.info("Added new user Email: " + savedLmsUser.getEmail());

        return new ResponseEntity<>(
                new NewDomainResponse(savedTenant, savedLmsUser, code), HttpStatus.CREATED);
    }


    @GetMapping(value = "/isunique")
    public ResponseEntity<String> isUnique(@RequestParam String domainName) {
        logger.info("Checking if domainName " + domainName + " is unique");
        domainName = domainName.strip();
        if (domainName == null || domainName.isEmpty()) {
            return new ResponseEntity<>("{\"error\" : \"domainName cannot be null\"}",
                    HttpStatus.BAD_REQUEST);
        }
        try {
            String result = tenantService.isUniqueDomainName(domainName).toString();
            String responseString = "{ \n \"result\" : \"" + result + "\" \n}";
            return new ResponseEntity<>(responseString, HttpStatus.OK);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
