package com.skolarli.lmsservice.controller;

import com.skolarli.lmsservice.models.db.Course;
import com.skolarli.lmsservice.services.CourseService;
import com.skolarli.lmsservice.services.LmsUserService;
import com.skolarli.lmsservice.utils.UserUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/courses")
public class CourseController {

    Logger logger = LoggerFactory.getLogger(CourseController.class);

    @Autowired
    private CourseService courseService;

    @Autowired
    private LmsUserService lmsUserService;
    @Autowired
    private UserUtils userUtils;

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<Course>> getAllCourses() {
       try {
           return new ResponseEntity<List<Course>>(courseService.getAllCourses(), HttpStatus.OK);
       } catch (Exception e) {
           throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
       }
    }

    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public ResponseEntity<Course> getCourse(@PathVariable long id) {
       try {
           return new ResponseEntity<Course>(courseService.getCourseById(id), HttpStatus.OK);
       } catch (Exception e) {
           throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
       }
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Course> addCourse(@Valid  @RequestBody Course course) {
       logger.info("Received request for new course courseName: " + course.getCourseName());
       course.setCourseDeleted(false);
       
       try {
           return new ResponseEntity<Course>(courseService.saveCourse(course), HttpStatus.CREATED);
       } catch (Exception e) {
           throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
       }
    }

    @RequestMapping(value = "{id}", method = RequestMethod.PUT)
    public ResponseEntity<Course> updateCourse(@PathVariable long id, @RequestBody Course course) {
        try {
            return new ResponseEntity<Course>(courseService.updateCourse(course, id), HttpStatus.OK);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

   @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteCourse(@PathVariable long id) {
         try {
              courseService.deleteCourse(id);
         } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
        return new ResponseEntity<String> ("Course Deleted!", HttpStatus.OK);
    }

    @RequestMapping(value = "hard/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<String> hardDeleteCourse(@PathVariable long id) {
         try {
              courseService.hardDeleteCourse(id);
         } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
        return new ResponseEntity<String> ("Course Deleted!", HttpStatus.OK);
    }
}
