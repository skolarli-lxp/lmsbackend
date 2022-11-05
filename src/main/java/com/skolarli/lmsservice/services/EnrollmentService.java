package com.skolarli.lmsservice.services;

import com.skolarli.lmsservice.models.db.Enrollment;

import java.util.List;

public interface EnrollmentService {
        Enrollment save(Enrollment enrollment);
        List<Enrollment> getAllEnrollments();
        Enrollment getEnrollmentById(long id);
        Enrollment updateEnrollment(Enrollment enrollment, long id);
        void deleteEnrollment(long id);
        void hardDeleteEnrollment(long id);

}
