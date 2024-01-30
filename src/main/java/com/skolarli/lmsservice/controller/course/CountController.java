package com.skolarli.lmsservice.controller.course;

import com.skolarli.lmsservice.models.CourseStatus;
import com.skolarli.lmsservice.models.Role;
import com.skolarli.lmsservice.models.dto.course.CountResponse;
import com.skolarli.lmsservice.services.core.LmsUserService;
import com.skolarli.lmsservice.services.course.BatchScheduleService;
import com.skolarli.lmsservice.services.course.BatchService;
import com.skolarli.lmsservice.services.course.CourseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@RestController
@RequestMapping("/count")
public class CountController {
    final CourseService courseService;

    final BatchService batchService;

    final BatchScheduleService batchScheduleService;


    final LmsUserService lmsUserService;

    final Logger logger = LoggerFactory.getLogger(CountController.class);

    public CountController(BatchService batchService, BatchScheduleService batchScheduleService,
                           LmsUserService lmsUserService, CourseService courseService) {
        this.batchService = batchService;
        this.batchScheduleService = batchScheduleService;
        this.lmsUserService = lmsUserService;
        this.courseService = courseService;
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResponseEntity<?> getAllCounts() {
        UUID uuid = UUID.randomUUID();
        MDC.put("requestId", uuid.toString());
        logger.info("Received request for getCounts");

        CountResponse response = new CountResponse();

        try {
            response.setTotalCourses(courseService.getCourseCount(null));
            response.setPublishedCourses(courseService.getCourseCount(CourseStatus.PUBLISHED));

            response.setTotalBatches(batchService.getBatchCount());
            response.setTotalBatchSchedules(batchScheduleService.getBatchScheduleCount());

            response.setTotalStudents(lmsUserService.getLmsUserCount(Role.STUDENT));
            response.setTotalInstructors(lmsUserService.getLmsUserCount(Role.INSTRUCTOR));

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        } finally {
            MDC.remove("requestId");
        }
    }

}
