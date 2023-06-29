package com.skolarli.lmsservice.controller;

import com.skolarli.lmsservice.exception.OperationNotSupportedException;
import com.skolarli.lmsservice.models.db.Batch;
import com.skolarli.lmsservice.models.db.BatchSchedule;
import com.skolarli.lmsservice.models.dto.course.NewBatchRequest;
import com.skolarli.lmsservice.services.BatchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;
import javax.validation.Valid;

@RestController
@RequestMapping("/batch")
public class BatchController {
    final BatchService batchService;
    final Logger logger = LoggerFactory.getLogger(BatchController.class);

    public BatchController(BatchService batchService) {
        this.batchService = batchService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<Batch>> getAllBatches(@RequestParam(required = false)
                                                     Long courseId) {
        UUID uuid = UUID.randomUUID();
        MDC.put("requestId", uuid.toString());
        logger.info("Received request for getAllBatches" + (courseId != null
                ? " for courseId: " + courseId
                : ""));

        if (courseId != null) {
            try {
                return new ResponseEntity<>(batchService.getBatchesForCourse(courseId),
                        HttpStatus.OK);
            } catch (Exception e) {
                logger.error("Error in getAllBatches: " + e.getMessage());
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
            } finally {
                MDC.remove("requestId");
            }
        }

        try {
            return new ResponseEntity<>(batchService.getAllBatches(), HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error in getAllBatches: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        } finally {
            MDC.remove("requestId");
        }
    }

    @RequestMapping(method = RequestMethod.GET, value = "{id}")
    public ResponseEntity<Batch> getBatch(@PathVariable long id) {

        UUID uuid = UUID.randomUUID();
        MDC.put("requestId", uuid.toString());
        logger.info("Received request for getBatch for id: " + id);

        try {
            return new ResponseEntity<>(batchService.getBatch(id), HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error in getBatch: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        } finally {
            MDC.remove("requestId");
        }
    }

    @RequestMapping(method = RequestMethod.GET, value = "getSchedules/{id}")
    public ResponseEntity<List<BatchSchedule>> getSchedules(@PathVariable long id) {

        UUID uuid = UUID.randomUUID();
        MDC.put("requestId", uuid.toString());
        logger.info("Received request for getSchedules for id: " + id);

        try {
            Batch batch = batchService.getBatch(id);
            return new ResponseEntity<>(batch.getBatchSchedules(), HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error in getSchedules: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        } finally {
            MDC.remove("requestId");
        }
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Batch> addBatch(@Valid @RequestBody NewBatchRequest batchRequest) {

        UUID uuid = UUID.randomUUID();
        MDC.put("requestId", uuid.toString());
        logger.info("Received request for new batch for course"
                + batchRequest.getCourseId());

        Batch batch = batchService.toBatch(batchRequest);
        logger.info("Received request for new batch for course"
                + batch.getCourse().getCourseName());

        try {
            return new ResponseEntity<>(batchService.saveBatch(batch), HttpStatus.CREATED);
        } catch (OperationNotSupportedException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage());
        } catch (Exception e) {
            logger.error("Error in addBatch: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        } finally {
            MDC.remove("requestId");
        }
    }

    @RequestMapping(method = RequestMethod.PUT, value = "{id}")
    public ResponseEntity<Batch> updateBatch(@PathVariable long id,
                                             @RequestBody NewBatchRequest batchRequest) {
        UUID uuid = UUID.randomUUID();
        MDC.put("requestId", uuid.toString());
        logger.info("Received request for updating batch batchID: " + id);

        Batch batch = batchService.toBatch(batchRequest);
        logger.info("Received request for updating batch batchID: " + id);

        try {
            return new ResponseEntity<>(batchService.updateBatch(batch, id), HttpStatus.OK);
        } catch (OperationNotSupportedException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage());
        } catch (Exception e) {
            logger.error("Error in updateBatch: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        } finally {
            MDC.remove("requestId");
        }
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "{id}")
    public ResponseEntity<String> deleteBatch(@PathVariable long id) {

        UUID uuid = UUID.randomUUID();
        MDC.put("requestId", uuid.toString());
        logger.info("Received request for deleting batch ID : " + id);

        Batch batch = batchService.getBatch(id);
        logger.info("Received request for deleting batch for course"
                + batch.getCourse().getCourseName());

        try {
            batchService.softDeleteBatch(id);
            return new ResponseEntity<>("Batch Deleted !", HttpStatus.OK);
        } catch (OperationNotSupportedException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        } catch (Exception e) {
            logger.error("Error in deleteBatch: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        } finally {
            MDC.remove("requestId");
        }
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "hard/{id}")
    public ResponseEntity<String> hardDeleteBatch(@PathVariable long id) {

        UUID uuid = UUID.randomUUID();
        MDC.put("requestId", uuid.toString());
        logger.info("Received request for hard deleting batch ID : " + id);

        Batch batch = batchService.getBatch(id);
        logger.info("Received request for deleting batch for course"
                + batch.getCourse().getCourseName());

        try {
            batchService.hardDeleteBatch(id);
            return new ResponseEntity<>("Batch Deleted !", HttpStatus.OK);
        } catch (OperationNotSupportedException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage());
        } catch (Exception e) {
            logger.error("Error in deleteBatch: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        } finally {
            MDC.remove("requestId");
        }
    }
}
