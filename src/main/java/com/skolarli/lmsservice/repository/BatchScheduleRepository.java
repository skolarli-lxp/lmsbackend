package com.skolarli.lmsservice.repository;

import com.skolarli.lmsservice.models.db.BatchSchedule;

import java.util.Date;
import java.util.List;

public interface BatchScheduleRepository extends TenantableRepository<BatchSchedule> {
    List<BatchSchedule> findByBatch_Id(long batchId);

    List<BatchSchedule> findAllByStartDateTimeBetweenAndBatch_Id(Date queryStartDate,
                                                                 Date queryEndDate, Long batchId);

    List<BatchSchedule> findAllByStartDateTimeAfterAndBatch_Id(Date queryStartDate, Long batchId);

    List<BatchSchedule> findAllByStartDateTimeBeforeAndBatch_Id(Date queryEndDate, Long batchId);
}
