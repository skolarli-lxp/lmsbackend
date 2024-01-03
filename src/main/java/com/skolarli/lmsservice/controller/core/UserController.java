package com.skolarli.lmsservice.controller.core;

import com.skolarli.lmsservice.contexts.TenantContext;
import com.skolarli.lmsservice.exception.OperationNotSupportedException;
import com.skolarli.lmsservice.exception.ResourceNotFoundException;
import com.skolarli.lmsservice.models.Role;
import com.skolarli.lmsservice.models.db.core.LmsUser;
import com.skolarli.lmsservice.models.db.core.VerificationCode;
import com.skolarli.lmsservice.models.db.course.Batch;
import com.skolarli.lmsservice.models.dto.core.GetLmsUserResponse;
import com.skolarli.lmsservice.services.core.LmsUserService;
import com.skolarli.lmsservice.services.core.VerificationService;
import com.skolarli.lmsservice.utils.UserUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;
import javax.validation.Valid;

@RestController
@RequestMapping(value = "user")
public class UserController {

    final Logger logger = LoggerFactory.getLogger(UserController.class);
    @Autowired
    TenantContext tenantContext;
    @Autowired
    VerificationService verificationService;
    @Autowired
    private UserUtils userUtils;
    @Autowired
    private LmsUserService lmsUserService;

    @GetMapping
    public ResponseEntity<List<LmsUser>> getAllUsers() {

        UUID uuid = UUID.randomUUID();
        MDC.put("requestId", uuid.toString());
        logger.info("Received List All Users request");

        LmsUser currentUser = userUtils.getCurrentUser();
        if (!currentUser.getIsAdmin()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Permission denied");
        }
        return new ResponseEntity<>(lmsUserService.getAllLmsUsers(), HttpStatus.OK);
    }

