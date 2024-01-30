package com.skolarli.lmsservice.controller.core;

import com.skolarli.lmsservice.contexts.TenantContext;
import com.skolarli.lmsservice.exception.OperationNotSupportedException;
import com.skolarli.lmsservice.exception.ResourceNotFoundException;
import com.skolarli.lmsservice.models.Role;
import com.skolarli.lmsservice.models.db.core.LmsUser;
import com.skolarli.lmsservice.models.db.core.VerificationCode;
import com.skolarli.lmsservice.models.db.course.Batch;
import com.skolarli.lmsservice.models.dto.core.GetLmsUserResponse;
import com.skolarli.lmsservice.models.dto.course.BatchResponse;
import com.skolarli.lmsservice.services.core.LmsUserService;
import com.skolarli.lmsservice.services.core.VerificationService;
import com.skolarli.lmsservice.utils.UserUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.validation.Valid;

@RestController
@RequestMapping(value = "user")
public class UserController {

    final Logger logger = LoggerFactory.getLogger(UserController.class);
    private final UserUtils userUtils;
    private final LmsUserService lmsUserService;
    TenantContext tenantContext;
    VerificationService verificationService;

    public UserController(UserUtils userUtils, LmsUserService lmsUserService,
                          VerificationService verificationService, TenantContext tenantContext) {
        this.userUtils = userUtils;
        this.lmsUserService = lmsUserService;
        this.verificationService = verificationService;
        this.tenantContext = tenantContext;
    }

