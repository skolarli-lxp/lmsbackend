package com.skolarli.lmsservice.controller;

import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.skolarli.lmsservice.models.db.Lesson;
import com.skolarli.lmsservice.models.LessonSortOrderrequest;
import com.skolarli.lmsservice.models.NewLessonRequest;
import com.skolarli.lmsservice.models.UpdateLessonDescriptionRequest;
import com.skolarli.lmsservice.services.LessonService;

@RestController
@RequestMapping("/lesson")
public class LessonController {
    Logger logger = LoggerFactory.getLogger(LessonController.class);

    LessonService lessonService;

    public LessonController(LessonService lessonService) {
        this.lessonService = lessonService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<Lesson>> getAllLessons() {
        try {
            return new ResponseEntity<>(lessonService.getAllLessons(), HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error in getAllLessons: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @RequestMapping(method = RequestMethod.GET, value = "{id}")
    public ResponseEntity<Lesson> getLesson(@PathVariable long id) {
        try {
            return new ResponseEntity<>(lessonService.getLessonById(id), HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error in getLesson: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @RequestMapping(method = RequestMethod.GET, value = "chapter/{id}")
    public ResponseEntity<List<Lesson>> getLessonByChapterId(@PathVariable long id) {
        try {
            return new ResponseEntity<>(lessonService.getLessonsByChapterId(id), HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error in getLesson: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @RequestMapping(method = RequestMethod.GET, value = "getsortorder/{chapterId}")
    public ResponseEntity<List<LessonSortOrderrequest>> getAllLessonsSortOrder(@PathVariable long chapterId) {
        try {
            return new ResponseEntity<>(lessonService.getAllLessonsSortOrder(chapterId), HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error in getAllLessonsSortOrder: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Lesson> addLesson(@RequestBody NewLessonRequest newLessonRequest) {
        Lesson lesson = lessonService.toLesson(newLessonRequest);
        try {
            return new ResponseEntity<>(lessonService.saveLesson(lesson), HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error in addLesson: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    } 
    
    @RequestMapping(method = RequestMethod.PUT, value = "{id}")
    public ResponseEntity<Lesson> updateLesson(@PathVariable long id, @RequestBody Lesson lesson) {
        try {
            return new ResponseEntity<>(lessonService.updateLesson(lesson, id), HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error in updateLesson: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @RequestMapping(method = RequestMethod.PUT, value = "updatedescription/{id}")
    public ResponseEntity<UpdateLessonDescriptionRequest> updateLessonDescription(@PathVariable long id, 
                                                          @RequestBody UpdateLessonDescriptionRequest updateLessonDescriptionRequest) {
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
        }
    }

    @RequestMapping(method = RequestMethod.PUT, value = "updatesortorder/{chapterId}")
    public ResponseEntity<List<LessonSortOrderrequest>> updateLessonSortOrder(@PathVariable long chapterId, @Valid @RequestBody List<LessonSortOrderrequest> lessonSortOrderrequest) {
        try {
            return new ResponseEntity<>(lessonService.updateLessonSortOrder(chapterId, lessonSortOrderrequest), HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error in updateLessonSortOrder: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "{id}")
    public ResponseEntity<Lesson> deleteLesson(@PathVariable long id) {
        try {
            lessonService.deleteLesson(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error in deleteLesson: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "hard/{id}")
    public ResponseEntity<Lesson> hardDeleteLesson(@PathVariable long id) {
        try {
            lessonService.hardDeleteLesson(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error in hardDeleteLesson: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
    
}