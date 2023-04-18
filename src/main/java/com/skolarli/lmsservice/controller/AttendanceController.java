package com.skolarli.lmsservice.controller;

import com.skolarli.lmsservice.exception.OperationNotSupportedException;
import com.skolarli.lmsservice.models.NewAttendanceRequest;
import com.skolarli.lmsservice.models.NewAttendancesForScheduleRequest;
import com.skolarli.lmsservice.models.db.Attendance;
import com.skolarli.lmsservice.services.AttendanceService;
import com.skolarli.lmsservice.services.BatchScheduleService;
import com.skolarli.lmsservice.services.LmsUserService;
import com.skolarli.lmsservice.utils.UserUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/attendance")
public class AttendanceController {
    Logger logger = LoggerFactory.getLogger(AttendanceController.class);
    final
    AttendanceService attendanceService;
    final BatchScheduleService batchScheduleService;
    final LmsUserService lmsUserService;
    final UserUtils userUtils;

    public AttendanceController(AttendanceService attendanceService, BatchScheduleService batchScheduleService,
                                LmsUserService lmsUserService, UserUtils userUtils) {
        this.attendanceService = attendanceService;
        this.batchScheduleService = batchScheduleService;
        this.lmsUserService = lmsUserService;
        this.userUtils = userUtils;
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<Attendance>> getAllAttendances(@RequestParam(required = false) Long batchScheduleId) {
        if (batchScheduleId != null) {
            try {
                return new ResponseEntity<>(attendanceService.getAttendanceForSchedule(batchScheduleId), HttpStatus.OK);
            } catch (Exception e) {
                logger.error("Error in getAllAttendances: " + e.getMessage());
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
            }
        }
        try {
            return new ResponseEntity<>(attendanceService.getAllAttendance(), HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error in getAllAttendances: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @RequestMapping(method = RequestMethod.GET, value = "{id}")
    public ResponseEntity<Attendance> getAttendance(@PathVariable long id) {
        try {
            return new ResponseEntity<>(attendanceService.getAttendance(id), HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error in getAttendance: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Attendance> addAttendance(@Valid @RequestBody NewAttendanceRequest request) {
        Attendance attendance = attendanceService.toAttendance(request);
        try {
            return new ResponseEntity<>(attendanceService.saveAttendance(attendance), HttpStatus.OK);
        } catch (DataIntegrityViolationException e){
            logger.error("Error in addAttendance: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Attendance already exists");
        } catch (Exception e) {
            logger.error("Error in addAttendance: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @RequestMapping(method = RequestMethod.POST, value = "/forschedule")
    public ResponseEntity<List<Attendance>> addAttendancesForSchedule(@Valid @RequestBody List<NewAttendancesForScheduleRequest> request,
                                                                      @RequestParam Long batchScheduleId) {
        List<Attendance> attendances = attendanceService.toAttendances(request, batchScheduleId);
        try {
            return new ResponseEntity<>(attendanceService.saveAllAttendance(attendances), HttpStatus.OK);
        } catch(DataIntegrityViolationException e){
            logger.error("Error in addAttendancesForSchedule: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Attendance already exists");
        } catch (Exception e) {
            logger.error("Error in addAttendancesForSchedule: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @RequestMapping(method = RequestMethod.PUT, value = "{id}")
    public ResponseEntity<Attendance> updateAttendance(@PathVariable long id, @Valid @RequestBody NewAttendanceRequest request) {
        Attendance attendance = attendanceService.toAttendance(request);
        try {
            return new ResponseEntity<>(attendanceService.updateAttendance(attendance, id), HttpStatus.OK);
        }catch (OperationNotSupportedException e) {
            throw new ResponseStatusException( HttpStatus.FORBIDDEN, e.getMessage());
        } catch (Exception e) {
            logger.error("Error in updateAttendance: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "{id}")
    public ResponseEntity<String> deleteAttendance(@PathVariable long id) {
        try {
            attendanceService.deleteAttendance(id);
            return new ResponseEntity<>("Attendance Deleted!",HttpStatus.OK);
        }catch (OperationNotSupportedException e) {
            throw new ResponseStatusException( HttpStatus.FORBIDDEN, e.getMessage());
        }catch (Exception e) {
            logger.error("Error in deleteAttendance: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "hard/{id}")
    public ResponseEntity<String> hardDeleteAttendance(@PathVariable long id) {
        try {
            attendanceService.hardDeleteAttendance(id);
            return new ResponseEntity<>("Attendance Deleted!",HttpStatus.OK);
        }catch (OperationNotSupportedException e) {
            throw new ResponseStatusException( HttpStatus.FORBIDDEN, e.getMessage());
        }catch (Exception e) {
            logger.error("Error in deleteAttendance: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
