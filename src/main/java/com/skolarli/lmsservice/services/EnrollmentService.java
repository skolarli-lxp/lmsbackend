package com.skolarli.lmsservice.services;

import com.skolarli.lmsservice.models.db.Enrollment;
import com.skolarli.lmsservice.models.dto.NewEnrollmentRequest;
import com.skolarli.lmsservice.models.dto.NewEnrollmentsForBatchRequest;

import java.util.List;

public interface EnrollmentService {
    Enrollment toEnrollment(NewEnrollmentRequest request);

    Enrollment toEnrollment(NewEnrollmentsForBatchRequest request);

    List<Enrollment> toEnrollmentList(List<NewEnrollmentsForBatchRequest> request);

    List<Enrollment> getAllEnrollments();

    List<Enrollment> getEnrollmentsByBatchId(long batchId);

    Enrollment getEnrollmentById(long id);


    Enrollment save(Enrollment enrollment);

    List<Enrollment> saveAllEnrollments(List<Enrollment> enrollments, Long batchId);

    Enrollment updateEnrollment(Enrollment enrollment, long id);

    void softDeleteEnrollment(long id);

    void hardDeleteEnrollment(long id);
}
