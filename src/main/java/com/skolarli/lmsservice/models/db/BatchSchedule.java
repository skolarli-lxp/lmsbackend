package com.skolarli.lmsservice.models.db;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name="batch_schedules")
public class BatchSchedule extends Tenantable {
    public static final Logger logger = LoggerFactory.getLogger(BatchSchedule.class);
    
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
    @JsonIgnoreProperties({"batchSchedule", "student"})
    private List<Attendance> attendanceList;

    private Boolean batchScheduleIsDeleted;

    public void update(BatchSchedule newBatchSchedule) {
        if(newBatchSchedule.getId() != 0) {
            logger.error("Cannot update id");
        }
        if (newBatchSchedule.getBatch() != null) {
            this.batch = newBatchSchedule.getBatch();
        }
        if(newBatchSchedule.getStartDateTime() != null) {
            this.startDateTime = newBatchSchedule.getStartDateTime();
        }
        if(newBatchSchedule.getEndDateTime() != null) {
            this.endDateTime = newBatchSchedule.getEndDateTime();
        }
        if(newBatchSchedule.getAttendanceList() != null && !newBatchSchedule.getAttendanceList().isEmpty()) {
            newBatchSchedule.getAttendanceList().forEach(attendance -> {
                if (!this.getAttendanceList().contains(attendance)) 
                    this.getAttendanceList().add(attendance);
            });
        }
    }
}

