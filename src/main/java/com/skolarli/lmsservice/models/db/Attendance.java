package com.skolarli.lmsservice.models.db;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.Date;
import javax.persistence.*;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "attendance", uniqueConstraints = @UniqueConstraint(
        name = "dupattendance", columnNames = {"student_id", "schedule_id"}))
@Where(clause = "attendance_is_deleted is null or attendance_is_deleted = false")
public class Attendance extends Tenantable {
    private static final Logger logger = LoggerFactory.getLogger(Attendance.class);
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "schedule_id")
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    @JsonIdentityReference(alwaysAsId = true)
    private BatchSchedule batchSchedule;

    @ManyToOne
    @JoinColumn(name = "student_id")
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    @JsonIdentityReference(alwaysAsId = true)
    private LmsUser student;

    private Boolean attended;
    private Instant startDateTime;
    private Instant endDateTime;
    private Boolean attendanceIsDeleted;

    public Attendance(BatchSchedule batchSchedule, LmsUser student,
                      Boolean attended, Instant startDateTime, Instant endDateTime) {
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
