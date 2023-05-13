package com.skolarli.lmsservice.models.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.ZonedDateTime;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NewAttendanceRequest {
    @NotNull(message = "batchScheduleId cannot be empty")
    private long batchScheduleId;
    @Positive(message = "studentId cannot be empty")
    private long studentId;
    @NotNull(message = "attended cannot be empty")
    @Getter
    private Boolean attended;

    private ZonedDateTime startDateTime;
    private ZonedDateTime endDateTime;
}