package com.skolarli.lmsservice.controller;

import com.skolarli.lmsservice.exception.OperationNotSupportedException;
import com.skolarli.lmsservice.models.db.BatchSchedule;
import com.skolarli.lmsservice.models.db.LmsUser;
import com.skolarli.lmsservice.models.dto.NewBatchScheduleRequest;
import com.skolarli.lmsservice.models.dto.NewBatchSchedulesForBatchRequest;
import com.skolarli.lmsservice.services.BatchScheduleService;
import com.skolarli.lmsservice.services.BatchService;
import com.skolarli.lmsservice.utils.UserUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import javax.validation.Valid;

@RestController
@RequestMapping("/batchschedule")
public class BatchScheduleController {
    final BatchScheduleService batchScheduleService;
    final BatchService batchService;
    final UserUtils userUtils;
    final Logger logger = LoggerFactory.getLogger(BatchScheduleController.class);

    public BatchScheduleController(BatchScheduleService batchScheduleService,
                                   BatchService batchService, UserUtils userUtils) {
        this.batchScheduleService = batchScheduleService;
        this.batchService = batchService;
        this.userUtils = userUtils;
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<BatchSchedule>> getAllBatchSchedules(
            @RequestParam(required = false) Long batchId,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd")
            LocalDate queryStartDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd")
            LocalDate queryEndDate) {

        UUID uuid = UUID.randomUUID();
        MDC.put("requestId", uuid.toString());
        logger.info("Received request for getAllBatchSchedules" + (batchId != null
                ? " for batchId: " + batchId
                : "") + (queryStartDate != null
                ? " for queryStartDate: " + queryStartDate
                : "") + (queryEndDate != null
                ? " for queryEndDate: " + queryEndDate
                : ""));


        Instant queryStartDateInstant = null;
        Instant queryEndDateAsDate = null;
        if (batchId != null) {
            if (queryStartDate != null) {
                queryStartDateInstant = queryStartDate.atStartOfDay().toInstant(
                        java.time.ZoneOffset.UTC);
            }
            if (queryEndDate != null) {
                queryEndDateAsDate = queryEndDate.atStartOfDay().toInstant(
                        java.time.ZoneOffset.UTC);
                // Make it end of day
                queryEndDateAsDate = queryEndDateAsDate.plusSeconds(86399);
            }
            try {
                return new ResponseEntity<>(
                        batchScheduleService.getSchedulesForBatch(
                                batchId, queryStartDateInstant, queryEndDateAsDate),
                        HttpStatus.OK);
            } catch (Exception e) {
                logger.error("Error in getAllBatchSchedules: " + e.getMessage());
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
            } finally {
                MDC.remove("requestId");
            }
        }
        try {
            return new ResponseEntity<>(batchScheduleService.getAllBatchSchedules(),
                    HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error in getAllBatchSchedules: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        } finally {
            MDC.remove("requestId");
        }
    }

    @RequestMapping(method = RequestMethod.GET, value = "{id}")
    public ResponseEntity<BatchSchedule> getBatchSchedule(@PathVariable long id) {

        UUID uuid = UUID.randomUUID();
        MDC.put("requestId", uuid.toString());
        logger.info("Received request for getBatchSchedule for id: " + id);

        try {
            return new ResponseEntity<>(batchScheduleService.getBatchSchedule(id), HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error in getBatchSchedule: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        } finally {
            MDC.remove("requestId");
        }
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<BatchSchedule> addBatchSchedule(
            @Valid @RequestBody NewBatchScheduleRequest request) {

        UUID uuid = UUID.randomUUID();
        MDC.put("requestId", uuid.toString());
        logger.info("Received request for addBatchSchedule for batch: " + request.getBatchId());

        BatchSchedule batchSchedule = batchScheduleService.toBatchSchedule(request);

        try {
            return new ResponseEntity<>(batchScheduleService.saveBatchSchedule(batchSchedule),
                    HttpStatus.OK);
        } catch (OperationNotSupportedException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage());
        } catch (Exception e) {
            logger.error("Error in addBatchSchedule: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        } finally {
            MDC.remove("requestId");
        }
    }

    @RequestMapping(method = RequestMethod.POST, value = "/forbatch")
    public ResponseEntity<List<BatchSchedule>> addBatchSchedules(
            @Valid @RequestBody List<NewBatchSchedulesForBatchRequest> request,
            @RequestParam Long batchId) {

        UUID uuid = UUID.randomUUID();
        MDC.put("requestId", uuid.toString());
        logger.info("Received request for addBatchSchedules for batch: " + batchId);

        List<BatchSchedule> batchSchedules = batchScheduleService.toBatchScheduleList(request);

        try {
            return new ResponseEntity<>(batchScheduleService.saveAllBatchSchedules(
                    batchSchedules, batchId), HttpStatus.OK);
        } catch (OperationNotSupportedException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage());
        } catch (Exception e) {
            logger.error("Error in addBatchSchedule: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        } finally {
            MDC.remove("requestId");
        }
    }

    @RequestMapping(method = RequestMethod.PUT, value = "{id}")
    public ResponseEntity<BatchSchedule> updateBatchSchedule(
            @PathVariable Long id,
            @RequestBody NewBatchScheduleRequest request) {

        UUID uuid = UUID.randomUUID();
        MDC.put("requestId", uuid.toString());
        logger.info("Received request for updateBatchSchedule for id: " + id);

        BatchSchedule batchSchedule = new BatchSchedule();
        if (request.getBatchId() != 0) {
            batchSchedule.setBatch(batchService.getBatch(request.getBatchId()));
        }
        if (request.getStartDateTime() != null) {
            batchSchedule.setStartDateTime(request.getStartDateTime().toInstant());
        }
        if (request.getEndDateTime() != null) {
            batchSchedule.setEndDateTime(request.getEndDateTime().toInstant());
        }
        batchSchedule.setTitle(request.getTitle());
        batchSchedule.setDescription(request.getDescription());
        batchSchedule.setMeetingLink(request.getMeetingLink());

        try {
            return new ResponseEntity<>(batchScheduleService.updateBatchSchedule(batchSchedule,
                    id), HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error in updateBatchSchedule: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        } finally {
            MDC.remove("requestId");
        }
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "{id}")
    public ResponseEntity<String> hardDeleteBatchSchedule(@PathVariable long id) {

        UUID uuid = UUID.randomUUID();
        MDC.put("requestId", uuid.toString());
        logger.info("Received request for delete batch schedule for id: " + id);

        BatchSchedule batchSchedule = batchScheduleService.getBatchSchedule(id);
        LmsUser currentUser = userUtils.getCurrentUser();
        if (!currentUser.getIsAdmin() && currentUser
                != batchSchedule.getBatch().getCourse().getCourseOwner()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "");
        }
        try {
            batchScheduleService.hardDeleteBatchSchedule(id);
            return new ResponseEntity<>("Deleted Schedule", HttpStatus.OK);
        } catch (OperationNotSupportedException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        } catch (Exception e) {
            logger.error("Error in hardDeleteBatchSchedule: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        } finally {
            MDC.remove("requestId");
        }
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "soft/{id}")
    public ResponseEntity<String> deleteBatchSchedule(@PathVariable long id) {

        UUID uuid = UUID.randomUUID();
        MDC.put("requestId", uuid.toString());
        logger.info("Received request for soft  delete batch schedule for id: " + id);

        BatchSchedule batchSchedule = batchScheduleService.getBatchSchedule(id);
        LmsUser currentUser = userUtils.getCurrentUser();
        if (!currentUser.getIsAdmin() && currentUser
                != batchSchedule.getBatch().getCourse().getCourseOwner()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "");
        }
        try {
            batchScheduleService.softDeleteBatchSchedule(id);
            return new ResponseEntity<>("Deleted Schedule", HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error in deleteBatchSchedule: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        } finally {
            MDC.remove("requestId");
        }
    }
}
