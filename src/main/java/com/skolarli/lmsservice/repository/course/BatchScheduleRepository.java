package com.skolarli.lmsservice.repository.course;

import com.skolarli.lmsservice.models.db.course.BatchSchedule;
import com.skolarli.lmsservice.repository.core.TenantableRepository;

import java.time.Instant;
import java.util.List;

public interface BatchScheduleRepository extends TenantableRepository<BatchSchedule> {
    List<BatchSchedule> findByBatch_Id(Long batchId);

    List<BatchSchedule> findAllByStartDateTimeBetweenAndBatch_Id(Instant queryStartDate,
                                                                 Instant queryEndDate,
                                                                 Long batchId);

    List<BatchSchedule> findAllByStartDateTimeBetween(Instant queryStartDate,
                                                      Instant queryEndDate);

    List<BatchSchedule> findAllByStartDateTimeAfterAndBatch_Id(Instant queryStartDate,
                                                               Long batchId);

    List<BatchSchedule> findAllByStartDateTimeAfter(Instant queryStartDate);

    List<BatchSchedule> findAllByStartDateTimeBeforeAndBatch_Id(Instant queryEndDate, Long batchId);

    List<BatchSchedule> findAllByStartDateTimeBefore(Instant queryEndDate);
}
