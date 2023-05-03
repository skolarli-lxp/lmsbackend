package com.skolarli.lmsservice.models.db;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.List;
import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "batch_schedules")
@Where(clause = "batch_schedule_is_deleted is null or batch_schedule_is_deleted = false")
public class BatchSchedule extends Tenantable {
    public static final Logger logger = LoggerFactory.getLogger(BatchSchedule.class);

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "batch_id")
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    @JsonIdentityReference(alwaysAsId = true)
    private Batch batch;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(columnDefinition = "VARCHAR(1024)")
    private String meetingLink;

    private Date startDateTime;
    private Date endDateTime;

    @OneToMany(mappedBy = "batchSchedule")
    @JsonIgnoreProperties({"batchSchedule", "student"})
    private List<Attendance> attendanceList;

    private Boolean batchScheduleIsDeleted;

    public void update(BatchSchedule newBatchSchedule) {
        if (newBatchSchedule.getId() != 0) {
            logger.error("Cannot update id");
        }
        if (newBatchSchedule.getBatch() != null) {
            this.batch = newBatchSchedule.getBatch();
        }
        if (newBatchSchedule.getMeetingLink() != null) {
            this.meetingLink = newBatchSchedule.getMeetingLink();
        }
        if (newBatchSchedule.getTitle() != null) {
            this.title = newBatchSchedule.getTitle();
        }
        if (newBatchSchedule.getDescription() != null) {
            this.description = newBatchSchedule.getDescription();
        }
        if (newBatchSchedule.getStartDateTime() != null) {
            this.startDateTime = newBatchSchedule.getStartDateTime();
        }
        if (newBatchSchedule.getEndDateTime() != null) {
            this.endDateTime = newBatchSchedule.getEndDateTime();
        }
        if (newBatchSchedule.getAttendanceList() != null
                && !newBatchSchedule.getAttendanceList().isEmpty()) {
            newBatchSchedule.getAttendanceList().forEach(attendance -> {
                if (!this.getAttendanceList().contains(attendance)) {
                    this.getAttendanceList().add(attendance);
                }
            });
        }
    }
}

