package com.skolarli.lmsservice.repository;

import com.skolarli.lmsservice.models.db.BatchSchedule;

import java.time.Instant;
import java.util.List;

public interface BatchScheduleRepository extends TenantableRepository<BatchSchedule> {
    List<BatchSchedule> findByBatch_Id(long batchId);

    List<BatchSchedule> findAllByStartDateTimeBetweenAndBatch_Id(Instant queryStartDate,
                                                                 Instant queryEndDate,
                                                                 Long batchId);

    List<BatchSchedule> findAllByStartDateTimeAfterAndBatch_Id(Instant queryStartDate,
                                                               Long batchId);

    List<BatchSchedule> findAllByStartDateTimeBeforeAndBatch_Id(Instant queryEndDate, Long batchId);
}
