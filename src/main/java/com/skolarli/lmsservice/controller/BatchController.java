package com.skolarli.lmsservice.controller;

import com.skolarli.lmsservice.exception.OperationNotSupportedException;
import com.skolarli.lmsservice.models.NewBatchRequest;
import com.skolarli.lmsservice.models.db.Batch;
import com.skolarli.lmsservice.models.db.BatchSchedule;
import com.skolarli.lmsservice.services.BatchService;
import com.skolarli.lmsservice.services.CourseService;
import com.skolarli.lmsservice.services.LmsUserService;
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
@RequestMapping("/batch")
public class BatchController {
    Logger logger = LoggerFactory.getLogger(BatchController.class);
    final BatchService batchService;
    final CourseService courseService;
    final LmsUserService lmsUserService;
    final UserUtils userUtils;

    public BatchController(BatchService batchService, CourseService courseService, LmsUserService lmsUserService, UserUtils userUtils) {
        this.batchService = batchService;
        this.courseService = courseService;
        this.lmsUserService = lmsUserService;
        this.userUtils = userUtils;
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<Batch>> getAllBatches() {
        try {
        return new ResponseEntity<>(batchService.getAllBatches(), HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error in getAllBatches: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @RequestMapping(method = RequestMethod.GET, value = "{id}")
    public ResponseEntity<Batch> getBatch(@PathVariable long id) {
        try {
            return new ResponseEntity<>(batchService.getBatch(id), HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error in getBatch: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @RequestMapping(method = RequestMethod.GET, value = "getSchedules/{id}")
    public ResponseEntity<List<BatchSchedule>> getSchedules(@PathVariable long id) {
        try {
            Batch batch = batchService.getBatch(id);
            return new ResponseEntity<>(batch.getBatchSchedules(), HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error in getSchedules: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Batch> addBatch(@Valid @RequestBody NewBatchRequest batchRequest) {
        Batch batch = batchService.toBatch(batchRequest);
        logger.info("Received request for new batch for course" + batch.getCourse().getCourseName());

        try {
            return new ResponseEntity<>(batchService.saveBatch(batch), HttpStatus.CREATED);
        }catch (OperationNotSupportedException e){
            throw new ResponseStatusException( HttpStatus.FORBIDDEN, e.getMessage());
        }catch (Exception e) {
            logger.error("Error in addBatch: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @RequestMapping(method = RequestMethod.PUT, value = "{id}")
        public ResponseEntity<Batch> updateBatch(@PathVariable long id, @RequestBody NewBatchRequest batchRequest) {
        Batch batch = batchService.toBatch(batchRequest);
        logger.info("Received request for updating batch batchID: " + id );

        try {
            return new ResponseEntity<>(batchService.updateBatch(batch, id), HttpStatus.OK);
        }catch (OperationNotSupportedException e){
            throw new ResponseStatusException( HttpStatus.FORBIDDEN, e.getMessage());
        }catch (Exception e) {
            logger.error("Error in updateBatch: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "{id}")
    public ResponseEntity<String> deleteBatch(@PathVariable long id) {
        Batch batch = batchService.getBatch(id);
        logger.info("Received request for deleting batch for course" + batch.getCourse().getCourseName());

        try {
            batchService.deleteBatch(id);
            return new ResponseEntity<>("Batch Deleted !", HttpStatus.OK);
        } catch (OperationNotSupportedException e){
            throw new ResponseStatusException( HttpStatus.FORBIDDEN, e.getMessage());
        } catch (Exception e) {
            logger.error("Error in deleteBatch: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "hard/{id}")
    public ResponseEntity<String> hardDeleteBatch(@PathVariable long id) {
        Batch batch = batchService.getBatch(id);
        logger.info("Received request for deleting batch for course" + batch.getCourse().getCourseName());

        try {
            batchService.hardDeleteBatch(id);
            return new ResponseEntity<>("Batch Deleted !", HttpStatus.OK);
        } catch (OperationNotSupportedException e){
            throw new ResponseStatusException( HttpStatus.FORBIDDEN, e.getMessage());
        } catch (Exception e) {
            logger.error("Error in deleteBatch: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    
}
