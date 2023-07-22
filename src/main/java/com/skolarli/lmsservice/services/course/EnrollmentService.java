package com.skolarli.lmsservice.services.course;

import com.skolarli.lmsservice.models.db.course.Enrollment;
import com.skolarli.lmsservice.models.dto.course.NewEnrollmentRequest;
import com.skolarli.lmsservice.models.dto.course.NewEnrollmentsForBatchRequest;

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
