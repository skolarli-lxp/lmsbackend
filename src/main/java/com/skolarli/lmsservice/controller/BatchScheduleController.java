package com.skolarli.lmsservice.controller;

import com.skolarli.lmsservice.exception.OperationNotSupportedException;
import com.skolarli.lmsservice.models.NewBatchScheduleRequest;
import com.skolarli.lmsservice.models.NewBatchSchedulesForBatchRequest;
import com.skolarli.lmsservice.models.db.BatchSchedule;
import com.skolarli.lmsservice.models.db.LmsUser;
import com.skolarli.lmsservice.services.BatchScheduleService;
import com.skolarli.lmsservice.services.BatchService;
import com.skolarli.lmsservice.utils.UserUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/batchschedule")
public class BatchScheduleController {
    Logger logger = LoggerFactory.getLogger(BatchScheduleController.class);
    final BatchScheduleService batchScheduleService;
    final BatchService batchService;
    final UserUtils userUtils;

    public BatchScheduleController(BatchScheduleService batchScheduleService,
                                   BatchService batchService, UserUtils userUtils) {
        this.batchScheduleService = batchScheduleService;
        this.batchService = batchService;
        this.userUtils = userUtils;
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<BatchSchedule>> getAllBatchSchedules(
            @RequestParam(required = false) Long batchId) {
        if (batchId != null) {
            try {
                return new ResponseEntity<>(batchScheduleService.getSchedulesForBatch(batchId),
                        HttpStatus.OK);
            } catch (Exception e) {
                logger.error("Error in getAllBatchSchedules: " + e.getMessage());
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
            }
        }
        try {
            return new ResponseEntity<>(batchScheduleService.getAllBatchSchedules(),
                    HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error in getAllBatchSchedules: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @RequestMapping(method = RequestMethod.GET, value = "{id}")
    public ResponseEntity<BatchSchedule> getBatchSchedule(@PathVariable long id) {
        try {
            return new ResponseEntity<>(batchScheduleService.getBatchSchedule(id), HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error in getBatchSchedule: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<BatchSchedule> addBatchSchedule(
            @Valid @RequestBody NewBatchScheduleRequest request) {
        BatchSchedule batchSchedule = batchScheduleService.toBatchSchedule(request);

        try {
            return new ResponseEntity<>(batchScheduleService.saveBatchSchedule(batchSchedule),
                    HttpStatus.OK);
        } catch (OperationNotSupportedException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage());
        } catch (Exception e) {
            logger.error("Error in addBatchSchedule: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @RequestMapping(method = RequestMethod.POST, value = "/forbatch")
    public ResponseEntity<List<BatchSchedule>> addBatchSchedules(
            @Valid @RequestBody List<NewBatchSchedulesForBatchRequest> request,
            @RequestParam Long batchId) {
        List<BatchSchedule> batchSchedules = batchScheduleService.toBatchScheduleList(request);

        try {
            return new ResponseEntity<>(batchScheduleService.saveAllBatchSchedules(
                    batchSchedules, batchId), HttpStatus.OK);
        } catch (OperationNotSupportedException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage());
        } catch (Exception e) {
            logger.error("Error in addBatchSchedule: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @RequestMapping(method = RequestMethod.PUT, value = "{id}")
    public ResponseEntity<BatchSchedule> updateBatchSchedule(
            @PathVariable Long id,
            @RequestBody NewBatchScheduleRequest request) {
        BatchSchedule batchSchedule = new BatchSchedule();
        if (request.getBatchId() != 0) {
            batchSchedule.setBatch(batchService.getBatch(request.getBatchId()));
        }
        batchSchedule.setStartDateTime(request.getStartDateTime());
        batchSchedule.setEndDateTime(request.getEndDateTime());
        batchSchedule.setTitle(request.getTitle());
        batchSchedule.setDescription(request.getDescription());
        batchSchedule.setMeetingLink(request.getMeetingLink());

        try {
            return new ResponseEntity<>(batchScheduleService.updateBatchSchedule(batchSchedule,
                    id), HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error in updateBatchSchedule: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "{id}")
    public ResponseEntity<String> deleteBatchSchedule(@PathVariable long id) {
        BatchSchedule batchSchedule = batchScheduleService.getBatchSchedule(id);
        LmsUser currentUser = userUtils.getCurrentUser();
        if (!currentUser.getIsAdmin() && currentUser !=
                batchSchedule.getBatch().getCourse().getCourseOwner()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "");
        }
        try {
            batchScheduleService.deleteBatchSchedule(id);
            return new ResponseEntity<>("Deleted Schedule", HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error in deleteBatchSchedule: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "hard/{id}")
    public ResponseEntity<String> hardDeleteBatchSchedule(@PathVariable long id) {
        BatchSchedule batchSchedule = batchScheduleService.getBatchSchedule(id);
        LmsUser currentUser = userUtils.getCurrentUser();
        if (!currentUser.getIsAdmin() && currentUser !=
                batchSchedule.getBatch().getCourse().getCourseOwner()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "");
        }
        try {
            batchScheduleService.hardDeleteBatchSchedule(id);
            return new ResponseEntity<>("Deleted Schedule", HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error in hardDeleteBatchSchedule: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
