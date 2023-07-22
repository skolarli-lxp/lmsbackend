package com.skolarli.lmsservice.services.course;

import com.skolarli.lmsservice.models.db.course.Lesson;
import com.skolarli.lmsservice.models.dto.course.LessonSortOrderRequest;
import com.skolarli.lmsservice.models.dto.course.LessonSortOrderResponse;
import com.skolarli.lmsservice.models.dto.course.LessonUpdateRequest;
import com.skolarli.lmsservice.models.dto.course.NewLessonRequest;

import java.util.List;


public interface LessonService {
    Lesson toLesson(NewLessonRequest newLessonRequest);

    List<Lesson> getAllLessons();

    Lesson getLessonById(long id);

    List<Lesson> getLessonsByChapterId(long id);

    List<LessonSortOrderResponse> getAllLessonsSortOrder(long chapterId);

    Lesson saveLesson(Lesson lesson);

    Lesson updateLesson(LessonUpdateRequest lesson, long id);

    Lesson updateLesson(Lesson lesson, long id);

    List<LessonSortOrderResponse> updateLessonSortOrder(
            Long chapterId, List<LessonSortOrderRequest> lessonSortOrderRequest);

    void softDeleteLesson(long id);

    void hardDeleteLesson(long id);
}