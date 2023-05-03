package com.skolarli.lmsservice.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NewBatchSchedulesForBatchRequest {
    @NotNull(message = "startDateTime cannot be empty")
    private Date startDateTime;
    @NotNull(message = "endDateTime cannot be empty")
    private Date endDateTime;

    private String meetingLink;

    private String title;
    private String description;
}
