package com.skolarli.lmsservice.repository;

import com.skolarli.lmsservice.models.db.course.Batch;

import java.util.List;

public interface BatchRepository extends TenantableRepository<Batch> {
    List<Batch> findByCourse_Id(long courseId);
}
