package com.skolarli.lmsservice.repository;

import com.skolarli.lmsservice.models.db.course.Enrollment;

import java.util.List;

public interface EnrollmentRepository extends TenantableRepository<Enrollment> {
    List<Enrollment> findByBatchId(long batchId);
}
