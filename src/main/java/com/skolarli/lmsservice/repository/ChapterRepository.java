package com.skolarli.lmsservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import com.skolarli.lmsservice.models.db.Chapter;

public interface ChapterRepository extends TenantableRepository<Chapter> {
    @Query(value = "SELECT COALESCE(MAX(chapter_sort_order),0) " +
            "FROM lms.chapters where course_id = ?1", nativeQuery = true)
    int findMaxChapterSortOrder(long courseId);

    List<Chapter> findByCourseId(long courseId);
    List<Chapter> findByCourseIdOrderByChapterSortOrderAsc(long courseId);
}