package com.skolarli.lmsservice.services;

import com.skolarli.lmsservice.models.db.BatchSchedule;

import java.util.List;

public interface BatchScheduleService {
    BatchSchedule saveBatchSchedule(BatchSchedule batchSchedule);
    BatchSchedule updateBatchSchedule(BatchSchedule batchSchedule, long id);
    BatchSchedule getBatchSchedule(long id);
    List<BatchSchedule> getAllBatchSchedules();
    List<BatchSchedule> getSchedulesForBatch(long batchId);
    void deleteBatchSchedule(long id);
}
