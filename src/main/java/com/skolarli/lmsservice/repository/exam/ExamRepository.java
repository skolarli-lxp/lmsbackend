package com.skolarli.lmsservice.repository.exam;

import com.skolarli.lmsservice.models.db.exam.Exam;
import com.skolarli.lmsservice.repository.core.TenantableRepository;

import java.util.List;

public interface ExamRepository extends TenantableRepository<Exam> {
    List<Exam> findAllByCourse_Id(Long courseId);

    List<Exam> findAllByBatch_Id(Long batchId);
}
