package com.skolarli.lmsservice.models.dto.course;

import com.skolarli.lmsservice.models.db.course.BatchSchedule;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BatchScheduleResponse {
    private long id;

    private Long batchId;

    private String title;

    private String description;

    private String meetingLink;

    private String resourceFileUrl;

    private String trainerInstructionsText;

    private Instant startDateTime;
    private Instant endDateTime;

    private Boolean batchScheduleIsDeleted;

    public BatchScheduleResponse(BatchSchedule batchSchedule) {
        this.id = batchSchedule.getId();
        if (batchSchedule.getBatch() != null) {
            this.batchId = batchSchedule.getBatch().getId();
        }
        this.title = batchSchedule.getTitle();
        this.description = batchSchedule.getDescription();
        this.meetingLink = batchSchedule.getMeetingLink();
        this.resourceFileUrl = batchSchedule.getResourceFileUrl();
        this.trainerInstructionsText = batchSchedule.getTrainerInstructionsText();
        this.startDateTime = batchSchedule.getStartDateTime();
        this.endDateTime = batchSchedule.getEndDateTime();
        this.batchScheduleIsDeleted = batchSchedule.getBatchScheduleIsDeleted();
    }
}

