package com.skolarli.lmsservice.services.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.skolarli.lmsservice.exception.ResourceNotFoundException;
import com.skolarli.lmsservice.models.db.Lesson;
import com.skolarli.lmsservice.repository.LessonRepository;
import com.skolarli.lmsservice.services.LessonService;

@Service
public class LessonServiceImpl implements LessonService {

    Logger logger = (Logger) LoggerFactory.getLogger(LessonServiceImpl.class);
    
    LessonRepository lessonRepository;

    public LessonServiceImpl(LessonRepository lessonRepository) {
        super();
        this.lessonRepository = lessonRepository;
    }

    @Override
    public Lesson saveLesson(Lesson lesson) {
        return lessonRepository.save(lesson);
    }

    @Override
    public List<Lesson> getAllLessons() {
        return lessonRepository.findAll();
    }

    @Override
    public Lesson getLessonById(long id) {
        Lesson lesson = lessonRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Lesson", "Id", id));
        return lesson;
    }

    @Override
    public Lesson updateLesson(Lesson lesson, long id) {
        Lesson existingLesson = lessonRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Lesson", "Id", id));
        existingLesson.update(lesson);
        lessonRepository.save(existingLesson);
        return existingLesson;
    }

    @Override
    public void deleteLesson(long id) {
        Lesson existingLesson = lessonRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Lesson", "Id", id));
        
    }
}