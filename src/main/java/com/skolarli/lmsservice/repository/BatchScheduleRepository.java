package com.skolarli.lmsservice.repository;

import com.skolarli.lmsservice.models.db.BatchSchedule;

import java.util.List;

public interface BatchScheduleRepository extends TenantableRepository<BatchSchedule> {
    List<BatchSchedule>  findByBatch_Id(long batchId);
}
