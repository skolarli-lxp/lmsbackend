package com.skolarli.lmsservice.models.db;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name="attendance")
public class Attendance extends Tenantable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "schedule_id")
    @JsonIgnoreProperties("attendanceList")
    private BatchSchedule batchSchedule;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private LmsUser student;

    private Boolean attended;
    private Date startDateTime;
    private Date endDateTime;

    public Attendance(BatchSchedule batchSchedule, LmsUser student, Boolean attended, Date startDateTime, Date endDateTime) {
        this.batchSchedule = batchSchedule;
        this.student = student;
        this.attended = attended;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
    }
}
