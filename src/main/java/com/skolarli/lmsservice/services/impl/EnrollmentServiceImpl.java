package com.skolarli.lmsservice.services.impl;

import com.skolarli.lmsservice.exception.ResourceNotFoundException;
import com.skolarli.lmsservice.models.db.Enrollment;
import com.skolarli.lmsservice.repository.EnrollmentRepository;
import com.skolarli.lmsservice.services.EnrollmentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EnrollmentServiceImpl implements EnrollmentService {
    Logger logger = LoggerFactory.getLogger(EnrollmentServiceImpl.class);

    private final EnrollmentRepository enrollmentRepository;

    public EnrollmentServiceImpl(EnrollmentRepository enrollmentRepository) {
        this.enrollmentRepository = enrollmentRepository;
    }


    @Override
    public Enrollment save(Enrollment enrollment) {
        return enrollmentRepository.save(enrollment);
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
    public Enrollment updateEnrollment(Enrollment enrollment, long id) {
        Enrollment existingEnrollment = enrollmentRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Enrollment", "Id", id));
        // Update existing Enrollment
        existingEnrollment.update(enrollment);
        enrollmentRepository.save(existingEnrollment);
        return existingEnrollment;
    }

    @Override
    public void deleteEnrollment(long id) {
        enrollmentRepository.deleteById(id);
    }
}
