package com.skolarli.lmsservice.services;

import java.util.List;

import com.skolarli.lmsservice.models.NewLessonRequest;
import com.skolarli.lmsservice.models.db.Lesson;


public interface LessonService {
    Lesson toLesson(NewLessonRequest newLessonRequest);

    List<Lesson> getAllLessons ();
    Lesson getLessonById(long id);
    List<Lesson> getLessonsByChapterId(long id);

    Lesson saveLesson (Lesson lesson);
   
    Lesson updateLesson(Lesson lesson, long id);
    
    void deleteLesson(long id);
    void hardDeleteLesson(long id);
}