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
public class ChapterSortOrderResponse {
    @NotNull
    private long chapterId;
    private String chapterName;
    @NotNull
    private Integer chapterSortOrder;
}