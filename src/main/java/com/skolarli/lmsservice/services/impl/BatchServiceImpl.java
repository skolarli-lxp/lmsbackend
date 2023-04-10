package com.skolarli.lmsservice.services.impl;

import com.skolarli.lmsservice.exception.OperationNotSupportedException;
import com.skolarli.lmsservice.exception.ResourceNotFoundException;
import com.skolarli.lmsservice.models.NewBatchRequest;
import com.skolarli.lmsservice.models.db.Batch;
import com.skolarli.lmsservice.models.db.LmsUser;
import com.skolarli.lmsservice.repository.BatchRepository;
import com.skolarli.lmsservice.services.BatchService;
import com.skolarli.lmsservice.services.CourseService;
import com.skolarli.lmsservice.services.LmsUserService;
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
    private final CourseService courseService;
    private final LmsUserService lmsUserService;


    public BatchServiceImpl(BatchRepository batchRepository, UserUtils userUtils, CourseService courseService, LmsUserService lmsUserService) {
        this.batchRepository = batchRepository;
        this.userUtils = userUtils;
        this.courseService = courseService;
        this.lmsUserService = lmsUserService;
    }

    private Boolean validate(Batch batch){
        LmsUser instructor = batch.getInstructor();
        if (instructor.getIsInstructor()== null || instructor.getIsInstructor()==false){
            logger.error("Batch instructor not valid");
            return false;
        }
        return true;
    }

    private Boolean checkPermissions (Batch existingBatch) {
        LmsUser currentUser = userUtils.getCurrentUser();
        if (currentUser.getIsAdmin() != true && currentUser != existingBatch.getCourse().getCourseOwner()) {
            return false;
        }
        return true;
    }

    @Override
    public Batch toBatch(NewBatchRequest newBatchRequest) {
        Batch batch = new Batch();
        if (newBatchRequest.getCourseId() != 0) {
            batch.setCourse(courseService.getCourseById(newBatchRequest.getCourseId()));
        }
        if (newBatchRequest.getInstructorId() != 0) {
            batch.setInstructor(lmsUserService.getLmsUserById(newBatchRequest.getInstructorId()));
        }
        batch.setBatchName(newBatchRequest.getBatchName());
        batch.setBatchEnrollmentCapacity(newBatchRequest.getBatchEnrollmentCapacity());
        batch.setBatchAdditionalInfo(newBatchRequest.getBatchAdditionalInfo());
        batch.setBatchFees(newBatchRequest.getBatchFees());
        batch.setBatchDiscountType(newBatchRequest.getBatchDiscountType());
        batch.setBatchDiscountAmount(newBatchRequest.getBatchDiscountAmount());

        batch.setBatchStartDate(newBatchRequest.getBatchStartDate());
        batch.setBatchEndDate(newBatchRequest.getBatchEndDate());
        batch.setBatchStatus(newBatchRequest.getBatchStatus());
        batch.setBatchDeliveryFormat(newBatchRequest.getBatchDeliveryFormat());
        batch.setBatchDurationHours(newBatchRequest.getBatchDurationHours());
        batch.setBatchCustomJs(newBatchRequest.getBatchCustomJs());
        batch.setBatchSeoAllowComments(newBatchRequest.getBatchSeoAllowComments());
        batch.setBatchSeoAllowRatings(newBatchRequest.getBatchSeoAllowRatings());
        batch.setBatchSeoTitleTag(newBatchRequest.getBatchSeoTitleTag());
        batch.setBatchSeoMetaDescription(newBatchRequest.getBatchSeoMetaDescription());
        batch.setBatchSeoMetaKeywords(newBatchRequest.getBatchSeoMetaKeywords());

        return batch;
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
    public Batch saveBatch(Batch batch) {
        LmsUser currentUser = userUtils.getCurrentUser();
        if (currentUser.getIsAdmin() != true && currentUser != batch.getCourse().getCourseOwner()) {
            throw new OperationNotSupportedException("Operation not supported");
        }
        if (validate(batch) == false){
            throw new OperationNotSupportedException("Operation not supported");
        }
        batch.setBatchIsDeleted(false);
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
        if (newBatch.getInstructor() != null && validate(newBatch) == false){
            throw new OperationNotSupportedException("Operation not supported");
        }
        existingBatch.update(newBatch);        
        return batchRepository.save(existingBatch);      
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
