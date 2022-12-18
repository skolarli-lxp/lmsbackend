package com.skolarli.lmsservice.services;

import com.skolarli.lmsservice.models.NewEnrollmentRequest;
import com.skolarli.lmsservice.models.db.Enrollment;

import java.util.List;

public interface EnrollmentService {
        Enrollment toEnrollment(NewEnrollmentRequest request);

        List<Enrollment> getAllEnrollments();
        Enrollment getEnrollmentById(long id);

        Enrollment save(Enrollment enrollment);
        
        Enrollment updateEnrollment(Enrollment enrollment, long id);

        void deleteEnrollment(long id);
        void hardDeleteEnrollment(long id);
}
