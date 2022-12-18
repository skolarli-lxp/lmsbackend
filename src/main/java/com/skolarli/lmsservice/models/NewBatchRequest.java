package com.skolarli.lmsservice.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NewBatchRequest {
    
    @NotNull(message = "courseId cannot be empty")
    private long courseId;
    @NotNull(message = "instructorId cannot be empty")
    private long instructorId;

}
