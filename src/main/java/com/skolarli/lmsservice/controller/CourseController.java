package com.skolarli.lmsservice.controller;

import com.skolarli.lmsservice.models.db.Course;
import com.skolarli.lmsservice.models.db.LmsUser;
import com.skolarli.lmsservice.services.CourseService;
import com.skolarli.lmsservice.services.LmsUserService;
import com.skolarli.lmsservice.utils.UserUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
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

   @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Course> addCourse(@Valid  @RequestBody Course course) {
       logger.info("Received request for new course courseName: " + course.getCourseName());
       String currentUserEmail = (String)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
       LmsUser currentUser = lmsUserService.getLmsUserByEmail(currentUserEmail);
       course.setCourseOwner(currentUser);
       try {
           return new ResponseEntity<Course>(courseService.saveCourse(course), HttpStatus.CREATED);
       } catch (Exception e) {
           throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
       }
    }

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

    @RequestMapping(value = "{id}", method = RequestMethod.PUT)
    public ResponseEntity<Course> updateCourse(@PathVariable long id, @RequestBody Course course) {
        LmsUser currentUser = userUtils.getCurrentUser();
        Course existingCourse = courseService.getCourseById(id);
        if (currentUser.getIsAdmin() != true && currentUser != existingCourse.getCourseOwner()) {
            throw new ResponseStatusException( HttpStatus.FORBIDDEN, "");
        }
        try {
            return new ResponseEntity<Course>(courseService.updateCourse(course, id), HttpStatus.OK);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    // Delete should do a soft delete
   @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteCourse(@PathVariable long id) {
       //TODO: Implementation
        return new ResponseEntity<String> ("Course Deleted!", HttpStatus.OK);
    }
}
