package com.skolarli.lmsservice.services.course;

import com.skolarli.lmsservice.models.db.course.Attendance;
import com.skolarli.lmsservice.models.dto.course.NewAttendanceRequest;
import com.skolarli.lmsservice.models.dto.course.NewAttendancesForScheduleRequest;

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

    List<Attendance> createOrUpdateAllAttendances(List<NewAttendancesForScheduleRequest>
                                                          newAttendanceRequests,
                                                  Long batchScheduleId);

    //DELETE
    void hardDeleteAttendance(long id);
}
