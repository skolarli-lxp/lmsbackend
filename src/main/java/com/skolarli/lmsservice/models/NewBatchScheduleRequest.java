package com.skolarli.lmsservice.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NewBatchScheduleRequest {
    @NotNull(message = "batchId cannot be empty")
    private long batchId;
    @NotNull(message = "startDateTime cannot be empty")
    private Date startDateTime;
    @NotNull(message = "endDateTime cannot be empty")
    private Date endDateTime;
}
