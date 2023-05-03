package com.skolarli.lmsservice.repository;

import com.skolarli.lmsservice.models.db.Chapter;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ChapterRepository extends TenantableRepository<Chapter> {
    @Query(value = "SELECT COALESCE(MAX(chapter_sort_order),0) " +
            "FROM lms.chapters where course_id = ?1", nativeQuery = true)
    int findMaxChapterSortOrder(long courseId);

    List<Chapter> findByCourseId(long courseId);

    List<Chapter> findByCourseIdOrderByChapterSortOrderAsc(long courseId);
}