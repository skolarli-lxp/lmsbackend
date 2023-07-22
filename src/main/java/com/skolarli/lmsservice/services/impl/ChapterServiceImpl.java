package com.skolarli.lmsservice.services.impl;

import ch.qos.logback.classic.Logger;
import com.skolarli.lmsservice.exception.OperationNotSupportedException;
import com.skolarli.lmsservice.exception.ResourceNotFoundException;
import com.skolarli.lmsservice.models.db.core.LmsUser;
import com.skolarli.lmsservice.models.db.course.Chapter;
import com.skolarli.lmsservice.models.dto.course.ChapterSortOrderRequest;
import com.skolarli.lmsservice.models.dto.course.ChapterSortOrderResponse;
import com.skolarli.lmsservice.models.dto.course.NewChapterRequest;
import com.skolarli.lmsservice.repository.ChapterRepository;
import com.skolarli.lmsservice.services.ChapterService;
import com.skolarli.lmsservice.services.CourseService;
import com.skolarli.lmsservice.utils.UserUtils;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ChapterServiceImpl implements ChapterService {

    private static final Logger logger = (Logger) LoggerFactory.getLogger(ChapterServiceImpl.class);

    final ChapterRepository chapterRepository;
    final UserUtils userUtils;
    final CourseService courseService;

    public ChapterServiceImpl(ChapterRepository chapterRepository, UserUtils userutils,
                              CourseService courseService) {
        super();
        this.chapterRepository = chapterRepository;
        this.userUtils = userutils;
        this.courseService = courseService;
    }

    private Boolean checkPermission(Chapter chapter) {
        LmsUser currentUser = userUtils.getCurrentUser();
        return currentUser.getIsAdmin() || currentUser == chapter.getCourse().getCourseOwner();
    }

    private int getHighestSortOrder(long courseId) {
        return chapterRepository.findMaxChapterSortOrder(courseId);
    }

    @Override
    public Chapter toChapter(NewChapterRequest newChapterRequest) {
        Chapter chapter = new Chapter();
        long courseId = newChapterRequest.getCourseId();

        chapter.setChapterName(newChapterRequest.getChapterName());
        chapter.setChapterDescription(newChapterRequest.getChapterDescription());

        if (newChapterRequest.getChapterSortOrder() == 0) {
            newChapterRequest.setChapterSortOrder(getHighestSortOrder(courseId) + 1);
        }

        chapter.setChapterSortOrder(newChapterRequest.getChapterSortOrder());
        chapter.setCourse(courseService.getCourseById(courseId));
        chapter.setChapterIsDeleted(false);

        return chapter;
    }

    @Override
    public Chapter getChapterById(Long id) {
        List<Chapter> existingChapter = chapterRepository.findAllById(new ArrayList<>(List.of(id)));
        if (existingChapter.size() == 0) {
            throw new ResourceNotFoundException("Chapter", "Id", id);
        }
        return existingChapter.get(0);
    }

    @Override
    public List<Chapter> getChaptersByCourseId(Long courseId) {
        return chapterRepository.findByCourseIdOrderByChapterSortOrderAsc(courseId);
    }

    @Override
    public List<Chapter> getAllChapters() {
        return chapterRepository.findAll();
    }


    @Override
    public List<ChapterSortOrderResponse> getChaptersSortOrder(Long courseId) {
        List<Chapter> chapters = getChaptersByCourseId(courseId);
        return Chapter.toChapterSortOrderResponseList(chapters);
    }

    @Override
    public Chapter saveChapter(Chapter chapter) {
        if (!checkPermission(chapter)) {
            throw new OperationNotSupportedException("Operation not supported");
        }
        return chapterRepository.save(chapter);
    }

    @Override
    public Chapter updateChapter(NewChapterRequest request, long id) {
        Chapter existingChapter = chapterRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Chapter", "Id", id));
        if (!checkPermission(existingChapter)) {
            throw new OperationNotSupportedException("User does not have permissions to "
                    + "update this chapter");
        }
        existingChapter.update(request);
        return chapterRepository.save(existingChapter);
    }

    @Override
    public List<ChapterSortOrderResponse> updateChaptersSortOrder(
            Long courseId,
            List<ChapterSortOrderRequest> chaptersSortOrderList) {
        List<Chapter> chapters = getChaptersByCourseId(courseId);
        for (Chapter chapter : chapters) {
            for (ChapterSortOrderRequest chapterSortOrder : chaptersSortOrderList) {
                if (chapterSortOrder.getChapterSortOrder() > 0
                        && chapter.getId() == chapterSortOrder.getChapterId()) {

                    chapter.setChapterSortOrder(chapterSortOrder.getChapterSortOrder());
                }
            }
        }
        chapterRepository.saveAll(chapters);
        return Chapter.toChapterSortOrderResponseList(chapters);
    }

    private void canDelete(Chapter existingChapter) {
        if (!checkPermission(existingChapter)) {
            throw new OperationNotSupportedException("User does not have permissions to delete "
                    + "this chapter");
        }
        if (existingChapter.getChapterLessons() != null
                && !existingChapter.getChapterLessons().isEmpty()) {
            throw new OperationNotSupportedException("Cannot delete chapter with lessons");
        }
    }

    @Override
    public void softDeleteChapter(long id) {
        Chapter existingChapter = chapterRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Chapter", "Id", id));
        try {
            canDelete(existingChapter);
        } catch (OperationNotSupportedException e) {
            logger.error("Cannot delete chapter", e);
            throw e;
        }
        existingChapter.setChapterIsDeleted(true);
        chapterRepository.save(existingChapter);
    }

    @Override
    public void hardDeleteChapter(long id) {
        Chapter existingChapter = chapterRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Chapter", "Id", id));
        try {
            canDelete(existingChapter);
        } catch (OperationNotSupportedException e) {
            logger.error("Cannot delete chapter", e);
            throw e;
        }
        chapterRepository.delete(existingChapter);
    }
}