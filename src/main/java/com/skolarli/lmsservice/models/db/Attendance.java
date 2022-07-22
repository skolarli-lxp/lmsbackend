package com.skolarli.lmsservice.models.db;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name="attendance")
public class Attendance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "schedule_id")
    @JsonIgnoreProperties("attendanceList")
    private BatchSchedule batchSchedule;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private LmsUser lmsUser;

    private Boolean attended;
    private Date startDateTime;
    private Date endDateTime;
}
