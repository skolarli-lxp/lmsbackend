package com.skolarli.lmsservice.services.course;

import com.skolarli.lmsservice.models.db.course.BatchSchedule;
import com.skolarli.lmsservice.models.dto.course.NewBatchScheduleRequest;
import com.skolarli.lmsservice.models.dto.course.NewBatchSchedulesForBatchRequest;

import java.time.Instant;
import java.util.List;

public interface BatchScheduleService {

    //READ
    BatchSchedule getBatchSchedule(long id);

    List<BatchSchedule> getAllBatchSchedules();

    List<BatchSchedule> getSchedulesWithCriteria(Long batchId,
                                                 Instant queryStartDate, Instant queryEndDate);

    Long getBatchScheduleCount();

    //CREATE
    BatchSchedule saveBatchSchedule(BatchSchedule batchSchedule);

    List<BatchSchedule> saveAllBatchSchedules(List<BatchSchedule> batchSchedules, Long batchId);

    //UPDATE
    BatchSchedule updateBatchSchedule(BatchSchedule batchSchedule, long id);

    //DELETE
    void softDeleteBatchSchedule(long id);

    void hardDeleteBatchSchedule(long id);

    BatchSchedule toBatchSchedule(
            NewBatchSchedulesForBatchRequest newBatchSchedulesForBatchRequest);

    BatchSchedule toBatchSchedule(NewBatchScheduleRequest newBatchScheduleRequest);

    List<BatchSchedule> toBatchScheduleList(
            List<NewBatchSchedulesForBatchRequest> newBatchSchedulesForBatchRequests);
}
