package com.skolarli.lmsservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import com.skolarli.lmsservice.models.db.Lesson;


public interface LessonRepository extends TenantableRepository<Lesson> {
    @Query(value = "SELECT COALESCE(MAX(lesson_sort_order),0) FROM lms.lessons where chapter_id = ?1", nativeQuery = true)
    int findMaxLessonSortOrder(long chapterId);

    List<Lesson> findByChapterId(long courseId);
    List<Lesson> findByChapterIdOrderByLessonSortOrderAsc(long courseId);
    
}