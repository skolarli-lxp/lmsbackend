package com.skolarli.lmsservice.models.dto.course;

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
public class NewBatchScheduleRequest {
    @NotNull(message = "batchId cannot be empty")
    private long batchId;
    @NotNull(message = "startDateTime cannot be empty")
    private ZonedDateTime startDateTime;
    @NotNull(message = "endDateTime cannot be empty")
    private ZonedDateTime endDateTime;

    private String meetingLink;

    private String title;
    private String description;
}
