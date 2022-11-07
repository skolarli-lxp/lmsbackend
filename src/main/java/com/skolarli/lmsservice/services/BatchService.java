package com.skolarli.lmsservice.services;

import com.skolarli.lmsservice.models.NewBatchRequest;
import com.skolarli.lmsservice.models.db.Batch;

import java.util.List;

public interface BatchService {
    Batch toBatch(NewBatchRequest newBatchRequest);
    Batch saveBatch(Batch batch);
    Batch updateBatch(Batch batch, long id);
    Batch getBatch(long id);
    List<Batch> getAllBatches();
    List<Batch> getBatchesForCourse(long courseId);
    void deleteBatch(long id);
    void hardDeleteBatch(long id);
}
