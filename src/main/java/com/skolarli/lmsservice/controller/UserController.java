package com.skolarli.lmsservice.controller;

import com.skolarli.lmsservice.contexts.TenantContext;
import com.skolarli.lmsservice.exception.OperationNotSupportedException;
import com.skolarli.lmsservice.models.Role;
import com.skolarli.lmsservice.models.db.LmsUser;
import com.skolarli.lmsservice.models.db.VerificationCode;
import com.skolarli.lmsservice.services.LmsUserService;
import com.skolarli.lmsservice.utils.UserUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "user")
public class UserController {

    Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserUtils userUtils;

    @Autowired
    private LmsUserService lmsUserService;

    @Autowired
    TenantContext tenantContext;

    @GetMapping
    public ResponseEntity<List<LmsUser>> getAllUsers() {
        logger.info("Received List All Users request");
        LmsUser currentUser = userUtils.getCurrentUser();
        if (currentUser.getIsAdmin() != true) {
            throw new ResponseStatusException( HttpStatus.FORBIDDEN, "Permission denied");
        }
        return new ResponseEntity<>(lmsUserService.getAllLmsUsers(), HttpStatus.OK);
    }

    @GetMapping(value = "{id}")
    public ResponseEntity<LmsUser> getUser(@PathVariable long id) {
        logger.info("Received Get User request UserId: " + id);
        LmsUser currentUser = userUtils.getCurrentUser();
        if (currentUser.getIsAdmin() != true && currentUser.getId() != id) {
            throw new ResponseStatusException( HttpStatus.FORBIDDEN, "Permission denied");
        }
        return new ResponseEntity<>(lmsUserService.getLmsUserById(id), HttpStatus.OK);
    }

    @GetMapping(value = "email/{email}")
    public ResponseEntity<LmsUser> getUserByEmail(@PathVariable String email) {
        logger.info("Received Get User request Email: " + email);
        long tenantId = tenantContext.getTenantId();
        return new ResponseEntity<>(lmsUserService.getLmsUserByEmailAndTenantId(email, tenantId), HttpStatus.OK);
    }

    @GetMapping(value = "role")
    public ResponseEntity<List<LmsUser>> getUserByRole(@RequestParam("role") Role role) {
        logger.info("Received Get User request Role: " + role);
        long tenantId = tenantContext.getTenantId();
        return new ResponseEntity<>(lmsUserService.getLmsUsersByRole(role), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<LmsUser> addNewUser(@Valid @RequestBody LmsUser lmsUser) {
        logger.info("Received new user request Email: " + lmsUser.getEmail() );
        LmsUser currentUser = userUtils.getCurrentUser();
        if (currentUser.getIsAdmin() != true) {
            throw new ResponseStatusException( HttpStatus.FORBIDDEN, "Permission denied");
        }
        lmsUser.setUserIsDeleted(false);
        return new ResponseEntity<LmsUser>(lmsUserService.saveLmsUser(lmsUser), HttpStatus.CREATED);
    }

    @GetMapping(value = "/verificationCode/{id}")
    public ResponseEntity<VerificationCode> generateVerificationCode(@PathVariable long id) {
        logger.info("Received generate code Request Id: " + id );
        LmsUser currentUser = userUtils.getCurrentUser();
        if (currentUser.getIsAdmin() != true) {
            throw new ResponseStatusException( HttpStatus.FORBIDDEN, "Permission denied");
        }
        return new ResponseEntity<>(lmsUserService.generateVerificationCode(id), HttpStatus.OK);
    }

    @PutMapping(value="{id}")
    public ResponseEntity<LmsUser> updateUser(@PathVariable long id, @RequestBody LmsUser lmsUser) {
        logger.info("Received update user request Email: " + lmsUser.getEmail() );
        LmsUser currentUser = userUtils.getCurrentUser();
        if (currentUser.getIsAdmin() != true && currentUser.getId() != id) {
            throw new ResponseStatusException( HttpStatus.FORBIDDEN, "Permission denied");
        }

        try {
            return new ResponseEntity<>(lmsUserService.updateLmsUser(lmsUser, id), HttpStatus.OK);
        } catch (OperationNotSupportedException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @DeleteMapping(value = "{id}")
    public ResponseEntity<String> deleteUser(@PathVariable long id) {
        logger.info("Received Delete User request UserId: " + id);
        LmsUser currentUser = userUtils.getCurrentUser();
        if (currentUser.getIsAdmin() != true) {
            throw new ResponseStatusException( HttpStatus.FORBIDDEN, "Permission denied");
        }
        lmsUserService.deleteLmsUser(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(value = "hard/{id}")
    public ResponseEntity<String> hardDeleteUser(@PathVariable long id) {
        logger.info("Received Delete User request UserId: " + id);
        LmsUser currentUser = userUtils.getCurrentUser();
        if (currentUser.getIsAdmin() != true) {
            throw new ResponseStatusException( HttpStatus.FORBIDDEN, "Permission denied");
        }
        lmsUserService.hardDeleteLmsUser(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
