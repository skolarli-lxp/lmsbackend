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
public class NewAttendancesForScheduleRequest {
    @NotNull(message = "studentId cannot be empty")
    private long studentId;
    @NotNull(message = "attended cannot be empty")
    @Getter
    private Boolean attended;

    private ZonedDateTime startDateTime;
    private ZonedDateTime endDateTime;
}