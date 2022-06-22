package com.skolarli.lmsservice.controller;

import com.skolarli.lmsservice.models.db.Course;
import com.skolarli.lmsservice.models.db.LmsUser;
import com.skolarli.lmsservice.services.CourseService;
import com.skolarli.lmsservice.services.LmsUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

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

   @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Course> saveCourse(@Valid  @RequestBody Course course) {
       logger.info("Received request for new course courseName: " + course.getName());
       String currentUserEmail = (String)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
       LmsUser currentUser = lmsUserService.getLmsUserByEmail(currentUserEmail);
       course.setOwner(currentUser);
       Course savedCourse = courseService.saveCourse(course);

        return new ResponseEntity<Course>(savedCourse, HttpStatus.CREATED);
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<Course>> getAllCourses() {
        return new ResponseEntity<List<Course>>(courseService.getAllCourses(),HttpStatus.OK);
    }

    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public ResponseEntity<Course> getCourse(@PathVariable long id) {
        return new ResponseEntity<Course>(courseService.getCourseById(id),HttpStatus.OK);
    }

    @RequestMapping(value = "{id}", method = RequestMethod.PUT)
    public ResponseEntity<Course> updateCourse(@PathVariable long id, @RequestBody Course course) {
        return new ResponseEntity<Course>(courseService.updateCourse(course, id),HttpStatus.OK);
    }

    // Delete should do a soft delete
   @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteCourse(@PathVariable long id) {
       //TODO: Implementation
        return new ResponseEntity<String> ("Course Deleted!", HttpStatus.OK);
    }
}
