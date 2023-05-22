package com.skolarli.lmsservice.services;

import com.skolarli.lmsservice.models.db.Batch;
import com.skolarli.lmsservice.models.dto.NewBatchRequest;

import java.util.List;

public interface BatchService {
    Batch toBatch(NewBatchRequest newBatchRequest);

    Batch getBatch(long id);

    List<Batch> getAllBatches();

    List<Batch> getBatchesForCourse(long courseId);

    Batch saveBatch(Batch batch);

    Batch updateBatch(Batch batch, long id);

    void softDeleteBatch(long id);

    void hardDeleteBatch(long id);
}
