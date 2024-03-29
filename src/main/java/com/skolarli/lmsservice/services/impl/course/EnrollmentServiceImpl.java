package com.skolarli.lmsservice.services.impl.course;

import com.skolarli.lmsservice.exception.OperationNotSupportedException;
import com.skolarli.lmsservice.exception.ResourceNotFoundException;
import com.skolarli.lmsservice.models.db.core.LmsUser;
import com.skolarli.lmsservice.models.db.course.Batch;
import com.skolarli.lmsservice.models.db.course.Enrollment;
import com.skolarli.lmsservice.models.dto.course.NewEnrollmentRequest;
import com.skolarli.lmsservice.models.dto.course.NewEnrollmentsForBatchRequest;
import com.skolarli.lmsservice.repository.course.EnrollmentRepository;
import com.skolarli.lmsservice.services.core.LmsUserService;
import com.skolarli.lmsservice.services.course.BatchService;
import com.skolarli.lmsservice.services.course.EnrollmentService;
import com.skolarli.lmsservice.utils.UserUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class EnrollmentServiceImpl implements EnrollmentService {
    final Logger logger = LoggerFactory.getLogger(EnrollmentServiceImpl.class);

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

    private Boolean checkPermissions(Enrollment existingEnrollment) {
        LmsUser currentUser = userUtils.getCurrentUser();
        return currentUser.getIsAdmin()
                || currentUser == existingEnrollment.getBatch().getCourse().getCourseOwner();
    }

    @Override
    public Enrollment toEnrollment(NewEnrollmentRequest newEnrollmentRequest) {
        Batch batch = batchService.getBatch(newEnrollmentRequest.getBatchId());
        LmsUser student = lmsUserService.getLmsUserById(newEnrollmentRequest.getStudentId());
        if (!student.getIsStudent()) {
            throw new OperationNotSupportedException("User is not a student");
        }

        Enrollment enrollment = new Enrollment();
        enrollment.setBatch(batch);
        enrollment.setStudent(student);
        enrollment.setEnrollmentIsDeleted(false);

        return enrollment;
    }

    @Override
    public Enrollment toEnrollment(NewEnrollmentsForBatchRequest request) {
        LmsUser student = lmsUserService.getLmsUserById(request.getStudentId());
        if (!student.getIsStudent()) {
            throw new OperationNotSupportedException("User is not a student");
        }
        Enrollment enrollment = new Enrollment();
        enrollment.setStudent(student);
        enrollment.setEnrollmentIsDeleted(false);
        return enrollment;
    }

    @Override
    public List<Enrollment> toEnrollmentList(List<NewEnrollmentsForBatchRequest> request) {
        List<Enrollment> enrollments = new ArrayList<>();
        for (NewEnrollmentsForBatchRequest newEnrollmentRequest : request) {
            enrollments.add(toEnrollment(newEnrollmentRequest));
        }
        // Some more fun ways to do the same  thing
        //request.stream().forEach(newEnrollmentRequest ->
        //              enrollments.add(toEnrollment(newEnrollmentRequest)));
        //return request.stream().map(this::toEnrollment).collect(Collectors.toList());
        return enrollments;
    }

    @Override
    public List<Enrollment> getAllEnrollments() {
        return enrollmentRepository.findAll();
    }

    @Override
    public Enrollment getEnrollmentById(long id) {
        List<Enrollment> existingEnrollment = enrollmentRepository.findAllById(
                new ArrayList<>(List.of(id)));
        if (existingEnrollment.size() == 0) {
            throw new ResourceNotFoundException("Enrollment", "Id", id);
        }
        return existingEnrollment.get(0);
    }

    @Override
    public List<Enrollment> getEnrollmentsByBatchId(long batchId) {
        return enrollmentRepository.findByBatchId(batchId);
    }


    @Override
    public Enrollment save(Enrollment enrollment) {
        try {
            return enrollmentRepository.save(enrollment);
        } catch (DataIntegrityViolationException e) {
            logger.error("Enrollment already exists");
            return enrollmentRepository.findByStudent_IdAndBatch_Id(enrollment.getStudent().getId(),
                    enrollment.getBatch().getId());
        }
    }

    @Override
    public List<Enrollment> saveAllEnrollments(List<Enrollment> enrollments, Long batchId) {
        Batch batch = batchService.getBatch(batchId);
        List<Enrollment> result = new ArrayList<>();
        for (Enrollment enrollment : enrollments) {
            enrollment.setBatch(batch);
            enrollment.setEnrollmentIsDeleted(false);
        }
        for (Enrollment enrollment : enrollments) {
            try {
                Enrollment saved = enrollmentRepository.save(enrollment);
                result.add(saved);
            } catch (DataIntegrityViolationException e) {
                logger.error("Enrollment already exists");
                result.add(enrollmentRepository.findByStudent_IdAndBatch_Id(enrollment.getStudent().getId(),
                        enrollment.getBatch().getId()));
            }
        }
        return result;
    }

    @Override
    public Enrollment updateEnrollment(Enrollment enrollment, long id) {
        Enrollment existingEnrollment = enrollmentRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Enrollment", "Id", id));
        if (!checkPermissions(existingEnrollment)) {
            throw new OperationNotSupportedException("User does not have permission to update "
                    + "this enrollment");
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
    public void softDeleteEnrollment(long id) {
        Enrollment existingEnrollment = enrollmentRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Enrollment", "Id", id));
        if (!checkPermissions(existingEnrollment)) {
            throw new OperationNotSupportedException("User does not have permission to perform "
                    + "Delete operation");
        }
        existingEnrollment.setEnrollmentIsDeleted(true);
        enrollmentRepository.save(existingEnrollment);
    }


    @Override
    public void hardDeleteEnrollment(long id) {
        Enrollment existingEnrollment = enrollmentRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Enrollment", "Id", id));
        if (!checkPermissions(existingEnrollment)) {
            throw new OperationNotSupportedException("User does not have permission to perform"
                    + " Delete operation");
        }
        enrollmentRepository.delete(existingEnrollment);
    }
}
