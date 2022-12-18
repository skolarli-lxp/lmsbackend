package com.skolarli.lmsservice.services.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.skolarli.lmsservice.exception.OperationNotSupportedException;
import com.skolarli.lmsservice.exception.ResourceNotFoundException;
import com.skolarli.lmsservice.models.NewLessonRequest;
import com.skolarli.lmsservice.models.db.Lesson;
import com.skolarli.lmsservice.models.db.LmsUser;
import com.skolarli.lmsservice.repository.LessonRepository;
import com.skolarli.lmsservice.services.ChapterService;
import com.skolarli.lmsservice.services.LessonService;
import com.skolarli.lmsservice.utils.UserUtils;

@Service
public class LessonServiceImpl implements LessonService {

    Logger logger = (Logger) LoggerFactory.getLogger(LessonServiceImpl.class);
    
    LessonRepository lessonRepository;
    UserUtils userUtils;
    ChapterService chapterService;

    public LessonServiceImpl(LessonRepository lessonRepository, UserUtils userUtils, ChapterService chapterService) {
        super();
        this.lessonRepository = lessonRepository;
        this.userUtils = userUtils;
        this.chapterService = chapterService;
    }

    private Boolean checkPermission(Lesson lesson) {
        LmsUser currentUser = userUtils.getCurrentUser();
        if (currentUser.getIsAdmin() != true && currentUser != lesson.getChapter().getCourse().getCourseOwner()) {
            return false;
        }
        return true;
    }

    @Override
    public Lesson toLesson(NewLessonRequest newLessonRequest) {
        Lesson lesson = new Lesson();

        lesson.setLessonName(newLessonRequest.getLessonName());
        lesson.setLessonDescription(newLessonRequest.getLessonDescription());
        lesson.setChapter(chapterService.getChapterById(newLessonRequest.getChapterId()));

        // Lesson Video Related info
        lesson.setVideoId(newLessonRequest.getVideoId());
        lesson.setVideoTitle(newLessonRequest.getVideoTitle());
        lesson.setVideoDescription(newLessonRequest.getVideoDescription());
        lesson.setVideoUrl(newLessonRequest.getVideoUrl());
        lesson.setVideoThumbnailUrl(newLessonRequest.getVideoThumbnailUrl());
        lesson.setVideoSize(newLessonRequest.getVideoSize());
        lesson.setAllowDownload(newLessonRequest.getAllowDownload());
        lesson.setVideoIsActive(newLessonRequest.getVideoIsActive());
        
        // Lesson Text Related info
        lesson.setTextContent(newLessonRequest.getTextContent());
        lesson.setTextTitle(newLessonRequest.getTextTitle());
        lesson.setTextDescription(newLessonRequest.getTextDescription());
        lesson.setTextUrl(newLessonRequest.getTextUrl());
        lesson.setTextIsActive(newLessonRequest.getTextIsActive());

        // Lesson PDF Related info
        lesson.setPdfTitle(newLessonRequest.getPdfTitle());
        lesson.setPdfDescription(newLessonRequest.getPdfDescription());
        lesson.setPdfUrl(newLessonRequest.getPdfUrl());
        lesson.setPdfSize(newLessonRequest.getPdfSize());
        lesson.setPdfIsActive(newLessonRequest.getPdfIsActive());

        // Lesson Audio Related info
        lesson.setAudioTitle(newLessonRequest.getAudioTitle());
        lesson.setAudioDescription(newLessonRequest.getAudioDescription());
        lesson.setAudioUrl(newLessonRequest.getAudioUrl());
        lesson.setAudioSize(newLessonRequest.getAudioSize());
        lesson.setAudioIsActive(newLessonRequest.getAudioIsActive());

        // Lesson Presentation Related info
        lesson.setPresentationTitle(newLessonRequest.getPresentationTitle());
        lesson.setPresentationDescription(newLessonRequest.getPresentationDescription());
        lesson.setPresentationUrl(newLessonRequest.getPresentationUrl());
        lesson.setPresentationSize(newLessonRequest.getPresentationSize());
        lesson.setPresentationIsActive(newLessonRequest.getPresentationIsActive());

        // Lesson Downloadables Related info
        lesson.setDownloadablesTitle(newLessonRequest.getDownloadablesTitle());
        lesson.setDownloadablesDescription(newLessonRequest.getDownloadablesDescription());
        lesson.setDownloadablesUrl(newLessonRequest.getDownloadablesUrl());
        lesson.setDownloadablesSize(newLessonRequest.getDownloadablesSize());
        lesson.setDownloadablesIsActive(newLessonRequest.getDownloadablesIsActive());

        lesson.setLessonIsDeleted(false);

        return lesson;
    }


    @Override
    public List<Lesson> getAllLessons() {
        return lessonRepository.findAll();
    }

    @Override
    public Lesson getLessonById(long id) {
        Lesson lesson = lessonRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Lesson", "Id", id));
        return lesson;
    }


    @Override
    public Lesson saveLesson(Lesson lesson) {
        return lessonRepository.save(lesson);
    }

    @Override
    public Lesson updateLesson(Lesson lesson, long id) {
        Lesson existingLesson = lessonRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Lesson", "Id", id));
        if (checkPermission(existingLesson) == false) {
            throw new OperationNotSupportedException("User does not have permissions to update this lesson");
        }
        if (lesson.getLessonIsDeleted() != null) {
            logger.error("Cannot change deleted status. Use delete APIs instead");
            lesson.setLessonIsDeleted(null);
        }
        existingLesson.update(lesson);
        return lessonRepository.save(existingLesson);
    }

    @Override
    public void deleteLesson(long id) {
        Lesson existingLesson = lessonRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Lesson", "Id", id));
        if (checkPermission(existingLesson) == false) {
            throw new OperationNotSupportedException("User does not have permissions to delete this lesson");
        }
        existingLesson.setLessonIsDeleted(true);
        lessonRepository.save(existingLesson);
    }

    @Override
    public void hardDeleteLesson(long id) {
        Lesson existingLesson = lessonRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Lesson", "Id", id));
        if (checkPermission(existingLesson) == false) {
            throw new OperationNotSupportedException("User does not have permissions to delete this lesson");
        }
        lessonRepository.delete(existingLesson);
    }
}