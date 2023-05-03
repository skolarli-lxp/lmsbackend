package com.skolarli.lmsservice.services.impl;

import com.skolarli.lmsservice.exception.OperationNotSupportedException;
import com.skolarli.lmsservice.exception.ResourceNotFoundException;
import com.skolarli.lmsservice.models.NewBatchScheduleRequest;
import com.skolarli.lmsservice.models.NewBatchSchedulesForBatchRequest;
import com.skolarli.lmsservice.models.db.Batch;
import com.skolarli.lmsservice.models.db.BatchSchedule;
import com.skolarli.lmsservice.models.db.LmsUser;
import com.skolarli.lmsservice.repository.BatchScheduleRepository;
import com.skolarli.lmsservice.services.BatchScheduleService;
import com.skolarli.lmsservice.services.BatchService;
import com.skolarli.lmsservice.utils.UserUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class BatchScheduleServiceImpl implements BatchScheduleService {
    final Logger logger = LoggerFactory.getLogger(BatchScheduleServiceImpl.class);

    final BatchScheduleRepository batchScheduleRepository;
    final UserUtils userUtils;

    final BatchService batchService;

    public BatchScheduleServiceImpl(BatchScheduleRepository batchScheduleRepository,
                                    UserUtils userUtils, BatchService batchService) {
        this.batchScheduleRepository = batchScheduleRepository;
        this.userUtils = userUtils;
        this.batchService = batchService;
    }

    private Boolean checkPermissions(BatchSchedule existingBatchSchedule) {
        LmsUser currentUser = userUtils.getCurrentUser();
        return currentUser.getIsAdmin()
                || currentUser == existingBatchSchedule.getBatch().getCourse().getCourseOwner();
    }

    private Boolean checkPermissions(Batch existingBatch) {
        LmsUser currentUser = userUtils.getCurrentUser();
        return currentUser.getIsAdmin()
                || currentUser == existingBatch.getCourse().getCourseOwner();
    }

    @Override
    public BatchSchedule toBatchSchedule(
            NewBatchSchedulesForBatchRequest newBatchSchedulesForBatchRequest) {
        if (newBatchSchedulesForBatchRequest.getEndDateTime().before(
                newBatchSchedulesForBatchRequest.getStartDateTime())) {
            throw new OperationNotSupportedException(
                    "End date time cannot be before start date time");
        }
        BatchSchedule batchSchedule = new BatchSchedule();
        if (newBatchSchedulesForBatchRequest.getStartDateTime() != null) {
            batchSchedule.setStartDateTime(newBatchSchedulesForBatchRequest.getStartDateTime());
        }
        if (newBatchSchedulesForBatchRequest.getEndDateTime() != null) {
            batchSchedule.setEndDateTime(newBatchSchedulesForBatchRequest.getEndDateTime());
        }
        if (newBatchSchedulesForBatchRequest.getMeetingLink() != null) {
            batchSchedule.setMeetingLink(newBatchSchedulesForBatchRequest.getMeetingLink());
        }
        if (newBatchSchedulesForBatchRequest.getTitle() != null) {
            batchSchedule.setTitle(newBatchSchedulesForBatchRequest.getTitle());
        }
        if (newBatchSchedulesForBatchRequest.getDescription() != null) {
            batchSchedule.setDescription(newBatchSchedulesForBatchRequest.getDescription());
        }
        return batchSchedule;
    }

    @Override
    public BatchSchedule toBatchSchedule(NewBatchScheduleRequest newBatchScheduleRequest) {
        if (newBatchScheduleRequest.getEndDateTime().before(
                newBatchScheduleRequest.getStartDateTime())) {
            throw new OperationNotSupportedException(
                    "End date time cannot be before start date time");
        }
        Batch batch = batchService.getBatch(newBatchScheduleRequest.getBatchId());
        // TODO: throw BAD REQUEST from controller in this case
        if (batch == null) {
            throw new ResourceNotFoundException("Batch", "Id",
                    newBatchScheduleRequest.getBatchId());
        }
        BatchSchedule batchSchedule = new BatchSchedule();
        batchSchedule.setBatch(batch);
        if (newBatchScheduleRequest.getTitle() != null) {
            batchSchedule.setTitle(newBatchScheduleRequest.getTitle());
        }
        if (newBatchScheduleRequest.getDescription() != null) {
            batchSchedule.setDescription(newBatchScheduleRequest.getDescription());
        }
        batchSchedule.setMeetingLink(newBatchScheduleRequest.getMeetingLink());
        batchSchedule.setStartDateTime(newBatchScheduleRequest.getStartDateTime());
        batchSchedule.setEndDateTime(newBatchScheduleRequest.getEndDateTime());
        batchSchedule.setBatchScheduleIsDeleted(false);
        return batchSchedule;
    }

    @Override
    public List<BatchSchedule> toBatchScheduleList(
            List<NewBatchSchedulesForBatchRequest> newBatchSchedulesForBatchRequests) {
        return newBatchSchedulesForBatchRequests.stream().map(
                this::toBatchSchedule).collect(Collectors.toList());
    }

    @Override
    public BatchSchedule getBatchSchedule(long id) {
        List<BatchSchedule> existingBatchSchedule = batchScheduleRepository.findAllById(
                new ArrayList<>(List.of(id)));
        if (existingBatchSchedule.size() == 0) {
            throw new ResourceNotFoundException("BatchSchedule", "Id", id);
        }
        return existingBatchSchedule.get(0);
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
    public BatchSchedule saveBatchSchedule(BatchSchedule batchSchedule) {
        if (!checkPermissions(batchSchedule)) {
            throw new OperationNotSupportedException("Operation not supported");
        }
        return batchScheduleRepository.save(batchSchedule);
    }

    @Override
    public List<BatchSchedule> saveAllBatchSchedules(List<BatchSchedule> batchSchedules,
                                                     Long batchId) {
        Batch existingBatch = batchService.getBatch(batchId);
        if (!checkPermissions(batchSchedules.get(0))) {
            throw new OperationNotSupportedException("Operation not supported");
        }
        for (BatchSchedule batchSchedule : batchSchedules) {
            batchSchedule.setBatch(existingBatch);
        }
        return batchScheduleRepository.saveAll(batchSchedules);
    }

    @Override
    public BatchSchedule updateBatchSchedule(BatchSchedule batchSchedule, long id) {
        BatchSchedule existingBatchSchedule = batchScheduleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("BatchSchedule", "Id", id));
        LmsUser currentUser = userUtils.getCurrentUser();
        if (!currentUser.getIsAdmin() && currentUser
                != existingBatchSchedule.getBatch().getCourse().getCourseOwner()) {
            throw new OperationNotSupportedException(
                    "Operation not supported: Permission Denied");
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
        if (!checkPermissions(existingBatchSchedule)) {
            throw new OperationNotSupportedException("Operation not supported: Permission Denied");
        }
        existingBatchSchedule.setBatchScheduleIsDeleted(true);
        batchScheduleRepository.save(existingBatchSchedule);
    }

    @Override
    public void hardDeleteBatchSchedule(long id) {
        BatchSchedule existingBatchSchedule = batchScheduleRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("BatchSchedule", "Id", id));
        if (!checkPermissions(existingBatchSchedule)) {
            throw new OperationNotSupportedException("Operation not supported: Permission Denied");
        }
        batchScheduleRepository.delete(existingBatchSchedule);

    }
}
