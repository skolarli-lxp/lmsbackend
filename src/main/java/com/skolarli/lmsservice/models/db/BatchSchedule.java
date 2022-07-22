package com.skolarli.lmsservice.models.db;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.w3c.dom.stylesheets.LinkStyle;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Data
@Entity
@Table(name="attendance")
public class BatchSchedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "batch_id")
    @JsonIgnoreProperties("batchSchedules")
    private Batch batch;

    private Date startDateTime;
    private Date endDateTime;

    @OneToMany
    @JsonIgnoreProperties("batchSchedule")
    private List<Attendance> attendanceList;
}

