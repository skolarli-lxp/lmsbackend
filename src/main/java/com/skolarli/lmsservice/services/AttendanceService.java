package com.skolarli.lmsservice.services;

import com.skolarli.lmsservice.models.db.Attendance;

import java.util.List;

public interface AttendanceService {
        Attendance saveAttendance(Attendance attendance);
        Attendance updateAttendance(Attendance attendance);
        Attendance getAttendance(long id);
        List<Attendance> getAllAttendance();
        List<Attendance> getAttendanceForSchedule(long batchScheduleId);
        void deleteAttendance(long id);
}
