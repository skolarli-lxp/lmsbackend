package com.skolarli.lmsservice.controller;

import com.skolarli.lmsservice.exception.OperationNotSupportedException;
import com.skolarli.lmsservice.models.db.Lesson;
import com.skolarli.lmsservice.models.dto.*;
import com.skolarli.lmsservice.services.LessonService;
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
@RequestMapping("/lesson")
public class LessonController {
    final Logger logger = LoggerFactory.getLogger(LessonController.class);

    final LessonService lessonService;

    public LessonController(LessonService lessonService) {
        this.lessonService = lessonService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<Lesson>> getAllLessons() {

        UUID uuid = UUID.randomUUID();
        MDC.put("requestId", uuid.toString());
        logger.info("Received request for getAllLessons");

        try {
            return new ResponseEntity<>(lessonService.getAllLessons(), HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error in getAllLessons: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        } finally {
            MDC.remove("requestId");
        }
    }

    @RequestMapping(method = RequestMethod.GET, value = "{id}")
    public ResponseEntity<Lesson> getLesson(@PathVariable long id) {

        UUID uuid = UUID.randomUUID();
        MDC.put("requestId", uuid.toString());
        logger.info("Received request for getLesson with id: " + id);

        try {
            return new ResponseEntity<>(lessonService.getLessonById(id), HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error in getLesson: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        } finally {
            MDC.remove("requestId");
        }
    }

    @RequestMapping(method = RequestMethod.GET, value = "chapter/{id}")
    public ResponseEntity<List<Lesson>> getLessonByChapterId(@PathVariable long id) {

        UUID uuid = UUID.randomUUID();
        MDC.put("requestId", uuid.toString());
        logger.info("Received request for getLesson for chapterId: " + id);

        try {
            return new ResponseEntity<>(lessonService.getLessonsByChapterId(id), HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error in getLesson: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        } finally {
            MDC.remove("requestId");
        }
    }

    @RequestMapping(method = RequestMethod.GET, value = "getsortorder/{chapterId}")
    public ResponseEntity<List<LessonSortOrderResponse>> getAllLessonsSortOrder(
            @PathVariable long chapterId) {

        UUID uuid = UUID.randomUUID();
        MDC.put("requestId", uuid.toString());
        logger.info("Received request for getAllLessonsSortOrder for chapterId: " + chapterId);

        try {
            return new ResponseEntity<>(lessonService.getAllLessonsSortOrder(chapterId),
                    HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error in getAllLessonsSortOrder: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        } finally {
            MDC.remove("requestId");
        }
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Lesson> addLesson(@RequestBody NewLessonRequest newLessonRequest) {

        UUID uuid = UUID.randomUUID();
        MDC.put("requestId", uuid.toString());
        logger.info("Received request for addLesson for chapter: "
                + newLessonRequest.getChapterId());

        Lesson lesson = lessonService.toLesson(newLessonRequest);
        try {
            return new ResponseEntity<>(lessonService.saveLesson(lesson), HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error in addLesson: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        } finally {
            MDC.remove("requestId");
        }
    }

    @Deprecated
    @RequestMapping(method = RequestMethod.PUT, value = "{id}")
    public ResponseEntity<Lesson> updateLesson(@PathVariable long id,
                                               @RequestBody LessonUpdateRequest updateRequest) {

        UUID uuid = UUID.randomUUID();
        MDC.put("requestId", uuid.toString());
        logger.info("Received request for updateLesson: " + id);

        try {
            return new ResponseEntity<>(lessonService.updateLesson(updateRequest, id),
                    HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("Error in updateLesson: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        } finally {
            MDC.remove("requestId");
        }
    }

    @RequestMapping(method = RequestMethod.PUT, value = "updatedescription/{id}")
    public ResponseEntity<UpdateLessonDescriptionRequest> updateLessonDescription(
            @PathVariable long id,
            @RequestBody UpdateLessonDescriptionRequest updateLessonDescriptionRequest) {

        UUID uuid = UUID.randomUUID();
        MDC.put("requestId", uuid.toString());
        logger.info("Received request for updateLessonDescription: " + id);

        Lesson lesson = new Lesson();
        lesson.setLessonDescription(updateLessonDescriptionRequest.getLessonDescription());
        try {
            Lesson updatedLesson = lessonService.updateLesson(lesson, id);
            UpdateLessonDescriptionRequest response = new UpdateLessonDescriptionRequest(
                    updatedLesson.getLessonDescription());
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error in updateLesson: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        } finally {
            MDC.remove("requestId");
        }
    }

    @RequestMapping(method = RequestMethod.PUT, value = "updatesortorder/{chapterId}")
    public ResponseEntity<List<LessonSortOrderResponse>> updateLessonSortOrder(
            @PathVariable long chapterId,
            @Valid @RequestBody List<LessonSortOrderRequest> lessonSortOrderrequest) {

        UUID uuid = UUID.randomUUID();
        MDC.put("requestId", uuid.toString());
        logger.info("Received request for updateLessonSortOrder for chapterId: " + chapterId);

        try {
            return new ResponseEntity<>(lessonService.updateLessonSortOrder(chapterId,
                    lessonSortOrderrequest), HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error in updateLessonSortOrder: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    e.getMessage());
        } finally {
            MDC.remove("requestId");
        }
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "{id}")
    public ResponseEntity<Lesson> hardDeleteLesson(@PathVariable long id) {

        UUID uuid = UUID.randomUUID();
        MDC.put("requestId", uuid.toString());
        logger.info("Received request for hardDeleteLesson: " + id);

        try {
            lessonService.hardDeleteLesson(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (OperationNotSupportedException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        } catch (Exception e) {
            logger.error("Error in hardDeleteLesson: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    e.getMessage());
        } finally {
            MDC.remove("requestId");
        }
    }
}