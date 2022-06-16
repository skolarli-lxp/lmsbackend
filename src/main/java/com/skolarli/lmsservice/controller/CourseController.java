package com.skolarli.lmsservice.controller;

import com.skolarli.lmsservice.models.db.Course;
import com.skolarli.lmsservice.services.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/courses")
public class CourseController {

    @Autowired
    private CourseService courseService;

   @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Course> saveCourse(@RequestBody Course course) {
        return new ResponseEntity<Course>(courseService.saveCourse(course), HttpStatus.CREATED);
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

    @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteCourse(@PathVariable long id) {
        courseService.deleteCourse(id);
        return new ResponseEntity<String> ("Course Deleted!", HttpStatus.OK);
    }
}
