package com.skolarli.lmsservice.services.impl.course;

import com.skolarli.lmsservice.exception.OperationNotSupportedException;
import com.skolarli.lmsservice.exception.ResourceNotFoundException;
import com.skolarli.lmsservice.models.db.core.LmsUser;
import com.skolarli.lmsservice.models.db.course.Batch;
import com.skolarli.lmsservice.models.db.course.BatchSchedule;
import com.skolarli.lmsservice.models.dto.course.NewBatchScheduleRequest;
import com.skolarli.lmsservice.models.dto.course.NewBatchSchedulesForBatchRequest;
import com.skolarli.lmsservice.repository.course.BatchScheduleRepository;
import com.skolarli.lmsservice.services.course.BatchScheduleService;
import com.skolarli.lmsservice.services.course.BatchService;
import com.skolarli.lmsservice.utils.UserUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Instant;
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
        if (newBatchSchedulesForBatchRequest.getEndDateTime().isBefore(
            newBatchSchedulesForBatchRequest.getStartDateTime())) {
            throw new OperationNotSupportedException(
                "End date time cannot be before start date time");
        }
        BatchSchedule batchSchedule = new BatchSchedule();
        if (newBatchSchedulesForBatchRequest.getStartDateTime() != null) {
            batchSchedule.setStartDateTime(newBatchSchedulesForBatchRequest.getStartDateTime()
                .toInstant());
        }
        if (newBatchSchedulesForBatchRequest.getEndDateTime() != null) {
            batchSchedule.setEndDateTime(newBatchSchedulesForBatchRequest.getEndDateTime()
                .toInstant());
        }
        if (newBatchSchedulesForBatchRequest.getMeetingLink() != null) {
            batchSchedule.setMeetingLink(newBatchSchedulesForBatchRequest.getMeetingLink());
        }

        if (newBatchSchedulesForBatchRequest.getResourceFileUrl() != null) {
            batchSchedule.setResourceFileUrl(newBatchSchedulesForBatchRequest.getResourceFileUrl());
        }
        if (newBatchSchedulesForBatchRequest.getTrainerInstructionsText() != null) {
            batchSchedule.setTrainerInstructionsText(newBatchSchedulesForBatchRequest.getTrainerInstructionsText());
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
        if (newBatchScheduleRequest.getStartDateTime() != null
            && newBatchScheduleRequest.getEndDateTime() != null
            && newBatchScheduleRequest.getEndDateTime().isBefore(newBatchScheduleRequest.getStartDateTime())) {
            throw new OperationNotSupportedException(
                "End date time cannot be before start date time");
        }

        BatchSchedule batchSchedule = new BatchSchedule();

        if (newBatchScheduleRequest.getBatchId() != 0) {
            batchSchedule.setBatch(batchService.getBatch(newBatchScheduleRequest.getBatchId()));
        }
        if (newBatchScheduleRequest.getTitle() != null) {
            batchSchedule.setTitle(newBatchScheduleRequest.getTitle());
        }
        if (newBatchScheduleRequest.getDescription() != null) {
            batchSchedule.setDescription(newBatchScheduleRequest.getDescription());
        }
        batchSchedule.setMeetingLink(newBatchScheduleRequest.getMeetingLink());
        batchSchedule.setResourceFileUrl(newBatchScheduleRequest.getResourceFileUrl());
        batchSchedule.setTrainerInstructionsText(newBatchScheduleRequest.getTrainerInstructionsText());

        if (newBatchScheduleRequest.getStartDateTime() != null) {
            batchSchedule.setStartDateTime(newBatchScheduleRequest.getStartDateTime()
                .toInstant());
        }
        if (newBatchScheduleRequest.getEndDateTime() != null) {
            batchSchedule.setEndDateTime(newBatchScheduleRequest.getEndDateTime()
                .toInstant());
        }
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

    private List<BatchSchedule> getAllBatchSchedulesForTimePeriodAndBatch(Instant queryStartDate,
                                                                          Instant queryEndDate,
                                                                          Long batchId) {
        if (batchId == null) {
            throw new OperationNotSupportedException("Batch id cannot be null");
        }

        if (queryStartDate != null && queryEndDate != null) {
            if (queryStartDate.isAfter(queryEndDate)) {
                throw new OperationNotSupportedException(
                    "Query start date cannot be after query end date");
            }
            return batchScheduleRepository.findAllByStartDateTimeBetweenAndBatch_Id(
                queryStartDate, queryEndDate, batchId);
        } else if (queryStartDate != null) {
            return batchScheduleRepository.findAllByStartDateTimeAfterAndBatch_Id(
                queryStartDate, batchId);
        } else if (queryEndDate != null) {
            return batchScheduleRepository.findAllByStartDateTimeBeforeAndBatch_Id(
                queryEndDate, batchId);
        } else {
            throw new OperationNotSupportedException(
                "Query start date and query end date cannot be null");
        }
    }

    private List<BatchSchedule> getAllBatchSchedulesForTimePeriod(Instant queryStartDate,
                                                                  Instant queryEndDate) {
        if (queryStartDate != null && queryEndDate != null) {
            if (queryStartDate.isAfter(queryEndDate)) {
                throw new OperationNotSupportedException(
                    "Query start date cannot be after query end date");
            }
            return batchScheduleRepository.findAllByStartDateTimeBetween(
                queryStartDate, queryEndDate);
        } else if (queryStartDate != null) {
            return batchScheduleRepository.findAllByStartDateTimeAfter(
                queryStartDate);
        } else if (queryEndDate != null) {
            return batchScheduleRepository.findAllByStartDateTimeBefore(
                queryEndDate);
        } else {
            throw new OperationNotSupportedException(
                "Query start date and query end date cannot be null");
        }
    }

    @Override
    public List<BatchSchedule> getSchedulesWithCriteria(Long batchId, Instant queryStartDate,
                                                        Instant queryEndDate) {

        if (batchId == null) {
            if (queryStartDate != null || queryEndDate != null) {
                return getAllBatchSchedulesForTimePeriod(queryStartDate, queryEndDate);
            } else {
                return getAllBatchSchedules();
            }
        } else {
            if (queryStartDate != null || queryEndDate != null) {
                return getAllBatchSchedulesForTimePeriodAndBatch(queryStartDate, queryEndDate,
                    batchId);
            } else {
                return batchScheduleRepository.findByBatch_Id(batchId);
            }
        }
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

    private void canDelete(BatchSchedule existingBatchSchedule) {
        if (!checkPermissions(existingBatchSchedule)) {
            throw new OperationNotSupportedException("Operation not supported: Permission Denied");
        }
        if (existingBatchSchedule.getAttendanceList() != null && !existingBatchSchedule
            .getAttendanceList().isEmpty()) {
            throw new OperationNotSupportedException(
                "Cannot delete batch schedule with attendance");
        }
    }

    @Override
    public void softDeleteBatchSchedule(long id) {
        BatchSchedule existingBatchSchedule = batchScheduleRepository.findById(id).orElseThrow(
            () -> new ResourceNotFoundException("BatchSchedule", "Id", id));
        try {
            canDelete(existingBatchSchedule);
        } catch (OperationNotSupportedException e) {
            logger.error(e.getMessage());
            throw e;
        }
        existingBatchSchedule.setBatchScheduleIsDeleted(true);
        batchScheduleRepository.save(existingBatchSchedule);
    }

    @Override
    public void hardDeleteBatchSchedule(long id) {
        BatchSchedule existingBatchSchedule = batchScheduleRepository.findById(id).orElseThrow(
            () -> new ResourceNotFoundException("BatchSchedule", "Id", id));
        try {
            canDelete(existingBatchSchedule);
        } catch (OperationNotSupportedException e) {
            logger.error(e.getMessage());
            throw e;
        }
        batchScheduleRepository.delete(existingBatchSchedule);
    }
}
