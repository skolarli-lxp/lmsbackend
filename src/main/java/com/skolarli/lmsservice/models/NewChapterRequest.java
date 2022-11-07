package com.skolarli.lmsservice.models;

import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.skolarli.lmsservice.services.CourseService;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class NewChapterRequest {
    @JsonIgnore
    @Autowired
    private CourseService courseService;

    @NotNull(message = "chapterName cannot be empty")
    private String chapterName;

    private String chapterDescription;

    @NotNull(message = "courseId cannot be empty")
    private long courseId;
}