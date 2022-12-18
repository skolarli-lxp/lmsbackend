package com.skolarli.lmsservice.services.impl;

import com.skolarli.lmsservice.exception.OperationNotSupportedException;
import com.skolarli.lmsservice.exception.ResourceNotFoundException;
import com.skolarli.lmsservice.models.NewEnrollmentRequest;
import com.skolarli.lmsservice.models.db.Batch;
import com.skolarli.lmsservice.models.db.Enrollment;
import com.skolarli.lmsservice.models.db.LmsUser;
import com.skolarli.lmsservice.repository.EnrollmentRepository;
import com.skolarli.lmsservice.services.BatchService;
import com.skolarli.lmsservice.services.EnrollmentService;
import com.skolarli.lmsservice.services.LmsUserService;
import com.skolarli.lmsservice.utils.UserUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EnrollmentServiceImpl implements EnrollmentService {
    Logger logger = LoggerFactory.getLogger(EnrollmentServiceImpl.class);

    private final EnrollmentRepository enrollmentRepository;
    private final UserUtils userUtils;
    private final BatchService batchService;
    private final LmsUserService lmsUserService;

    public EnrollmentServiceImpl(EnrollmentRepository enrollmentRepository, UserUtils userUtils,
                                    BatchService batchService, LmsUserService lmsUserService) {
        this.enrollmentRepository = enrollmentRepository;
        this.userUtils = userUtils;
        this.batchService = batchService;
        this.lmsUserService = lmsUserService;
    }

    private Boolean checkPermissions (Enrollment existingEnrollment) {
        LmsUser currentUser = userUtils.getCurrentUser();
        if (currentUser.getIsAdmin() != true && currentUser != existingEnrollment.getBatch().getCourse().getCourseOwner()) {
            return false;
        }
        return true;
    }

    @Override
    public Enrollment toEnrollment(NewEnrollmentRequest newEnrollmentRequest) {
        Batch batch = batchService.getBatch(newEnrollmentRequest.getBatchId());
        LmsUser student = lmsUserService.getLmsUserById(newEnrollmentRequest.getStudentId());

        Enrollment enrollment = new Enrollment();
        enrollment.setBatch(batch);
        enrollment.setStudent(student);
        enrollment.setEnrollmentIsDeleted(false);
        
        return enrollment;
    }

    @Override
    public List<Enrollment> getAllEnrollments() {
        return enrollmentRepository.findAll();
    }

    @Override
    public Enrollment getEnrollmentById(long id) {
        return enrollmentRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Enrollment", "Id", id));
    }


    @Override
    public Enrollment save(Enrollment enrollment) {
        return enrollmentRepository.save(enrollment);
    }

    @Override
    public Enrollment updateEnrollment(Enrollment enrollment, long id) {
        Enrollment existingEnrollment = enrollmentRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Enrollment", "Id", id));
        if (checkPermissions(existingEnrollment) == false) {
            throw new OperationNotSupportedException("User does not have permission to update this enrollment");
        }
        if (enrollment.getEnrollmentIsDeleted() != null) {
            logger.error("Cannot change deleted status. Use Delete API instead");
            existingEnrollment.setEnrollmentIsDeleted(null);
        }
        existingEnrollment.update(enrollment);
        enrollmentRepository.save(existingEnrollment);
        return existingEnrollment;
    }

    @Override
    public void deleteEnrollment(long id) {
        Enrollment existingEnrollment = enrollmentRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Enrollment", "Id", id));
        if (checkPermissions(existingEnrollment) == false) {
            throw new OperationNotSupportedException("User does not have permission to perform Delete operation");
        }
        existingEnrollment.setEnrollmentIsDeleted(true);
        enrollmentRepository.save(existingEnrollment);
    }


    @Override
    public void hardDeleteEnrollment(long id) {
        Enrollment existingEnrollment = enrollmentRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Enrollment", "Id", id));
        if (checkPermissions(existingEnrollment) == false) {
            throw new OperationNotSupportedException("User does not have permission to perform Delete operation");
        }
        enrollmentRepository.delete(existingEnrollment);
    }
}