    @GetMapping(value = "{id}")
    public ResponseEntity<LmsUser> getUser(@PathVariable long id) {

        UUID uuid = UUID.randomUUID();
        MDC.put("requestId", uuid.toString());
        logger.info("Received Get User request UserId: " + id);

        LmsUser currentUser = userUtils.getCurrentUser();
        if (!currentUser.getIsAdmin() && currentUser.getId() != id) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Permission denied");
        }
        return new ResponseEntity<>(lmsUserService.getLmsUserById(id), HttpStatus.OK);
    }

    @GetMapping(value = "email2/{email}")
    public ResponseEntity<LmsUser> getUserByEmail2(@PathVariable String email) {

        UUID uuid = UUID.randomUUID();
        MDC.put("requestId", uuid.toString());
        logger.info("Received Get User request Email: " + email);

        long tenantId = tenantContext.getTenantId();
        return new ResponseEntity<>(lmsUserService.getLmsUserByEmailAndTenantId(email, tenantId),
            HttpStatus.OK);
    }

    @GetMapping(value = "email/{email}")
    public ResponseEntity<GetLmsUserResponse> getUserByEmail(@PathVariable String email) {

        UUID uuid = UUID.randomUUID();
        MDC.put("requestId", uuid.toString());
        logger.info("Received Get User request Email: " + email);

        long tenantId = tenantContext.getTenantId();
        LmsUser user = lmsUserService.getLmsUserByEmailAndTenantId(email, tenantId);
        return new ResponseEntity<>(new GetLmsUserResponse(user), HttpStatus.OK);
    }

    @GetMapping(value = "role")
    public ResponseEntity<List<LmsUser>> getUserByRole(@RequestParam("role") Role role) {

        UUID uuid = UUID.randomUUID();
        MDC.put("requestId", uuid.toString());
        logger.info("Received getUserByRole request " + role);

        return new ResponseEntity<>(lmsUserService.getLmsUsersByRole(role), HttpStatus.OK);
    }

    @GetMapping(value = "getAllEnrolledBatches")
    public ResponseEntity<List<Batch>> getAllEnrolledBatches(
        @RequestParam(required = false) Long studentId,
        @RequestParam(required = false) String studentEmail) {

        UUID uuid = UUID.randomUUID();
        MDC.put("requestId", uuid.toString());

        logger.info("Received getAllEnrolledBatches request" + (studentId != null ? " studentId: "
            + studentId : " studentEmail: " + studentEmail));

        LmsUser currentUser = userUtils.getCurrentUser();
        if (!currentUser.getIsAdmin() && currentUser.getId() != studentId && !currentUser.getEmail()
            .equals(studentEmail)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Permission denied");
        }
        if (studentId == null && (studentEmail == null || studentEmail.isEmpty())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                "Request should contain either studentId or studentEmail");
        }
        try {
            if (studentId != null) {
                return new ResponseEntity<>(
                    lmsUserService.getBatchesEnrolledForStudent(studentId), HttpStatus.OK);
            } else if (studentEmail != null && !studentEmail.isEmpty()) {
                return new ResponseEntity<>(
                    lmsUserService.getBatchesEnrolledForStudent(studentEmail), HttpStatus.OK);
            }
        } catch (OperationNotSupportedException | ResourceNotFoundException e) {
            logger.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } finally {
            MDC.remove("requestId");
        }
        return null;
    }

    @GetMapping(value = "getAllBatchesTaught")
    public ResponseEntity<List<Batch>> getAllTaughtBatches(
        @RequestParam(required = false) Long instructorId,
        @RequestParam(required = false) String instructorEmail) {

        UUID uuid = UUID.randomUUID();
        MDC.put("requestId", uuid.toString());
        logger.info("Received getAllBatchesTaught request "
            + (instructorId != null ? " instructorId: "
            + instructorId : instructorEmail != null ? "instructorEmail: " + instructorEmail : ""));

        LmsUser currentUser = userUtils.getCurrentUser();
        if (!currentUser.getIsAdmin() && currentUser.getId() != instructorId
            && !currentUser.getEmail().equals(instructorEmail)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Permission denied");
        }
        if (instructorId == null && (instructorEmail == null || instructorEmail.isEmpty())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                "Request should contain either instructorId or instructorEmail");
        }
        try {
            if (instructorId != null) {
                return new ResponseEntity<>(
                    lmsUserService.getBatchesTaughtByInstructor(instructorId), HttpStatus.OK);
            } else if (null != instructorEmail && !instructorEmail.isEmpty()) {
                return new ResponseEntity<>(
                    lmsUserService.getBatchesTaughtByInstructor(instructorEmail),
                    HttpStatus.OK);
            }
        } catch (OperationNotSupportedException | ResourceNotFoundException e) {
            logger.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } finally {
            MDC.remove("requestId");
        }
        return null;
    }

    @PostMapping
    public ResponseEntity<LmsUser> addNewUser(@Valid @RequestBody LmsUser lmsUser) {

        UUID uuid = UUID.randomUUID();
        MDC.put("requestId", uuid.toString());
        logger.info("Received new user request Email: " + lmsUser.getEmail());

        LmsUser currentUser = userUtils.getCurrentUser();
        if (!currentUser.getIsAdmin()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Permission denied");
        }
        lmsUser.setUserIsDeleted(false);
        lmsUser.setEmailVerified(false);
        lmsUser.setIsSuperAdmin(false);
        LmsUser savedUser = lmsUserService.saveLmsUser(lmsUser);
        VerificationCode code = verificationService.generateAndSaveVerificationCode(savedUser);
        savedUser.setVerificationCode(code);
        MDC.remove("requestId");

        return new ResponseEntity<LmsUser>(savedUser, HttpStatus.CREATED);
    }

    @PostMapping(value = "addMultiple")
    public ResponseEntity<List<LmsUser>> addMultipleUsers(@Valid @RequestBody List<LmsUser> lmsUsers) {
        UUID uuid = UUID.randomUUID();
        MDC.put("requestId", uuid.toString());
        logger.info("Received request to add multiple users");

        LmsUser currentUser = userUtils.getCurrentUser();
        if (!currentUser.getIsAdmin()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Permission denied");
        }

        List<LmsUser> savedUsers = lmsUserService.saveLmsUsers(lmsUsers);
        for (LmsUser savedUser : savedUsers) {
            VerificationCode code = verificationService.generateAndSaveVerificationCode(savedUser);
            savedUser.setVerificationCode(code);
        }

        MDC.remove("requestId");
        return new ResponseEntity(savedUsers, HttpStatus.CREATED);
    }

    @PutMapping(value = "{id}")
    public ResponseEntity<LmsUser> updateUser(@PathVariable long id, @RequestBody LmsUser lmsUser) {

        UUID uuid = UUID.randomUUID();
        MDC.put("requestId", uuid.toString());
        logger.info("Received update user request Email: " + id);

        LmsUser currentUser = userUtils.getCurrentUser();
        if (!currentUser.getIsAdmin() && currentUser.getId() != id) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Permission denied");
        }

        try {
            return new ResponseEntity<>(lmsUserService.updateLmsUser(lmsUser, id), HttpStatus.OK);
        } catch (OperationNotSupportedException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        } finally {
            MDC.remove("requestId");
        }
    }

    @DeleteMapping(value = "{id}")
    public ResponseEntity<String> deleteUser(@PathVariable long id) {

        UUID uuid = UUID.randomUUID();
        MDC.put("requestId", uuid.toString());
        logger.info("Received Delete User request UserId: " + id);

        LmsUser currentUser = userUtils.getCurrentUser();
        if (!currentUser.getIsAdmin()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Permission denied");
        }
        lmsUserService.deleteLmsUser(id);
        MDC.remove("requestId");

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(value = "hard/{id}")
    public ResponseEntity<String> hardDeleteUser(@PathVariable long id) {

        UUID uuid = UUID.randomUUID();
        MDC.put("requestId", uuid.toString());
        logger.info("Received Delete User request UserId: " + id);

        LmsUser currentUser = userUtils.getCurrentUser();
        if (!currentUser.getIsAdmin()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Permission denied");
        }
        lmsUserService.hardDeleteLmsUser(id);
        MDC.remove("requestId");

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
