package com.skolarli.lmsservice.models.dto.course;

import com.skolarli.lmsservice.models.BatchScheduleStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.ZonedDateTime;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NewBatchSchedulesForBatchRequest {
    @NotNull(message = "startDateTime cannot be empty")
    private ZonedDateTime startDateTime;
    @NotNull(message = "endDateTime cannot be empty")
    private ZonedDateTime endDateTime;

    private String meetingLink;
    private String resourceFileUrl;
    private String trainerInstructionsText;

    private String title;
    private String description;
    private BatchScheduleStatus status;
}
