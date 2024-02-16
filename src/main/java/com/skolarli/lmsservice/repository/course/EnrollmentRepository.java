package com.skolarli.lmsservice.repository.course;

import com.skolarli.lmsservice.models.db.course.Enrollment;
import com.skolarli.lmsservice.repository.core.TenantableRepository;

import java.util.List;

public interface EnrollmentRepository extends TenantableRepository<Enrollment> {
    List<Enrollment> findByBatchId(long batchId);

    Enrollment findByStudent_IdAndBatch_Id(Long studentId, Long batchId);
}
