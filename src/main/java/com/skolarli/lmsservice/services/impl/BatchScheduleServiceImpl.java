package com.skolarli.lmsservice.services.impl;

import com.skolarli.lmsservice.exception.ResourceNotFoundException;
import com.skolarli.lmsservice.models.db.Batch;
import com.skolarli.lmsservice.models.db.BatchSchedule;
import com.skolarli.lmsservice.repository.BatchScheduleRepository;
import com.skolarli.lmsservice.services.BatchScheduleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class BatchScheduleServiceImpl implements BatchScheduleService {
    Logger logger = LoggerFactory.getLogger(BatchScheduleServiceImpl.class);

    final BatchScheduleRepository batchScheduleRepository;

    public BatchScheduleServiceImpl(BatchScheduleRepository batchScheduleRepository) {
        this.batchScheduleRepository = batchScheduleRepository;
    }

    @Override
    public BatchSchedule saveBatchSchedule(BatchSchedule batchSchedule) {
        return batchScheduleRepository.save(batchSchedule);
    }

    @Override
    public BatchSchedule updateBatchSchedule(BatchSchedule batchSchedule) {
        return null;
    }

    @Override
    public BatchSchedule getBatchSchedule(long id) {
        BatchSchedule existingBatchSchedule = batchScheduleRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("BatchSchedule", "Id", id));
        return existingBatchSchedule;
    }

    @Override
    public List<BatchSchedule> getAllBatchSchedules() {
        return batchScheduleRepository.findAll();
    }

    @Override
    public List<BatchSchedule> getSchedulesForBatch(long batchId) {
        List<BatchSchedule> batchSchedules = batchScheduleRepository.findByBatch_Id(batchId);
        return batchSchedules;
    }

    @Override
    public void deleteBatchSchedule(long id) {
        BatchSchedule existingBatchSchedule = batchScheduleRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("BatchSchedule", "Id", id));
        batchScheduleRepository.delete(existingBatchSchedule);
    }
}
