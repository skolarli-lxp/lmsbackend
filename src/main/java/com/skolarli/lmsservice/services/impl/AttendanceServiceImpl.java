package com.skolarli.lmsservice.services.impl;

import com.skolarli.lmsservice.exception.ResourceNotFoundException;
import com.skolarli.lmsservice.models.db.Attendance;
import com.skolarli.lmsservice.repository.AttendanceRepository;
import com.skolarli.lmsservice.services.AttendanceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class AttendanceServiceImpl implements AttendanceService {
    Logger logger = LoggerFactory.getLogger(AttendanceServiceImpl.class);

    final AttendanceRepository attendanceRepository;

    public AttendanceServiceImpl(AttendanceRepository attendanceRepository) {
        this.attendanceRepository = attendanceRepository;
    }

    @Override
    public Attendance saveAttendance(Attendance attendance) {
        return attendanceRepository.save(attendance);
    }

    @Override
    public Attendance updateAttendance(Attendance attendance) {
        return null;
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
        attendanceRepository.delete(attendance);
    }
}
