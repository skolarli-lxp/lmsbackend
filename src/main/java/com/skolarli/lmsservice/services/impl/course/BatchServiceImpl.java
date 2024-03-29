package com.skolarli.lmsservice.services.impl.course;

import com.skolarli.lmsservice.exception.OperationNotSupportedException;
import com.skolarli.lmsservice.exception.ResourceNotFoundException;
import com.skolarli.lmsservice.models.db.core.LmsUser;
import com.skolarli.lmsservice.models.db.course.Batch;
import com.skolarli.lmsservice.models.dto.course.NewBatchRequest;
import com.skolarli.lmsservice.repository.course.BatchRepository;
import com.skolarli.lmsservice.services.core.LmsUserService;
import com.skolarli.lmsservice.services.course.BatchService;
import com.skolarli.lmsservice.services.course.CourseService;
import com.skolarli.lmsservice.utils.UserUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BatchServiceImpl implements BatchService {

    final Logger logger = LoggerFactory.getLogger(BatchServiceImpl.class);
    private final BatchRepository batchRepository;
    private final UserUtils userUtils;
    private final CourseService courseService;
    private final LmsUserService lmsUserService;


    public BatchServiceImpl(BatchRepository batchRepository, UserUtils userUtils,
                            CourseService courseService, LmsUserService lmsUserService) {
        this.batchRepository = batchRepository;
        this.userUtils = userUtils;
        this.courseService = courseService;
        this.lmsUserService = lmsUserService;
    }

    private Boolean validateInstructor(LmsUser instructor) {
        if (instructor.getIsInstructor() == null || !instructor.getIsInstructor()) {
            logger.error("Batch instructor not valid");
            return false;
        }
        return true;
    }

    private Boolean validate(Batch batch) {
        List<LmsUser> instructors = batch.getInstructors();
        if (instructors != null && !instructors.isEmpty()) {
            return instructors.stream().allMatch(this::validateInstructor);
        }
        return true;
    }

    private Boolean checkPermissions(Batch existingBatch) {
        LmsUser currentUser = userUtils.getCurrentUser();
        return currentUser.getIsAdmin()
                || currentUser == existingBatch.getCourse().getCourseOwner();
    }

    @Override
    public Batch toBatch(NewBatchRequest newBatchRequest) {
        Batch batch = new Batch();
        if (newBatchRequest.getCourseId() != 0) {
            batch.setCourse(courseService.getCourseById(newBatchRequest.getCourseId()));
        }
        if (newBatchRequest.getInstructorIds() != null) {
            List<LmsUser> instructors = newBatchRequest.getInstructorIds().stream()
                    .map(lmsUserService::getLmsUserById).collect(Collectors.toList());
            batch.setInstructors(instructors);
        }
        batch.setBatchName(newBatchRequest.getBatchName());
        batch.setBatchEnrollmentCapacity(newBatchRequest.getBatchEnrollmentCapacity());
        batch.setBatchAdditionalInfo(newBatchRequest.getBatchAdditionalInfo());
        batch.setBatchFees(newBatchRequest.getBatchFees());
        batch.setBatchDiscountType(newBatchRequest.getBatchDiscountType());
        batch.setBatchDiscountAmount(newBatchRequest.getBatchDiscountAmount());

        if (newBatchRequest.getBatchStartDate() != null) {
            batch.setBatchStartDate(newBatchRequest.getBatchStartDate().toInstant());
        }
        if (newBatchRequest.getBatchEndDate() != null) {
            batch.setBatchEndDate(newBatchRequest.getBatchEndDate().toInstant());
        }
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
        List<Batch> existingBatch = batchRepository.findAllById(new ArrayList<>(List.of(id)));
        if (existingBatch.size() == 0) {
            throw new ResourceNotFoundException("Batch", "Id", id);
        }
        return existingBatch.get(0);
    }

    @Override
    public List<Batch> getBatchesForCourse(long courseId) {
        List<Batch> batches = batchRepository.findByCourse_Id(courseId);
        return batches;
    }

    @Override
    public Long getBatchCount() {
        return batchRepository.findBatchCount();
    }

    @Override
    public Batch saveBatch(Batch batch) {
        LmsUser currentUser = userUtils.getCurrentUser();
        if (!currentUser.getIsAdmin() && currentUser != batch.getCourse().getCourseOwner()) {
            throw new OperationNotSupportedException("Operation not supported");
        }
        if (!validate(batch)) {
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
        if (!currentUser.getIsAdmin() && currentUser
                != existingBatch.getCourse().getCourseOwner()) {
            throw new OperationNotSupportedException(
                    "User does not have permission to perform Update operation");
        }
        if (newBatch.getBatchIsDeleted() != null) {
            logger.error("Cannot change deleted status. Use Delete API");
            existingBatch.setBatchIsDeleted(null);
        }
        if (newBatch.getInstructors() != null && !validate(newBatch)) {
            throw new OperationNotSupportedException("Operation not supported");
        }
        existingBatch.update(newBatch);
        return batchRepository.save(existingBatch);
    }

    private void canDelete(Batch existingBatch) {
        if (!checkPermissions(existingBatch)) {
            throw new OperationNotSupportedException(
                    "User does not gave permissions to perform delete operation");
        }
        if (existingBatch.getBatchSchedules() != null
                && !existingBatch.getBatchSchedules().isEmpty()) {
            throw new OperationNotSupportedException(
                    "Cannot delete batch with schedules. Please delete schedules first");
        }
    }

    @Override
    public void softDeleteBatch(long id) {
        Batch existingBatch = batchRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Batch", "Id", id));
        try {
            canDelete(existingBatch);
        } catch (OperationNotSupportedException e) {
            logger.error("Cannot delete batch", e);
            throw e;
        }
        existingBatch.setBatchIsDeleted(true);
        batchRepository.save(existingBatch);
    }

    @Override
    public void hardDeleteBatch(long id) {
        Batch existingBatch = batchRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Batch", "Id", id));
        try {
            canDelete(existingBatch);
        } catch (OperationNotSupportedException e) {
            logger.error("Cannot delete batch", e);
            throw e;
        }
        batchRepository.delete(existingBatch);
    }

}