    @GetMapping
    public ResponseEntity<?> getAllUsers(@RequestParam(required = false) Boolean condensed) {

        UUID uuid = UUID.randomUUID();
        MDC.put("requestId", uuid.toString());
        logger.info("Received List All Users request");

        LmsUser currentUser = userUtils.getCurrentUser();
        if (!currentUser.getIsAdmin()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Permission denied");
        }

        List<LmsUser> response = lmsUserService.getAllLmsUsers();
        if (condensed != null && condensed == Boolean.TRUE) {
            List<GetLmsUserResponse> getLmsUserResponses = response.stream()
                .map(GetLmsUserResponse::new).collect(Collectors.toList());
            return new ResponseEntity<>(getLmsUserResponses, HttpStatus.OK);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(value = "{id}")
    public ResponseEntity<?> getUser(@PathVariable long id,
                                     @RequestParam(required = false) Boolean condensed) {

        UUID uuid = UUID.randomUUID();
        MDC.put("requestId", uuid.toString());
        logger.info("Received Get User request UserId: " + id);

        LmsUser currentUser = userUtils.getCurrentUser();
        if (!currentUser.getIsAdmin() && currentUser.getId() != id) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Permission denied");
        }
        LmsUser response = lmsUserService.getLmsUserById(id);
        if (condensed != null && condensed == Boolean.TRUE) {
            return new ResponseEntity<>(new GetLmsUserResponse(response), HttpStatus.OK);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(value = "email2/{email}")
    public ResponseEntity<?> getUserByEmail2(@PathVariable String email,
                                             @RequestParam(required = false)
                                             Boolean condensed) {

        UUID uuid = UUID.randomUUID();
        MDC.put("requestId", uuid.toString());
        logger.info("Received Get User request Email: " + email);

        long tenantId = tenantContext.getTenantId();
        LmsUser response = lmsUserService.getLmsUserByEmailAndTenantId(email, tenantId);
        if (condensed != null && condensed == Boolean.TRUE) {
            return new ResponseEntity<>(new GetLmsUserResponse(response), HttpStatus.OK);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
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
    public ResponseEntity<?> getUserByRole(@RequestParam("role") Role role,
                                           @RequestParam(required = false) Boolean condensed) {

        UUID uuid = UUID.randomUUID();
        MDC.put("requestId", uuid.toString());
        logger.info("Received getUserByRole request " + role);

        List<LmsUser> response = lmsUserService.getLmsUsersByRole(role);
        if (condensed != null && condensed == Boolean.TRUE) {
            List<GetLmsUserResponse> getLmsUserResponses = response.stream()
                .map(GetLmsUserResponse::new).collect(Collectors.toList());
            return new ResponseEntity<>(getLmsUserResponses, HttpStatus.OK);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(value = "getAllEnrolledBatches")
    public ResponseEntity<?> getAllEnrolledBatches(
        @RequestParam(required = false) Long studentId,
        @RequestParam(required = false) String studentEmail,
        @RequestParam(required = false) Boolean condensed) {

        UUID uuid = UUID.randomUUID();
        MDC.put("requestId", uuid.toString());

        List<Batch> response = null;
        List<BatchResponse> condensedResponse = new ArrayList<>();

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
                response = lmsUserService.getBatchesEnrolledForStudent(studentId);
            } else if (studentEmail != null && !studentEmail.isEmpty()) {
                response = lmsUserService.getBatchesEnrolledForStudent(studentEmail);
            }
            if (condensed != null && condensed == Boolean.TRUE) {
                for (Batch batch : response) {
                    condensedResponse.add(new BatchResponse(batch));
                }
                return new ResponseEntity<>(condensedResponse, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(response, HttpStatus.OK);
            }
        } catch (OperationNotSupportedException | ResourceNotFoundException e) {
            logger.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } finally {
            MDC.remove("requestId");
        }
    }

    @GetMapping(value = "getAllBatchesTaught")
    public ResponseEntity<?> getAllTaughtBatches(
        @RequestParam(required = false) Long instructorId,
        @RequestParam(required = false) String instructorEmail,
        @RequestParam(required = false) Boolean condensed) {

        UUID uuid = UUID.randomUUID();
        MDC.put("requestId", uuid.toString());
        logger.info("Received getAllBatchesTaught request "
            + (instructorId != null ? " instructorId: "
            + instructorId : instructorEmail != null ? "instructorEmail: " + instructorEmail : ""));

        List<Batch> response = null;
        List<BatchResponse> condensedResponse = new ArrayList<>();

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
                response = lmsUserService.getBatchesTaughtByInstructor(instructorId);
            } else if (null != instructorEmail && !instructorEmail.isEmpty()) {
                response = lmsUserService.getBatchesTaughtByInstructor(instructorEmail);
            }
            if (condensed != null && condensed == Boolean.TRUE) {
                for (Batch batch : response) {
                    condensedResponse.add(new BatchResponse(batch));
                }
                return new ResponseEntity<>(condensedResponse, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(response, HttpStatus.OK);
            }
        } catch (OperationNotSupportedException | ResourceNotFoundException e) {
            logger.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } finally {
            MDC.remove("requestId");
        }
    }

    @PostMapping
    public ResponseEntity<?> addNewUser(@Valid @RequestBody LmsUser lmsUser,
                                        @RequestParam(required = false) Boolean condensed) {

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

        if (condensed != null && condensed == Boolean.TRUE) {
            return new ResponseEntity<>(new GetLmsUserResponse(savedUser), HttpStatus.CREATED);
        }
        return new ResponseEntity<LmsUser>(savedUser, HttpStatus.CREATED);
    }

    @PostMapping(value = "addMultiple")
    public ResponseEntity<?> addMultipleUsers(@Valid @RequestBody List<LmsUser> lmsUsers,
                                              @RequestParam(required = false) Boolean condensed) {
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

        if (condensed != null && condensed == Boolean.TRUE) {
            List<GetLmsUserResponse> condensedResponse = savedUsers.stream()
                .map(GetLmsUserResponse::new).collect(Collectors.toList());
            return new ResponseEntity<>(condensedResponse, HttpStatus.CREATED);
        }
        return new ResponseEntity(savedUsers, HttpStatus.CREATED);
    }

    @PutMapping(value = "{id}")
    public ResponseEntity<?> updateUser(@PathVariable long id, @RequestBody LmsUser lmsUser,
                                        @RequestParam(required = false) Boolean condensed) {

        UUID uuid = UUID.randomUUID();
        MDC.put("requestId", uuid.toString());
        logger.info("Received update user request Email: " + id);

        LmsUser currentUser = userUtils.getCurrentUser();
        if (!currentUser.getIsAdmin() && currentUser.getId() != id) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Permission denied");
        }

        try {
            LmsUser response = lmsUserService.updateLmsUser(lmsUser, id);
            if (condensed != null && condensed == Boolean.TRUE) {
                return new ResponseEntity<>(new GetLmsUserResponse(response), HttpStatus.OK);
            }
            return new ResponseEntity<>(response, HttpStatus.OK);
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
