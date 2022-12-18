package com.skolarli.lmsservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import com.skolarli.lmsservice.models.db.Chapter;

public interface ChapterRepository extends TenantableRepository<Chapter> {
    @Query("SELECT MAX(c.chapterSortOrder) FROM Chapter c WHERE c.course.id = ?1")
    int maxChapterSortOrder(long courseId);

    @Query("SELECT MAX(c.chapterSortOrder) FROM Chapter c WHERE c.course.id = ?1")
    int findMaxChapterSortOrder(long courseId);

    List<Chapter> findByCourseId(long courseId);
    List<Chapter> findByCourseIdOrderByChapterSortOrderAsc(long courseId);
}