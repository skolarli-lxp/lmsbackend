package com.skolarli.lmsservice.services;

import java.util.List;

import com.skolarli.lmsservice.models.db.Chapter;

public interface ChapterService {
    Chapter saveChapter(Chapter chapter);
    Chapter updateChapter(Chapter chapter, long id);
    void deleteChapter(long id);
    void hardDeleteChapter(long id);
    Chapter getChapterById(Long id);
    List<Chapter> getAllChapters();
}