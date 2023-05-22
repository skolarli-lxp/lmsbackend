package com.skolarli.lmsservice.services;

import com.skolarli.lmsservice.models.db.Chapter;
import com.skolarli.lmsservice.models.dto.ChapterSortOrderRequest;
import com.skolarli.lmsservice.models.dto.ChapterSortOrderResponse;
import com.skolarli.lmsservice.models.dto.NewChapterRequest;

import java.util.List;

public interface ChapterService {
    Chapter toChapter(NewChapterRequest newChapterRequest);

    Chapter getChapterById(Long id);

    List<Chapter> getAllChapters();

    List<Chapter> getChaptersByCourseId(Long courseId);

    List<ChapterSortOrderResponse> getChaptersSortOrder(Long courseId);

    Chapter saveChapter(Chapter chapter);

    Chapter updateChapter(Chapter chapter, long id);

    List<ChapterSortOrderResponse> updateChaptersSortOrder(
            Long courseId,
            List<ChapterSortOrderRequest> chaptersSortOrder);

    void softDeleteChapter(long id);

    void hardDeleteChapter(long id);

}