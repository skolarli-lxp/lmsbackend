package com.skolarli.lmsservice.models.db;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import org.hibernate.annotations.Where;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name="attendance")
@Where(clause = "attendance_is_deleted is null or attendance_is_deleted = false")
public class Attendance extends Tenantable{
    private static final Logger logger = LoggerFactory.getLogger(Attendance.class);
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "schedule_id")
    @JsonIgnoreProperties("attendanceList")
    private BatchSchedule batchSchedule;

    @ManyToOne
    @JoinColumn(name = "student_id")
    @JsonIgnoreProperties("attendanceList")
    private LmsUser student;

    private Boolean attended;
    private Date startDateTime;
    private Date endDateTime;
    private Boolean attendanceIsDeleted;

    public Attendance(BatchSchedule batchSchedule, LmsUser student, Boolean attended, Date startDateTime, Date endDateTime) {
        this.batchSchedule = batchSchedule;
        this.student = student;
        this.attended = attended;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
    }

    public void updateAttendance(Attendance newAttendance) {
        if (newAttendance.getId() != 0) {
            logger.error("Cannot update id");
        }
        if (newAttendance.getBatchSchedule() != null) {
            this.batchSchedule = newAttendance.getBatchSchedule();
        }
        if (newAttendance.getStudent() != null) {
            this.student = newAttendance.getStudent();
        }
        if (newAttendance.getAttended() != null) {
            this.attended = newAttendance.getAttended();
        }
        if (newAttendance.getStartDateTime() != null) {
            this.startDateTime = newAttendance.getStartDateTime();
        }
        if (newAttendance.getEndDateTime() != null) {
            this.endDateTime = newAttendance.getEndDateTime();
        }
        if (newAttendance.getAttendanceIsDeleted() != null) {
            this.attendanceIsDeleted = newAttendance.getAttendanceIsDeleted();
        }
    }
}
