package com.skolarli.lmsservice.services.impl;

import com.skolarli.lmsservice.exception.OperationNotSupportedException;
import com.skolarli.lmsservice.exception.ResourceNotFoundException;
import com.skolarli.lmsservice.models.db.Batch;
import com.skolarli.lmsservice.models.db.LmsUser;
import com.skolarli.lmsservice.repository.BatchRepository;
import com.skolarli.lmsservice.services.BatchService;
import com.skolarli.lmsservice.utils.UserUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BatchServiceImpl implements BatchService {

    Logger logger = LoggerFactory.getLogger(BatchServiceImpl.class);
    private final BatchRepository batchRepository;
    private final UserUtils userUtils;

    public BatchServiceImpl(BatchRepository batchRepository, UserUtils userUtils) {
        this.batchRepository = batchRepository;
        this.userUtils = userUtils;
    }

    @Override
    public Batch saveBatch(Batch batch) {
        return batchRepository.save(batch);
    }

    @Override
    public Batch updateBatch(Batch newBatch, long id) {
        Batch existingBatch = batchRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Batch", "Id", id));
        LmsUser currentUser = userUtils.getCurrentUser();
        if (currentUser.getIsAdmin() != true && currentUser != existingBatch.getCourse().getCourseOwner()) {
            throw new OperationNotSupportedException("User does not have permission to perform Update operation");
        }
        if (newBatch.getBatchIsDeleted() != null) {
            logger.error("Cannot change deleted status. Use Delete API");
            existingBatch.setBatchIsDeleted(null);
        }
        existingBatch.update(newBatch);        
        return batchRepository.save(existingBatch);      
    }

    @Override
    public List<Batch> getAllBatches() {
        return batchRepository.findAll();
    }

    @Override
    public Batch getBatch(long id) {
        Batch existingBatch = batchRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Batch", "Id", id));
        return existingBatch;
    }

    @Override
    public List<Batch> getBatchesForCourse(long courseId) {
        List<Batch> batches = batchRepository.findByCourse_Id(courseId);
        return batches;
    }

    private Boolean checkPermissions (Batch existingBatch) {
        LmsUser currentUser = userUtils.getCurrentUser();
        if (currentUser.getIsAdmin() != true && currentUser != existingBatch.getCourse().getCourseOwner()) {
            return false;
        }
        return true;
    }

    @Override
    public void deleteBatch(long id) {
        Batch existingBatch = batchRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Batch", "Id", id));
        if (checkPermissions(existingBatch) == false) {
            throw new OperationNotSupportedException("User does not gave permissions to perform delete operation");
        }
        existingBatch.setBatchIsDeleted(true);
        batchRepository.save(existingBatch);
    }

    @Override
    public void hardDeleteBatch(long id) {
        Batch existingBatch = batchRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Batch", "Id", id));
        if (checkPermissions(existingBatch) == false) {
            throw new OperationNotSupportedException("User does not gave permissions to perform delete operation");
        }
        batchRepository.delete(existingBatch);
    }
}
