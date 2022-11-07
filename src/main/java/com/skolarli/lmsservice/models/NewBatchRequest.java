package com.skolarli.lmsservice.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.skolarli.lmsservice.models.db.Batch;
import com.skolarli.lmsservice.services.CourseService;
import com.skolarli.lmsservice.services.LmsUserService;

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
