package com.skolarli.lmsservice.controller;

import com.skolarli.lmsservice.exception.OperationNotSupportedException;
import com.skolarli.lmsservice.models.db.course.Enrollment;
import com.skolarli.lmsservice.models.dto.course.NewEnrollmentRequest;
import com.skolarli.lmsservice.models.dto.course.NewEnrollmentsForBatchRequest;
import com.skolarli.lmsservice.services.EnrollmentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;
import javax.validation.Valid;


@RestController
@RequestMapping("/enrollment")
public class EnrollmentController {
    final Logger logger = LoggerFactory.getLogger(EnrollmentController.class);
    private final EnrollmentService enrollmentService;

    public EnrollmentController(EnrollmentService enrollmentService) {
        this.enrollmentService = enrollmentService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<Enrollment>> getAllEnrollments(@RequestParam(required = false)
                                                              Long batchId) {

        UUID uuid = UUID.randomUUID();
        MDC.put("requestId", uuid.toString());
        logger.info("Received request for getAllEnrollments" + (batchId != null
                ? " for batchId: " + batchId
                : ""));

        if (batchId != null) {
            try {
                return new ResponseEntity<>(enrollmentService.getEnrollmentsByBatchId(batchId),
                        HttpStatus.OK);
            } catch (Exception e) {
                logger.error("Error in getAllBatchSchedules: " + e.getMessage());
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
            }
        }
        try {
            return new ResponseEntity<>(enrollmentService.getAllEnrollments(), HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error in getAllEnrollments: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        } finally {
            MDC.remove("requestId");
        }
    }

    @RequestMapping(method = RequestMethod.GET, value = "{id}")
    public ResponseEntity<Enrollment> getEnrollment(@PathVariable long id) {

        UUID uuid = UUID.randomUUID();
        MDC.put("requestId", uuid.toString());
        logger.info("Received request for getEnrollment with id: " + id);

        try {
            return new ResponseEntity<>(enrollmentService.getEnrollmentById(id), HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error in getEnrollment: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        } finally {
            MDC.remove("requestId");
        }
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Enrollment> addEnrollment(@Valid @RequestBody
                                                    NewEnrollmentRequest request) {

        UUID uuid = UUID.randomUUID();
        MDC.put("requestId", uuid.toString());
        logger.info("Received request for addEnrollment for batch: " + request.getBatchId());

        Enrollment enrollment = enrollmentService.toEnrollment(request);
        try {
            return new ResponseEntity<>(enrollmentService.save(enrollment), HttpStatus.OK);
        } catch (DataIntegrityViolationException e) {
            logger.error("Error in addEnrollment: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Enrollment already exists");
        } catch (Exception e) {
            logger.error("Error in addEnrollment: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        } finally {
            MDC.remove("requestId");
        }
    }

    @RequestMapping(method = RequestMethod.POST, value = "/forbatch")
    public ResponseEntity<List<Enrollment>> addEnrollmentsForBatch(
            @Valid @RequestBody List<NewEnrollmentsForBatchRequest> request,
            @RequestParam Long batchId) {

        UUID uuid = UUID.randomUUID();
        MDC.put("requestId", uuid.toString());
        logger.info("Received request for addEnrollmentsForBatch for batch: " + batchId);

        List<Enrollment> enrollments = enrollmentService.toEnrollmentList(request);
        try {
            return new ResponseEntity<>(enrollmentService.saveAllEnrollments(enrollments, batchId),
                    HttpStatus.OK);
        } catch (DataIntegrityViolationException e) {
            logger.error("Error in addEnrollment: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Enrollment already exists");
        } catch (Exception e) {
            logger.error("Error in addEnrollment: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        } finally {
            MDC.remove("requestId");
        }
    }

    @RequestMapping(method = RequestMethod.PUT, value = "{id}")
    public ResponseEntity<Enrollment> updateEnrollment(
            @PathVariable long id,
            @Valid @RequestBody NewEnrollmentRequest request) {

        UUID uuid = UUID.randomUUID();
        MDC.put("requestId", uuid.toString());
        logger.info("Received request for updateEnrollment with id: " + id);

        Enrollment newEnrollment = enrollmentService.toEnrollment(request);
        Enrollment existingEnrollment = enrollmentService.getEnrollmentById(id);
        existingEnrollment.update(newEnrollment);
        try {
            return new ResponseEntity<>(enrollmentService.updateEnrollment(newEnrollment, id),
                    HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error in updateEnrollment: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        } finally {
            MDC.remove("requestId");
        }
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "{id}")
    public ResponseEntity<String> hardDeleteEnrollment(@PathVariable long id) {

        UUID uuid = UUID.randomUUID();
        MDC.put("requestId", uuid.toString());
        logger.info("Received request for hardDeleteEnrollment with id: " + id);

        try {
            enrollmentService.hardDeleteEnrollment(id);
            return new ResponseEntity<>("Enrollment Deleted!", HttpStatus.OK);
        } catch (OperationNotSupportedException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        } catch (Exception e) {
            logger.error("Error in deleteEnrollment: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        } finally {
            MDC.remove("requestId");
        }
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "soft/{id}")
    public ResponseEntity<String> deleteEnrollment(@PathVariable long id) {

        UUID uuid = UUID.randomUUID();
        MDC.put("requestId", uuid.toString());
        logger.info("Received request for deleteEnrollment with id: " + id);

        try {
            enrollmentService.softDeleteEnrollment(id);
            return new ResponseEntity<>("Enrollment Deleted!", HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error in deleteEnrollment: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        } finally {
            MDC.remove("requestId");
        }
    }
}
