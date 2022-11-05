package com.skolarli.lmsservice.services.impl;

import com.skolarli.lmsservice.exception.OperationNotSupportedException;
import com.skolarli.lmsservice.exception.ResourceNotFoundException;
import com.skolarli.lmsservice.models.db.Attendance;
import com.skolarli.lmsservice.models.db.LmsUser;
import com.skolarli.lmsservice.repository.AttendanceRepository;
import com.skolarli.lmsservice.services.AttendanceService;
import com.skolarli.lmsservice.utils.UserUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class AttendanceServiceImpl implements AttendanceService {
    Logger logger = LoggerFactory.getLogger(AttendanceServiceImpl.class);

    final AttendanceRepository attendanceRepository;
    final UserUtils userUtils;

    public AttendanceServiceImpl(AttendanceRepository attendanceRepository, UserUtils userUtils) {
        this.attendanceRepository = attendanceRepository;
        this.userUtils = userUtils;
    }

    @Override
    public Attendance saveAttendance(Attendance attendance) {
        if (checkPermission(attendance ) == false) {
            throw new OperationNotSupportedException("Operation not supported");
        }
        return attendanceRepository.save(attendance);
    }
    
    private Boolean checkPermission(Attendance attendance) {
        LmsUser currentUser = userUtils.getCurrentUser();
        if (currentUser.getIsAdmin() != true && currentUser != attendance.getBatchSchedule().getBatch().getCourse().getCourseOwner()) {
            return false;
        }
        return true;
    }

    @Override
    public Attendance updateAttendance(Attendance newAttendance, long id) {
        Attendance existingAttendance = attendanceRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Attendance", "Id", id));
        if (checkPermission(existingAttendance) == false) {
            throw new OperationNotSupportedException("User does not have permission to update this attendance");
        }
        if (newAttendance.getAttendanceIsDeleted() != null) {
            logger.error("Cannot change deleted status. Use delete APIs instead");
            newAttendance.setAttendanceIsDeleted(null);
        }
        existingAttendance.updateAttendance(newAttendance);
        return attendanceRepository.save(existingAttendance);
    }

    @Override
    public Attendance getAttendance(long id) {
        return attendanceRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Attendance", "Id", id));
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
    public void deleteAttendance(long id) {
        Attendance attendance = attendanceRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Attendance", "Id", id));
        if (checkPermission(attendance) == false) {
            throw new OperationNotSupportedException("User does not have permission to delete this attendance");
        }
        attendance.setAttendanceIsDeleted(true);
        attendanceRepository.save(attendance);
    }

    @Override
    public void hardDeleteAttendance(long id) {
        Attendance attendance = attendanceRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Attendance", "Id", id));
        if (checkPermission(attendance) == false) {
            throw new OperationNotSupportedException("User does not have permission to delete this attendance");
        }
        attendanceRepository.delete(attendance);
    }
}
