package com.skolarli.lmsservice.controller;

import com.skolarli.lmsservice.contexts.TenantContext;
import com.skolarli.lmsservice.exception.OperationNotSupportedException;
import com.skolarli.lmsservice.exception.ResourceNotFoundException;
import com.skolarli.lmsservice.models.Role;
import com.skolarli.lmsservice.models.db.Batch;
import com.skolarli.lmsservice.models.db.LmsUser;
import com.skolarli.lmsservice.models.db.VerificationCode;
import com.skolarli.lmsservice.services.LmsUserService;
import com.skolarli.lmsservice.services.VerificationService;
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

    @Autowired
    VerificationService verificationService;

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

    @GetMapping(value = "getAllEnrolledBatches")
    public ResponseEntity<List<Batch>> getAllEnrolledBatches(
                                @RequestParam(required = false) Long studentId,
                                @RequestParam(required = false) String studentEmail) {

        logger.info("Received Get All Enrolled Batches request");
        LmsUser currentUser = userUtils.getCurrentUser();
        if (!currentUser.getIsAdmin() && currentUser.getId() != studentId && !currentUser.getEmail().equals(studentEmail)) {
            throw new ResponseStatusException( HttpStatus.FORBIDDEN, "Permission denied");
        }
        if (studentId == null && (studentEmail == null || studentEmail.isEmpty())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Request should contain either studentId or studentEmail");
        }
        try {
            if (studentId != null) {
                return new ResponseEntity<>(
                        lmsUserService.getBatchesEnrolledForStudent(studentId), HttpStatus.OK);
            } else if (studentEmail != null && !studentEmail.isEmpty()){
                return new ResponseEntity<>(
                        lmsUserService.getBatchesEnrolledForStudent(studentEmail), HttpStatus.OK);
            }
        }catch (OperationNotSupportedException | ResourceNotFoundException e) {
            logger.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
        return null;

    }

    @GetMapping( value = "getAllBatchesTaught")
    public ResponseEntity<List<Batch>> getAllTaughtBatches(
            @RequestParam(required = false) Long instructorId,
            @RequestParam(required = false) String instructorEmail) {
        logger.info("Received getAllBatchesTaught request");
        LmsUser currentUser = userUtils.getCurrentUser();
        if (!currentUser.getIsAdmin() && currentUser.getId() != instructorId && !currentUser.getEmail().equals(instructorEmail)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Permission denied");
        }
        if (instructorId == null && (instructorEmail == null || instructorEmail.isEmpty())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Request should contain either instructorId or instructorEmail");
        }
        try {
            if (instructorId != null) {
                return new ResponseEntity<>(
                        lmsUserService.getBatchesTaughtByInstructor(instructorId), HttpStatus.OK);
            } else if (null != instructorEmail && !instructorEmail.isEmpty()) {
                return new ResponseEntity<>(
                        lmsUserService.getBatchesTaughtByInstructor(instructorEmail), HttpStatus.OK);
            }
        } catch (OperationNotSupportedException | ResourceNotFoundException e) {
            logger.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
        return null;
    }

    @PostMapping
    public ResponseEntity<LmsUser> addNewUser(@Valid @RequestBody LmsUser lmsUser) {
        logger.info("Received new user request Email: " + lmsUser.getEmail() );
        LmsUser currentUser = userUtils.getCurrentUser();
        if (currentUser.getIsAdmin() != true) {
            throw new ResponseStatusException( HttpStatus.FORBIDDEN, "Permission denied");
        }
        lmsUser.setUserIsDeleted(false);
        lmsUser.setEmailVerified(false);
        LmsUser savedUser = lmsUserService.saveLmsUser(lmsUser);
        VerificationCode code = verificationService.generateAndSaveVerificationCode(savedUser);
        savedUser.setVerificationCode(code);
        return new ResponseEntity<LmsUser>(savedUser, HttpStatus.CREATED);
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
