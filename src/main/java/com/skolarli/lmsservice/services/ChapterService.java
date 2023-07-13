package com.skolarli.lmsservice.services;

import com.skolarli.lmsservice.models.db.Chapter;
import com.skolarli.lmsservice.models.dto.course.ChapterSortOrderRequest;
import com.skolarli.lmsservice.models.dto.course.ChapterSortOrderResponse;
import com.skolarli.lmsservice.models.dto.course.NewChapterRequest;

import java.util.List;

public interface ChapterService {
    Chapter toChapter(NewChapterRequest newChapterRequest);

    Chapter getChapterById(Long id);

    List<Chapter> getAllChapters();

    List<Chapter> getChaptersByCourseId(Long courseId);

    List<ChapterSortOrderResponse> getChaptersSortOrder(Long courseId);

    Chapter saveChapter(Chapter chapter);

    Chapter updateChapter(NewChapterRequest request, long id);

    List<ChapterSortOrderResponse> updateChaptersSortOrder(
            Long courseId,
            List<ChapterSortOrderRequest> chaptersSortOrder);

    void softDeleteChapter(long id);

    void hardDeleteChapter(long id);

}