package com.skolarli.lmsservice.services;

import com.skolarli.lmsservice.models.db.Lesson;
import com.skolarli.lmsservice.models.dto.LessonSortOrderRequest;
import com.skolarli.lmsservice.models.dto.LessonSortOrderResponse;
import com.skolarli.lmsservice.models.dto.NewLessonRequest;

import java.util.List;


public interface LessonService {
    Lesson toLesson(NewLessonRequest newLessonRequest);

    List<Lesson> getAllLessons();

    Lesson getLessonById(long id);

    List<Lesson> getLessonsByChapterId(long id);

    List<LessonSortOrderResponse> getAllLessonsSortOrder(long chapterId);

    Lesson saveLesson(Lesson lesson);

    Lesson updateLesson(Lesson lesson, long id);

    List<LessonSortOrderResponse> updateLessonSortOrder(
            Long chapterId, List<LessonSortOrderRequest> lessonSortOrderRequest);

    void deleteLesson(long id);

    void hardDeleteLesson(long id);
}