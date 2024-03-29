package com.skolarli.lmsservice.models.db.course;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.skolarli.lmsservice.models.BatchScheduleStatus;
import com.skolarli.lmsservice.models.db.core.Tenantable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.List;
import javax.persistence.*;
import javax.validation.constraints.NotNull;

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

    @Column(columnDefinition = "VARCHAR(25) default 'SCHEDULED'")
    @Enumerated(EnumType.STRING)
    private BatchScheduleStatus status;

    @Column(columnDefinition = "VARCHAR(1024)")
    private String meetingLink;

    @Column(columnDefinition = "VARCHAR(1024)")
    private String resourceFileUrl;

    @Column(columnDefinition = "TEXT")
    private String trainerInstructionsText;


    private Instant startDateTime;
    private Instant endDateTime;

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
        if (newBatchSchedule.getResourceFileUrl() != null) {
            this.resourceFileUrl = newBatchSchedule.getResourceFileUrl();
        }
        if (newBatchSchedule.getTrainerInstructionsText() != null) {
            this.trainerInstructionsText = newBatchSchedule.getTrainerInstructionsText();
        }
        if (newBatchSchedule.getTitle() != null) {
            this.title = newBatchSchedule.getTitle();
        }
        if (newBatchSchedule.getDescription() != null) {
            this.description = newBatchSchedule.getDescription();
        }
        if (newBatchSchedule.getStatus() != null) {
            this.status = newBatchSchedule.getStatus();
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

    public String toString() {
        return "BatchSchedule(id=" + this.getId()
            + ", batch=" + this.getBatch().getId()
            + ", title=" + this.getTitle()
            + ", description=" + this.getDescription()
            + ", status= " + this.getStatus()
            + ", meetingLink=" + this.getMeetingLink()
            + ", resourceFileUrl=" + this.getResourceFileUrl()
            + ", trainerInstructionsText=" + this.getTrainerInstructionsText()
            + ", startDateTime=" + this.getStartDateTime()
            + ", endDateTime=" + this.getEndDateTime()
            + ", attendanceList=" + this.getAttendanceList()
            + ", batchScheduleIsDeleted=" + this.getBatchScheduleIsDeleted() + ")";
    }
}

