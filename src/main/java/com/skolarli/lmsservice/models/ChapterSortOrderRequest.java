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
public class ChapterSortOrderRequest {
    @NotNull
    private long chapterId;
    @NotNull
    private Integer chapterSortOrder;
}