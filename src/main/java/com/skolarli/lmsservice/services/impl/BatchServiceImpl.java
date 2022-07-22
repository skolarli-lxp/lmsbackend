package com.skolarli.lmsservice.services.impl;

import com.skolarli.lmsservice.exception.ResourceNotFoundException;
import com.skolarli.lmsservice.models.db.Batch;
import com.skolarli.lmsservice.repository.BatchRepository;
import com.skolarli.lmsservice.services.BatchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BatchServiceImpl implements BatchService {

    Logger logger = LoggerFactory.getLogger(BatchServiceImpl.class);
    private final BatchRepository batchRepository;

    public BatchServiceImpl(BatchRepository batchRepository) {
        this.batchRepository = batchRepository;
    }

    @Override
    public Batch saveBatch(Batch batch) {
        return batchRepository.save(batch);
    }

    @Override
    public Batch updateBatch(Batch batch, long id) {
        return null;
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

    @Override
    public void deleteBatch(long id) {
        Batch existingBatch = batchRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Batch", "Id", id));
        batchRepository.delete(existingBatch);
    }
}
