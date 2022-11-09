package com.skolarli.lmsservice.models;

import javax.validation.constraints.NotNull;

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
    @NotNull(message = "chapterName cannot be empty")
    private String chapterName;

    private String chapterDescription;

    @NotNull(message = "courseId cannot be empty")
    private long courseId;
}