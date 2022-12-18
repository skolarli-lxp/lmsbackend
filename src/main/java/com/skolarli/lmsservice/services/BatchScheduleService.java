package com.skolarli.lmsservice.services;

import com.skolarli.lmsservice.models.db.BatchSchedule;

import java.util.List;

public interface BatchScheduleService {
    //READ
    BatchSchedule getBatchSchedule(long id);
    List<BatchSchedule> getAllBatchSchedules();
    List<BatchSchedule> getSchedulesForBatch(long batchId);

    //CREATE
    BatchSchedule saveBatchSchedule(BatchSchedule batchSchedule);
    //UPDATE
    BatchSchedule updateBatchSchedule(BatchSchedule batchSchedule, long id);
    
    //DELETE
    void deleteBatchSchedule(long id);
    void hardDeleteBatchSchedule(long id);
}
