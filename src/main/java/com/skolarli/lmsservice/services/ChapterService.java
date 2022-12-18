package com.skolarli.lmsservice.services;

import java.util.List;

import com.skolarli.lmsservice.models.NewChapterRequest;
import com.skolarli.lmsservice.models.db.Chapter;

public interface ChapterService {
    Chapter toChapter(NewChapterRequest newChapterRequest);

    Chapter getChapterById(Long id);
    List<Chapter> getAllChapters();
    public List<Chapter> getChaptersByCourseId(Long courseId);
    
    Chapter saveChapter(Chapter chapter);
    
    Chapter updateChapter(Chapter chapter, long id);

    void deleteChapter(long id);
    void hardDeleteChapter(long id);
    
}