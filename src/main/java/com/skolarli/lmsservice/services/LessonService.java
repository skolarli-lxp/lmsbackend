package com.skolarli.lmsservice.services;

import java.util.List;

import com.skolarli.lmsservice.models.NewLessonRequest;
import com.skolarli.lmsservice.models.db.Lesson;


public interface LessonService {
    Lesson toLesson(NewLessonRequest newLessonRequest);
    Lesson saveLesson (Lesson lesson);
    List<Lesson> getAllLessons ();
    Lesson getLessonById(long id);
    Lesson updateLesson(Lesson lesson, long id);
    void deleteLesson(long id);
    void hardDeleteLesson(long id);
}