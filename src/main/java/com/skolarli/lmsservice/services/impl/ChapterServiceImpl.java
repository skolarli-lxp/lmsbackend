package com.skolarli.lmsservice.services.impl;

import java.util.List;

import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.skolarli.lmsservice.exception.OperationNotSupportedException;
import com.skolarli.lmsservice.exception.ResourceNotFoundException;
import com.skolarli.lmsservice.models.ChapterSortOrderRequest;
import com.skolarli.lmsservice.models.ChapterSortOrderResponse;
import com.skolarli.lmsservice.models.NewChapterRequest;
import com.skolarli.lmsservice.models.db.Chapter;
import com.skolarli.lmsservice.models.db.LmsUser;
import com.skolarli.lmsservice.repository.ChapterRepository;
import com.skolarli.lmsservice.services.ChapterService;
import com.skolarli.lmsservice.services.CourseService;
import com.skolarli.lmsservice.utils.UserUtils;

import ch.qos.logback.classic.Logger;

@Service
public class ChapterServiceImpl implements ChapterService {

    private static final Logger logger = (Logger) LoggerFactory.getLogger(ChapterServiceImpl.class);

    ChapterRepository chapterRepository;
    UserUtils userUtils;
    CourseService courseService;

    public ChapterServiceImpl(ChapterRepository chapterRepository, UserUtils userutils, CourseService courseService) {
        super();
        this.chapterRepository = chapterRepository;
        this.userUtils = userutils;
        this.courseService = courseService;
    }

    private Boolean checkPermission(Chapter chapter) {
        LmsUser currentUser = userUtils.getCurrentUser();
        if (currentUser.getIsAdmin() != true && currentUser != chapter.getCourse().getCourseOwner()) {
            return false;
        }
        return true;
    }

    private int getHighestSortOrder(long courseId){
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

        return  chapter;
    }

    @Override
    public Chapter getChapterById(Long id) {
        Chapter existingChapter = chapterRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Chapter", "Id", id));
        return existingChapter;
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
        if (checkPermission(chapter) == false) {
            throw new OperationNotSupportedException("Operation not supported");
        }
        return chapterRepository.save(chapter);
    }

    @Override
    public Chapter updateChapter(Chapter chapter, long id) {
        Chapter existingChapter = chapterRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Chapter", "Id", id));
        if (checkPermission(existingChapter) == false) {
            throw new OperationNotSupportedException("User does not have permissions to update this chapter");
        }
        if (chapter.getChapterIsDeleted() != null) {
            logger.error("Cannot change deleted status. Use delete APIs instead");
            chapter.setChapterIsDeleted(null);
        }
        existingChapter.update(chapter);
        return chapterRepository.save(existingChapter);
    }

    @Override
    public List<ChapterSortOrderResponse> updateChaptersSortOrder(Long courseId,
            List<ChapterSortOrderRequest> chaptersSortOrderList) {
        List<Chapter> chapters = getChaptersByCourseId(courseId);
        for (Chapter chapter: chapters) {
            for (ChapterSortOrderRequest chapterSortOrder: chaptersSortOrderList) {
                if (chapterSortOrder.getChapterSortOrder() > 0 &&  
                    chapter.getId() == chapterSortOrder.getChapterId()) {
                        
                    chapter.setChapterSortOrder(chapterSortOrder.getChapterSortOrder());
                }
            }
        }
        chapterRepository.saveAll(chapters);
        return Chapter.toChapterSortOrderResponseList(chapters);
    }

    @Override
    public void deleteChapter(long id) {
        Chapter existingChapter = chapterRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Chapter", "Id", id));
        if (checkPermission(existingChapter) == false) {
            throw new OperationNotSupportedException("User does not have permissions to delete this chapter");
        }
        existingChapter.setChapterIsDeleted(true);
        chapterRepository.save(existingChapter);
    }

    @Override
    public void hardDeleteChapter(long id) {
        Chapter existingChapter = chapterRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Chapter", "Id", id));
        if (checkPermission(existingChapter) == false) {
            throw new OperationNotSupportedException("User does not have permissions to delete this chapter");
        }
        chapterRepository.delete(existingChapter);
    }
}