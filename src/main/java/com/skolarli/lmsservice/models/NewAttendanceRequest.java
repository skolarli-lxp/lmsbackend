package com.skolarli.lmsservice.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.skolarli.lmsservice.models.db.Attendance;
import com.skolarli.lmsservice.models.db.BatchSchedule;
import com.skolarli.lmsservice.models.db.LmsUser;
import com.skolarli.lmsservice.services.BatchScheduleService;
import com.skolarli.lmsservice.services.LmsUserService;
import com.skolarli.lmsservice.services.impl.LmsUserServiceImpl;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NewAttendanceRequest {
    @NotNull(message = "batchScheduleId cannot be empty")
    private long batchScheduleId;
    @NotNull(message = "studentId cannot be empty")
    private long studentId;
    @NotNull(message = "attended cannot be empty")
    @Getter
    private Boolean attended;

    private Date startDateTime;
    private Date endDateTime;

    @JsonIgnore
    @Autowired
    BatchScheduleService batchScheduleService;

    @JsonIgnore
    @Autowired
    LmsUserService lmsUserService;

    public Attendance toAttendance() {
        BatchSchedule batchSchedule = batchScheduleService.getBatchSchedule(batchScheduleId);
        LmsUser student = lmsUserService.getLmsUserById(studentId);
        return new Attendance(
                batchSchedule,
                student,
                attended,
                startDateTime,
                endDateTime
        );
    }

    public Attendance updateAttendance(Attendance attendance) {
        attendance.setAttended(attended);
        attendance.setStartDateTime(startDateTime);
        attendance.setEndDateTime(endDateTime);
        return attendance;
    }

}