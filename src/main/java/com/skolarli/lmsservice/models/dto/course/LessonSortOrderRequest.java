package com.skolarli.lmsservice.models.dto.course;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class LessonSortOrderRequest {
    @NotNull
    private long lessonId;
    @NotNull
    private Integer lessonSortOrder;
}