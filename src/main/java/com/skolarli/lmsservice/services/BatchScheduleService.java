package com.skolarli.lmsservice.services;

import com.skolarli.lmsservice.models.db.BatchSchedule;
import com.skolarli.lmsservice.models.dto.NewBatchScheduleRequest;
import com.skolarli.lmsservice.models.dto.NewBatchSchedulesForBatchRequest;

import java.util.List;

public interface BatchScheduleService {

    //READ
    BatchSchedule getBatchSchedule(long id);

    List<BatchSchedule> getAllBatchSchedules();

    List<BatchSchedule> getSchedulesForBatch(long batchId);

    //CREATE
    BatchSchedule saveBatchSchedule(BatchSchedule batchSchedule);

    List<BatchSchedule> saveAllBatchSchedules(List<BatchSchedule> batchSchedules, Long batchId);

    //UPDATE
    BatchSchedule updateBatchSchedule(BatchSchedule batchSchedule, long id);

    //DELETE
    void deleteBatchSchedule(long id);

    void hardDeleteBatchSchedule(long id);

    BatchSchedule toBatchSchedule(
            NewBatchSchedulesForBatchRequest newBatchSchedulesForBatchRequest);

    BatchSchedule toBatchSchedule(NewBatchScheduleRequest newBatchScheduleRequest);

    List<BatchSchedule> toBatchScheduleList(
            List<NewBatchSchedulesForBatchRequest> newBatchSchedulesForBatchRequests);
}
