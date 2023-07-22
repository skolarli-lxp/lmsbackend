package com.skolarli.lmsservice.repository.course;

import com.skolarli.lmsservice.models.db.course.Batch;
import com.skolarli.lmsservice.repository.core.TenantableRepository;

import java.util.List;

public interface BatchRepository extends TenantableRepository<Batch> {
    List<Batch> findByCourse_Id(long courseId);
}
