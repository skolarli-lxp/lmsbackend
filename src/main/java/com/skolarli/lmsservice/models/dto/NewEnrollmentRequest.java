package com.skolarli.lmsservice.models.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NewEnrollmentRequest {
    @NotNull(message = "batchId cannot be empty")
    private long batchId;
    @NotNull(message = "studentId cannot be empty")
    private long studentId;
}
