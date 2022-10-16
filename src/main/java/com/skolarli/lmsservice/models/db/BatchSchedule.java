package com.skolarli.lmsservice.models.db;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name="batch_schedules")
public class BatchSchedule extends Tenantable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "batch_id")
    @JsonIgnoreProperties("batchSchedules")
    private Batch batch;

    private Date startDateTime;
    private Date endDateTime;

    @OneToMany(mappedBy = "batchSchedule")
    @JsonIgnoreProperties("batchSchedule")
    private List<Attendance> attendanceList;
}

