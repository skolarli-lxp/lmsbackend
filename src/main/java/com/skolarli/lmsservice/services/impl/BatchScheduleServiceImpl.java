package com.skolarli.lmsservice.services.impl;

import com.skolarli.lmsservice.exception.OperationNotSupportedException;
import com.skolarli.lmsservice.exception.ResourceNotFoundException;
import com.skolarli.lmsservice.models.db.BatchSchedule;
import com.skolarli.lmsservice.models.db.LmsUser;
import com.skolarli.lmsservice.repository.BatchScheduleRepository;
import com.skolarli.lmsservice.services.BatchScheduleService;
import com.skolarli.lmsservice.utils.UserUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class BatchScheduleServiceImpl implements BatchScheduleService {
    Logger logger = LoggerFactory.getLogger(BatchScheduleServiceImpl.class);

    final BatchScheduleRepository batchScheduleRepository;
    final UserUtils userUtils;

    public BatchScheduleServiceImpl(BatchScheduleRepository batchScheduleRepository, UserUtils userUtils) {
        this.batchScheduleRepository = batchScheduleRepository;
        this.userUtils = userUtils;
    }

    private Boolean checkPermissions (BatchSchedule existingBatchSchedule) {
        LmsUser currentUser = userUtils.getCurrentUser();
        if (currentUser.getIsAdmin() != true && currentUser != existingBatchSchedule.getBatch().getCourse().getCourseOwner()) {
            return false;
        }
        return true;
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
    public BatchSchedule saveBatchSchedule(BatchSchedule batchSchedule)  {
        if (checkPermissions(batchSchedule) == false) {
            throw new OperationNotSupportedException("Operation not supported");
        }
        return batchScheduleRepository.save(batchSchedule);
    }

    @Override
    public BatchSchedule updateBatchSchedule(BatchSchedule batchSchedule, long id) {
        BatchSchedule existingBatchSchedule = batchScheduleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("BatchSchedule", "Id", id));
        LmsUser currentUser = userUtils.getCurrentUser();
        if (currentUser.getIsAdmin() != true && currentUser != existingBatchSchedule.getBatch().getCourse().getCourseOwner()) {
            throw new OperationNotSupportedException("Operation not supported: Permission Denied");
        }
        if (batchSchedule.getBatchScheduleIsDeleted() != null) {
            logger.error("Cannot change deleted status. Use Delete API");
            existingBatchSchedule.setBatchScheduleIsDeleted(null);
        }
        existingBatchSchedule.update(batchSchedule);
        return batchScheduleRepository.save(existingBatchSchedule);
    }
    

    @Override
    public void deleteBatchSchedule(long id) {
        BatchSchedule existingBatchSchedule = batchScheduleRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("BatchSchedule", "Id", id));
        if (checkPermissions(existingBatchSchedule) == false) {
            throw new OperationNotSupportedException("Operation not supported: Permission Denied");
        }
        existingBatchSchedule.setBatchScheduleIsDeleted(true);
        batchScheduleRepository.save(existingBatchSchedule);
    }

    @Override
    public void hardDeleteBatchSchedule(long id) {
        BatchSchedule existingBatchSchedule = batchScheduleRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("BatchSchedule", "Id", id));
        if (checkPermissions(existingBatchSchedule) == false) {
            throw new OperationNotSupportedException("Operation not supported: Permission Denied");
        }
        batchScheduleRepository.delete(existingBatchSchedule);
        
    }
}
