package com.skolarli.lmsservice.services.impl;

import com.skolarli.lmsservice.exception.OperationNotSupportedException;
import com.skolarli.lmsservice.exception.ResourceNotFoundException;
import com.skolarli.lmsservice.models.db.Attendance;
import com.skolarli.lmsservice.models.db.BatchSchedule;
import com.skolarli.lmsservice.models.db.LmsUser;
import com.skolarli.lmsservice.models.dto.NewAttendanceRequest;
import com.skolarli.lmsservice.models.dto.NewAttendancesForScheduleRequest;
import com.skolarli.lmsservice.repository.AttendanceRepository;
import com.skolarli.lmsservice.services.AttendanceService;
import com.skolarli.lmsservice.services.BatchScheduleService;
import com.skolarli.lmsservice.services.LmsUserService;
import com.skolarli.lmsservice.utils.UserUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class AttendanceServiceImpl implements AttendanceService {
    final AttendanceRepository attendanceRepository;
    final UserUtils userUtils;
    final LmsUserService lmsUserService;
    final BatchScheduleService batchScheduleService;
    final Logger logger = LoggerFactory.getLogger(AttendanceServiceImpl.class);

    public AttendanceServiceImpl(AttendanceRepository attendanceRepository,
                                 UserUtils userUtils,
                                 LmsUserService lmsUserService,
                                 BatchScheduleService batchScheduleService) {
        this.attendanceRepository = attendanceRepository;
        this.userUtils = userUtils;
        this.lmsUserService = lmsUserService;
        this.batchScheduleService = batchScheduleService;
    }

    private Boolean checkPermission(Attendance attendance) {
        LmsUser currentUser = userUtils.getCurrentUser();
        return currentUser.getIsAdmin() || currentUser == attendance.getBatchSchedule()
                .getBatch().getInstructor();
    }

    private void copyAttendanceDetails(NewAttendancesForScheduleRequest currentRequest,
                                       Attendance existingAttendance) {
        if (currentRequest.getAttended() != null) {
            existingAttendance.setAttended(currentRequest.getAttended());
        }
        if (currentRequest.getStartDateTime() != null) {
            existingAttendance.setStartDateTime(currentRequest.getStartDateTime()
                    .toInstant());
        }
        if (currentRequest.getEndDateTime() != null) {
            existingAttendance.setEndDateTime(currentRequest.getEndDateTime()
                    .toInstant());
        }
    }

    @Override
    public Attendance toAttendance(NewAttendanceRequest newAttendanceRequest) {
        BatchSchedule batchSchedule = batchScheduleService.getBatchSchedule(
                newAttendanceRequest.getBatchScheduleId());
        LmsUser student = lmsUserService.getLmsUserById(newAttendanceRequest.getStudentId());
        Attendance attendance = new Attendance();
        attendance.setBatchSchedule(batchSchedule);
        attendance.setStudent(student);
        attendance.setAttended(newAttendanceRequest.getAttended());
        if (newAttendanceRequest.getStartDateTime() != null) {
            attendance.setStartDateTime(newAttendanceRequest.getStartDateTime().toInstant());
        }
        if (newAttendanceRequest.getEndDateTime() != null) {
            attendance.setEndDateTime(newAttendanceRequest.getEndDateTime().toInstant());
        }
        attendance.setAttendanceIsDeleted(false);
        return attendance;
    }

    public Attendance toAttendance(
            NewAttendancesForScheduleRequest newAttendancesForScheduleRequest,
            Long batchScheduleId) {
        Attendance attendance = new Attendance();

        if (newAttendancesForScheduleRequest.getStudentId() != 0) {
            LmsUser student = lmsUserService.getLmsUserById(
                    newAttendancesForScheduleRequest.getStudentId());
            attendance.setStudent(student);
        } else {
            throw new OperationNotSupportedException("Student Id is required");
        }
        copyAttendanceDetails(newAttendancesForScheduleRequest, attendance);
        attendance.setAttendanceIsDeleted(false);
        if (batchScheduleId != 0) {
            BatchSchedule batchSchedule = batchScheduleService.getBatchSchedule(batchScheduleId);
            attendance.setBatchSchedule(batchSchedule);
        } else {
            throw new OperationNotSupportedException("Batch Schedule Id is required");
        }
        return attendance;
    }

    @Override
    public List<Attendance> toAttendances(
            List<NewAttendancesForScheduleRequest> newAttendancesForScheduleRequests,
            Long batchScheduleId) {
        return newAttendancesForScheduleRequests.stream()
                .map(newAttendanceForScheduleRequest -> toAttendance(
                        newAttendanceForScheduleRequest, batchScheduleId))
                .collect(Collectors.toList());
    }

    @Override
    public Attendance getAttendance(long id) {
        List<Attendance> existingAttendance = attendanceRepository.findAllById(
                new ArrayList<>(List.of(id)));
        if (existingAttendance.size() == 0) {
            throw new ResourceNotFoundException("Attendance", "Id", id);
        }
        return existingAttendance.get(0);
    }

    @Override
    public List<Attendance> getAllAttendance() {
        return attendanceRepository.findAll();
    }

    @Override
    public List<Attendance> getAttendanceForSchedule(long batchScheduleId) {
        return attendanceRepository.findByBatchSchedule_Id(batchScheduleId);
    }

    @Override
    public Attendance saveAttendance(Attendance attendance) {
        if (!checkPermission(attendance)) {
            throw new OperationNotSupportedException("Operation not supported");
        }
        return attendanceRepository.save(attendance);
    }

    @Override
    public List<Attendance> saveAllAttendance(List<Attendance> attendance) {
        if (!checkPermission(attendance.get(0))) {
            throw new OperationNotSupportedException("Operation not supported");
        }
        return attendanceRepository.saveAll(attendance);
    }

    @Override
    public Attendance updateAttendance(Attendance newAttendance, long id) {
        Attendance existingAttendance = attendanceRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Attendance", "Id", id));
        if (!checkPermission(existingAttendance)) {
            throw new OperationNotSupportedException(
                    "User does not have permission to update this attendance");
        }
        if (newAttendance.getAttendanceIsDeleted() != null) {
            logger.error("Cannot change deleted status. Use delete APIs instead");
            newAttendance.setAttendanceIsDeleted(null);
        }
        existingAttendance.updateAttendance(newAttendance);
        return attendanceRepository.save(existingAttendance);
    }

    @Override
    public List<Attendance> createOrUpdateAllAttendances(List<NewAttendancesForScheduleRequest>
                                                                 newAttendanceRequests,
                                                         Long batchScheduleId) {
        List<Attendance> existingAttendances = attendanceRepository
                .findByBatchSchedule_Id(batchScheduleId);
        List<Attendance> attendanceListToUpdate = new ArrayList<>();
        for (NewAttendancesForScheduleRequest currentRequest : newAttendanceRequests) {
            Boolean update = false;
            if (currentRequest.getStudentId() == 0) {
                throw new OperationNotSupportedException("Student Id is required");
            } else {
                for (Attendance existingAttendance : existingAttendances) {
                    if (existingAttendance.getStudent().getId() == currentRequest.getStudentId()) {
                        copyAttendanceDetails(currentRequest, existingAttendance);
                        attendanceListToUpdate.add(existingAttendance);
                        update = true;
                        break;
                    }
                }
                if (!update) {
                    attendanceListToUpdate.add(toAttendance(currentRequest, batchScheduleId));
                }
            }
        }
        return attendanceRepository.saveAll(attendanceListToUpdate);
    }

    @Override
    public void hardDeleteAttendance(long id) {
        Attendance attendance = attendanceRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Attendance", "Id", id));
        if (!checkPermission(attendance)) {
            throw new OperationNotSupportedException(
                    "User does not have permission to delete this attendance");
        }
        attendanceRepository.delete(attendance);
    }
}
