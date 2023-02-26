package com.skolarli.lmsservice.models;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class LessonSortOrderrequest {
    @NotNull
    private long lessonId;
    private String lessonName;
    @NotNull
    private Integer lessonSortOrder;
}