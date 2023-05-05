package com.skolarli.lmsservice.services;

import com.skolarli.lmsservice.models.db.Attendance;
import com.skolarli.lmsservice.models.dto.NewAttendanceRequest;
import com.skolarli.lmsservice.models.dto.NewAttendancesForScheduleRequest;

import java.util.List;

public interface AttendanceService {
    Attendance toAttendance(NewAttendanceRequest newAttendanceRequest);

    List<Attendance> toAttendances(
            List<NewAttendancesForScheduleRequest> newAttendancesForScheduleRequests,
            Long batchScheduleId);

    //READ
    Attendance getAttendance(long id);

    List<Attendance> getAllAttendance();

    List<Attendance> getAttendanceForSchedule(long batchScheduleId);

    //CREATE
    Attendance saveAttendance(Attendance attendance);

    List<Attendance> saveAllAttendance(List<Attendance> attendance);

    //UPDATE
    Attendance updateAttendance(Attendance attendance, long id);

    //DELETE
    void deleteAttendance(long id);

    void hardDeleteAttendance(long id);
}
