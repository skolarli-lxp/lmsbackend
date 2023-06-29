package com.skolarli.lmsservice.models.dto.course;

import lombok.*;

import javax.validation.constraints.NotNull;

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

    private int chapterSortOrder;
}