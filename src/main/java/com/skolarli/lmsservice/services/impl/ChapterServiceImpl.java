package com.skolarli.lmsservice.services.impl;

import java.util.List;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.skolarli.lmsservice.exception.ResourceNotFoundException;
import com.skolarli.lmsservice.models.db.Chapter;
import com.skolarli.lmsservice.repository.ChapterRepository;
import com.skolarli.lmsservice.services.ChapterService;

import ch.qos.logback.classic.Logger;

@Service
public class ChapterServiceImpl implements ChapterService {

    Logger logger = (Logger) LoggerFactory.getLogger(ChapterServiceImpl.class);

    ChapterRepository chapterRepository;

    public ChapterServiceImpl(ChapterRepository chapterRepository) {
        super();
        this.chapterRepository = chapterRepository;
    }

    @Override
    public Chapter saveChapter(Chapter chapter) {

        return chapterRepository.save(chapter);
    }

    @Override
    public Chapter updateChapter(Chapter chapter) {
        Chapter existingChapter = chapterRepository.findById(chapter.getId()).orElseThrow(
                () -> new ResourceNotFoundException("Chapter", "Id", chapter.getId()));
        existingChapter.update(chapter);
        return chapterRepository.save(existingChapter);
    }

    @Override
    public void deleteChapter(Chapter chapter) {
        Chapter existingChapter = chapterRepository.findById(chapter.getId()).orElseThrow(
                () -> new ResourceNotFoundException("Chapter", "Id", chapter.getId()));
        chapterRepository.delete(existingChapter);
    }

    @Override
    public Chapter getChapterById(Long id) {
        Chapter existingChapter = chapterRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Chapter", "Id", id));
        return existingChapter;
    }

    @Override
    public List<Chapter> getAllChapters() {
        return chapterRepository.findAll();
    }
    
}