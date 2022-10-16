package com.skolarli.lmsservice.services;

import java.util.List;

import com.skolarli.lmsservice.models.db.Chapter;

public interface ChapterService {
    Chapter saveChapter(Chapter chapter);
    Chapter updateChapter(Chapter chapter);
    void deleteChapter(Chapter chapter);
    Chapter getChapterById(Long id);
    List<Chapter> getAllChapters();
}