package com.skolarli.lmsservice.controller.course;

import com.skolarli.lmsservice.exception.OperationNotSupportedException;
import com.skolarli.lmsservice.models.db.core.LmsUser;
import com.skolarli.lmsservice.models.db.course.BatchSchedule;
import com.skolarli.lmsservice.models.dto.course.BatchScheduleResponse;
import com.skolarli.lmsservice.models.dto.course.NewBatchScheduleRequest;
import com.skolarli.lmsservice.models.dto.course.NewBatchSchedulesForBatchRequest;
import com.skolarli.lmsservice.services.course.BatchScheduleService;
import com.skolarli.lmsservice.services.course.BatchService;
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
import java.util.ArrayList;
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
    public ResponseEntity<?> getAllBatchSchedules(
        @RequestParam(required = false) Long batchId,
        @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd")
        LocalDate queryStartDate,
        @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd")
        LocalDate queryEndDate,
        @RequestParam(required = false) Boolean condensed) {

        UUID uuid = UUID.randomUUID();
        MDC.put("requestId", uuid.toString());
        logger.info("Received request for getAllBatchSchedules" + (batchId != null
            ? " for batchId: " + batchId
            : "") + (queryStartDate != null
            ? " for queryStartDate: " + queryStartDate
            : "") + (queryEndDate != null
            ? " for queryEndDate: " + queryEndDate
            : ""));

        List<BatchSchedule> response = null;
        List<BatchScheduleResponse> condensedResponse = new ArrayList<>();

        Instant queryStartDateInstant = null;
        Instant queryEndDateAsDate = null;

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
            response = batchScheduleService.getSchedulesWithCriteria(
                batchId, queryStartDateInstant, queryEndDateAsDate);
            if (condensed != null && condensed == Boolean.TRUE) {
                for (BatchSchedule schedule : response) {
                    condensedResponse.add(new BatchScheduleResponse(schedule));
                }
                return new ResponseEntity<>(condensedResponse, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(response, HttpStatus.OK);
            }
        } catch (Exception e) {
            logger.error("Error in getAllBatchSchedules: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        } finally {
            MDC.remove("requestId");
        }
    }

    @RequestMapping(method = RequestMethod.GET, value = "{id}")
    public ResponseEntity<?> getBatchSchedule(@PathVariable long id,
                                                          @RequestParam(required = false)
                                                          Boolean condensed) {

        UUID uuid = UUID.randomUUID();
        MDC.put("requestId", uuid.toString());
        logger.info("Received request for getBatchSchedule for id: " + id);

        BatchSchedule response = null;
        BatchScheduleResponse condensedResponse = null;

        try {
            response = batchScheduleService.getBatchSchedule(id);
            if (condensed != null && condensed == Boolean.TRUE) {
                condensedResponse = new BatchScheduleResponse(response);
                return new ResponseEntity<>(condensedResponse, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(response, HttpStatus.OK);
            }
        } catch (Exception e) {
            logger.error("Error in getBatchSchedule: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        } finally {
            MDC.remove("requestId");
        }
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> addBatchSchedule(
        @Valid @RequestBody NewBatchScheduleRequest request,
        @RequestParam Boolean condensed) {

        UUID uuid = UUID.randomUUID();
        MDC.put("requestId", uuid.toString());
        logger.info("Received request for addBatchSchedule for batch: " + request.getBatchId());

        BatchSchedule batchSchedule = batchScheduleService.toBatchSchedule(request);

        try {
            BatchSchedule response = batchScheduleService.saveBatchSchedule(batchSchedule);
            if (condensed != null && condensed == Boolean.TRUE) {
                return new ResponseEntity<>(new BatchScheduleResponse(response), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(response, HttpStatus.OK);
            }
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
    public ResponseEntity<?> addBatchSchedules(
        @Valid @RequestBody List<NewBatchSchedulesForBatchRequest> request,
        @RequestParam Long batchId,
        @RequestParam Boolean condensed) {

        UUID uuid = UUID.randomUUID();
        MDC.put("requestId", uuid.toString());
        logger.info("Received request for addBatchSchedules for batch: " + batchId);

        List<BatchSchedule> batchSchedules = batchScheduleService.toBatchScheduleList(request);

        List<BatchSchedule> response = null;
        List<BatchScheduleResponse> condensedResponse = new ArrayList<>();

        try {
            response = batchScheduleService.saveAllBatchSchedules(batchSchedules, batchId);
            if (condensed != null && condensed == Boolean.TRUE) {
                for (BatchSchedule schedule : response) {
                    condensedResponse.add(new BatchScheduleResponse(schedule));
                }
                return new ResponseEntity<>(condensedResponse, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(response, HttpStatus.OK);
            }
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
    public ResponseEntity<?> updateBatchSchedule(
        @PathVariable Long id,
        @RequestBody NewBatchScheduleRequest request,
        @RequestParam(required = false) Boolean condensed) {

        UUID uuid = UUID.randomUUID();
        MDC.put("requestId", uuid.toString());
        logger.info("Received request for updateBatchSchedule for id: " + id);

        BatchSchedule batchSchedule = batchScheduleService.toBatchSchedule(request);

        try {
            BatchSchedule response = batchScheduleService.updateBatchSchedule(batchSchedule,
                id);
            if (condensed != null && condensed == Boolean.TRUE) {
                return new ResponseEntity<>(new BatchScheduleResponse(response), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(response, HttpStatus.OK);
            }
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
