package com.skolarli.lmsservice.controller.course;

import com.skolarli.lmsservice.exception.OperationNotSupportedException;
import com.skolarli.lmsservice.models.db.course.Course;
import com.skolarli.lmsservice.models.dto.course.CourseResponse;
import com.skolarli.lmsservice.services.course.CourseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.validation.Valid;

@RestController
@RequestMapping(value = "/courses")
public class CourseController {

    final Logger logger = LoggerFactory.getLogger(CourseController.class);

    @Autowired
    private CourseService courseService;

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<?> getAllCourses(@RequestParam(required = false) Boolean condensed) {

        UUID uuid = UUID.randomUUID();
        MDC.put("requestId", uuid.toString());
        logger.info("Received request for getAllCourses");

        try {
            List<Course> response = courseService.getAllCourses();
            if (condensed != null && condensed == Boolean.TRUE) {
                List<CourseResponse> condensedResponse = new ArrayList<>();
                for (Course course : response) {
                    condensedResponse.add(new CourseResponse(course));
                }
                return new ResponseEntity<>(condensedResponse, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(response, HttpStatus.OK);
            }
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        } finally {
            MDC.remove("requestId");
        }
    }

    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getCourse(@PathVariable long id,
                                       @RequestParam(required = false) Boolean condense) {

        UUID uuid = UUID.randomUUID();
        MDC.put("requestId", uuid.toString());
        logger.info("Received request for getCourse with id: " + id);

        try {
            Course response = courseService.getCourseById(id);
            if (condense != null && condense == Boolean.TRUE) {
                return new ResponseEntity<>(new CourseResponse(response), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(response, HttpStatus.OK);
            }
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        } finally {
            MDC.remove("requestId");
        }
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Course> addCourse(@Valid @RequestBody Course course) {

        UUID uuid = UUID.randomUUID();
        MDC.put("requestId", uuid.toString());
        logger.info("Received request for new course courseName: " + course.getCourseName());

        course.setCourseDeleted(false);

        try {
            return new ResponseEntity<>(courseService.saveCourse(course), HttpStatus.CREATED);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        } finally {
            MDC.remove("requestId");
        }
    }

    @RequestMapping(value = "{id}", method = RequestMethod.PUT)
    public ResponseEntity<?> updateCourse(@PathVariable long id, @RequestBody Course course,
                                               @RequestParam(required = false) Boolean condense) {

        UUID uuid = UUID.randomUUID();
        MDC.put("requestId", uuid.toString());
        logger.info("Received request for updateCourse with id: " + id);

        try {
            Course response = courseService.updateCourse(course, id);
            if (condense != null && condense == Boolean.TRUE) {
                return new ResponseEntity<>(new CourseResponse(response), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(response, HttpStatus.OK);
            }
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        } finally {
            MDC.remove("requestId");
        }
    }

    @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    public ResponseEntity<String> softDeleteCourse(@PathVariable long id) {

        UUID uuid = UUID.randomUUID();
        MDC.put("requestId", uuid.toString());
        logger.info("Received request for softDeleteCourse with id: " + id);

        try {
            courseService.softDeleteCourse(id);
        } catch (OperationNotSupportedException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        } finally {
            MDC.remove("requestId");
        }
        return new ResponseEntity<String>("Course Deleted!", HttpStatus.OK);
    }

    @RequestMapping(value = "harddelete/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<String> hardDeleteCourse(@PathVariable long id) {

        UUID uuid = UUID.randomUUID();
        MDC.put("requestId", uuid.toString());
        logger.info("Received request for hardDeleteCourse with id: " + id);

        try {
            courseService.hardDeleteCourse(id);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        } finally {
            MDC.remove("requestId");
        }
        return new ResponseEntity<String>("Course Deleted!", HttpStatus.OK);
    }
}
