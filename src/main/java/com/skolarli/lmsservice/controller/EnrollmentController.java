package com.skolarli.lmsservice.controller;

import com.skolarli.lmsservice.models.NewEnrollmentRequest;
import com.skolarli.lmsservice.models.db.Enrollment;
import com.skolarli.lmsservice.services.EnrollmentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/enrollment")
public class EnrollmentController {
    Logger logger = LoggerFactory.getLogger(EnrollmentController.class);

    private final EnrollmentService enrollmentService;

    public EnrollmentController(EnrollmentService enrollmentService) {
        this.enrollmentService = enrollmentService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<Enrollment>> getAllEnrollments() {
        try {
            return new ResponseEntity<>(enrollmentService.getAllEnrollments(), HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error in getAllEnrollments: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @RequestMapping(method = RequestMethod.GET, value = "{id}")
    public ResponseEntity<Enrollment> getEnrollment(@PathVariable long id) {
        try {
            return new ResponseEntity<>(enrollmentService.getEnrollmentById(id), HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error in getEnrollment: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Enrollment> addEnrollment(@Valid @RequestBody NewEnrollmentRequest request) {
        Enrollment enrollment = request.toEnrollment();
        try {
            return new ResponseEntity<>(enrollmentService.save(enrollment), HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error in addEnrollment: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @RequestMapping(method = RequestMethod.PUT, value = "{id}")
    public ResponseEntity<Enrollment> updateEnrollment(@PathVariable long id, @Valid @RequestBody NewEnrollmentRequest request) {
        Enrollment newEnrollment = request.toEnrollment();
        Enrollment existingEnrollment = enrollmentService.getEnrollmentById(id);
        existingEnrollment.update(newEnrollment);
        try {
            return new ResponseEntity<>(enrollmentService.updateEnrollment(newEnrollment, id), HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error in updateEnrollment: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "{id}")
    public ResponseEntity<String> deleteEnrollment(@PathVariable long id) {
        try {
            enrollmentService.deleteEnrollment(id);
            return new ResponseEntity<>("Enrollment Deleted!", HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error in deleteEnrollment: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
