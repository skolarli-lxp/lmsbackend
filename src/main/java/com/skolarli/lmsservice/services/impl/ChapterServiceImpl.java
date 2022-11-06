package com.skolarli.lmsservice.services.impl;

import java.util.List;

import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.skolarli.lmsservice.exception.OperationNotSupportedException;
import com.skolarli.lmsservice.exception.ResourceNotFoundException;
import com.skolarli.lmsservice.models.db.Chapter;
import com.skolarli.lmsservice.models.db.LmsUser;
import com.skolarli.lmsservice.repository.ChapterRepository;
import com.skolarli.lmsservice.services.ChapterService;
import com.skolarli.lmsservice.utils.UserUtils;

import ch.qos.logback.classic.Logger;

@Service
public class ChapterServiceImpl implements ChapterService {

    private static final Logger logger = (Logger) LoggerFactory.getLogger(ChapterServiceImpl.class);

    ChapterRepository chapterRepository;
    UserUtils userUtils;

    public ChapterServiceImpl(ChapterRepository chapterRepository, UserUtils userutils) {
        super();
        this.chapterRepository = chapterRepository;
        this.userUtils = userutils;
    }

    @Override
    public Chapter saveChapter(Chapter chapter) {
        if (checkPermission(chapter) == false) {
            throw new OperationNotSupportedException("Operation not supported");
        }
        return chapterRepository.save(chapter);
    }

    private Boolean checkPermission(Chapter chapter) {
        LmsUser currentUser = userUtils.getCurrentUser();
        if (currentUser.getIsAdmin() != true && currentUser != chapter.getCourse().getCourseOwner()) {
            return false;
        }
        return true;
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

    @Override
    public Chapter getChapterById(Long id) {
        Chapter existingChapter = chapterRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Chapter", "Id", id));
        return existingChapter;
    }

    @Override
    public List<Chapter> getAllChapters() {
        return chapterRepository.findAll();
    }
}