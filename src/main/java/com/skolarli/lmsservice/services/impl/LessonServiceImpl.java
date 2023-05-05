package com.skolarli.lmsservice.services.impl;

import com.skolarli.lmsservice.exception.OperationNotSupportedException;
import com.skolarli.lmsservice.exception.ResourceNotFoundException;
import com.skolarli.lmsservice.models.db.Chapter;
import com.skolarli.lmsservice.models.db.Lesson;
import com.skolarli.lmsservice.models.db.LmsUser;
import com.skolarli.lmsservice.models.dto.LessonSortOrderRequest;
import com.skolarli.lmsservice.models.dto.LessonSortOrderResponse;
import com.skolarli.lmsservice.models.dto.LessonUpdateRequest;
import com.skolarli.lmsservice.models.dto.NewLessonRequest;
import com.skolarli.lmsservice.repository.LessonRepository;
import com.skolarli.lmsservice.services.ChapterService;
import com.skolarli.lmsservice.services.LessonService;
import com.skolarli.lmsservice.utils.UserUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class LessonServiceImpl implements LessonService {

    final Logger logger = LoggerFactory.getLogger(LessonServiceImpl.class);

    final LessonRepository lessonRepository;
    final UserUtils userUtils;
    final ChapterService chapterService;

    public LessonServiceImpl(LessonRepository lessonRepository, UserUtils userUtils,
                             ChapterService chapterService) {
        super();
        this.lessonRepository = lessonRepository;
        this.userUtils = userUtils;
        this.chapterService = chapterService;
    }

    private Boolean checkPermission(Lesson lesson) {
        LmsUser currentUser = userUtils.getCurrentUser();
        return currentUser.getIsAdmin()
                || currentUser == lesson.getChapter().getCourse().getCourseOwner();
    }

    @Override
    public Lesson toLesson(NewLessonRequest newLessonRequest) {
        Lesson lesson = new Lesson();
        long chapterId = newLessonRequest.getChapterId();

        lesson.setLessonName(newLessonRequest.getLessonName());
        lesson.setLessonDescription(newLessonRequest.getLessonDescription());
        lesson.setChapter(chapterService.getChapterById(chapterId));

        if (newLessonRequest.getLessonSortOrder() == 0) {
            newLessonRequest.setLessonSortOrder(lessonRepository.findMaxLessonSortOrder(chapterId)
                    + 1);
        }
        lesson.setLessonSortOrder(newLessonRequest.getLessonSortOrder());

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
        List<Lesson> lesson = lessonRepository.findAllById(new ArrayList<>(List.of(id)));
        if (lesson.isEmpty()) {
            throw new ResourceNotFoundException("Lesson", "Id", id);
        }
        return lesson.get(0);
    }

    @Override
    public List<Lesson> getLessonsByChapterId(long id) {
        return lessonRepository.findByChapterIdOrderByLessonSortOrderAsc(id);
    }

    @Override
    public List<LessonSortOrderResponse> getAllLessonsSortOrder(long chapterId) {
        List<Lesson> lessons = lessonRepository.findByChapterIdOrderByLessonSortOrderAsc(chapterId);
        return Lesson.toLessonSortOrderResponseList(lessons);
    }


    @Override
    public Lesson saveLesson(Lesson lesson) {
        return lessonRepository.save(lesson);
    }


    @Override
    public Lesson updateLesson(Lesson lesson, long id) {
        Lesson existingLesson = lessonRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Lesson", "Id", id));
        if (!checkPermission(existingLesson)) {
            throw new OperationNotSupportedException("User does not have permissions to update "
                    + "this lesson");
        }
        if (lesson.getLessonIsDeleted() != null) {
            logger.error("Cannot change deleted status. Use delete APIs instead");
            lesson.setLessonIsDeleted(null);
        }
        existingLesson.update(lesson);
        return lessonRepository.save(existingLesson);
    }

    @Override
    public Lesson updateLesson(LessonUpdateRequest lessonUpdateRequest, long id) {
        Lesson existingLesson = lessonRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Lesson", "Id", id));
        if (!checkPermission(existingLesson)) {
            throw new OperationNotSupportedException("User does not have permissions to update "
                    + "this lesson");
        }
        updateExistingLesson(existingLesson, lessonUpdateRequest);
        return lessonRepository.save(existingLesson);
    }

    public List<LessonSortOrderResponse> updateLessonSortOrder(
            Long chapterId,
            List<LessonSortOrderRequest> lessonSortOrderRequest) {
        List<Lesson> lessons = lessonRepository.findByChapterIdOrderByLessonSortOrderAsc(chapterId);
        for (Lesson lesson : lessons) {
            for (LessonSortOrderRequest lessonSortOrder : lessonSortOrderRequest) {
                if (lessonSortOrder.getLessonSortOrder() > 0
                        && lesson.getId() == lessonSortOrder.getLessonId()) {
                    lesson.setLessonSortOrder(lessonSortOrder.getLessonSortOrder());
                }
            }
        }
        List<Lesson> savedLessons = lessonRepository.saveAll(lessons);
        return Lesson.toLessonSortOrderResponseList(savedLessons);
    }

    @Override
    public void deleteLesson(long id) {
        Lesson existingLesson = lessonRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Lesson", "Id", id));
        if (!checkPermission(existingLesson)) {
            throw new OperationNotSupportedException("User does not have permissions to delete "
                    + "this lesson");
        }
        existingLesson.setLessonIsDeleted(true);
        lessonRepository.save(existingLesson);
    }

    @Override
    public void hardDeleteLesson(long id) {
        Lesson existingLesson = lessonRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Lesson", "Id", id));
        if (!checkPermission(existingLesson)) {
            throw new OperationNotSupportedException("User does not have permissions to delete"
                    + " this lesson");
        }
        lessonRepository.delete(existingLesson);
    }

    private void updateExistingLesson(Lesson existingLesson, LessonUpdateRequest updateRequest) {
        if (updateRequest.getChapterId() != null) {
            if (updateRequest.getChapterId().isEmpty()) {
                existingLesson.setChapter(null);
            } else {
                Chapter chapter = chapterService.getChapterById(updateRequest.getChapterId().get());
                existingLesson.setChapter(chapter);
            }
        }
        if (updateRequest.getLessonName() != null) {
            if (updateRequest.getLessonName().isPresent()) {
                existingLesson.setLessonName(updateRequest.getLessonName().get());
            } else {
                existingLesson.setLessonName(null);
            }
        }
        if (updateRequest.getLessonDescription() != null) {
            if (updateRequest.getLessonDescription().isPresent()) {
                existingLesson.setLessonDescription(updateRequest.getLessonDescription().get());
            } else {
                existingLesson.setLessonDescription(null);
            }
        }
        if (updateRequest.getLessonSortOrder() != null
                && updateRequest.getLessonSortOrder().isPresent()) {
            existingLesson.setLessonSortOrder(updateRequest.getLessonSortOrder().get());
        }

        // Lesson Video Related info
        if (updateRequest.getVideoId() != null) {
            if (updateRequest.getVideoId().isPresent()) {
                existingLesson.setVideoId(updateRequest.getVideoId().get());
            } else {
                existingLesson.setVideoId(null);
            }
        }
        if (updateRequest.getVideoTitle() != null) {
            if (updateRequest.getVideoTitle().isPresent()) {
                existingLesson.setVideoTitle(updateRequest.getVideoTitle().get());
            } else {
                existingLesson.setVideoTitle(null);
            }
        }
        if (updateRequest.getVideoDescription() != null) {
            if (updateRequest.getVideoDescription().isPresent()) {
                existingLesson.setVideoDescription(updateRequest.getVideoDescription().get());
            } else {
                existingLesson.setVideoDescription(null);
            }
        }
        if (updateRequest.getVideoUrl() != null) {
            if (updateRequest.getVideoUrl().isPresent()) {
                existingLesson.setVideoUrl(updateRequest.getVideoUrl().get());
            } else {
                existingLesson.setVideoUrl(null);
            }
        }
        if (updateRequest.getVideoSize() != null && updateRequest.getVideoSize().isPresent()) {
            existingLesson.setVideoSize(updateRequest.getVideoSize().get());
        }
        if (updateRequest.getVideoIsActive() != null
                && updateRequest.getVideoIsActive().isPresent()) {
            existingLesson.setVideoIsActive(updateRequest.getVideoIsActive().get());
        }
        if (updateRequest.getVideoThumbnailUrl() != null) {
            if (updateRequest.getVideoThumbnailUrl().isPresent()) {
                existingLesson.setVideoThumbnailUrl(updateRequest.getVideoThumbnailUrl().get());
            } else {
                existingLesson.setVideoThumbnailUrl(null);
            }
        }
        if (updateRequest.getAllowDownload() != null
                && updateRequest.getAllowDownload().isPresent()) {
            existingLesson.setAllowDownload(updateRequest.getAllowDownload().get());
        }

        // Lesson text related info
        if (updateRequest.getTextContent() != null) {
            if (updateRequest.getTextContent().isPresent()) {
                existingLesson.setTextContent(updateRequest.getTextContent().get());
            } else {
                existingLesson.setTextContent(null);
            }
        }
        if (updateRequest.getTextTitle() != null) {
            if (updateRequest.getTextTitle().isPresent()) {
                existingLesson.setTextTitle(updateRequest.getTextTitle().get());
            } else {
                existingLesson.setTextTitle(null);
            }
        }
        if (updateRequest.getTextDescription() != null) {
            if (updateRequest.getTextDescription().isPresent()) {
                existingLesson.setTextDescription(updateRequest.getTextDescription().get());
            } else {
                existingLesson.setTextDescription(null);
            }
        }
        if (updateRequest.getTextUrl() != null) {
            if (updateRequest.getTextUrl().isPresent()) {
                existingLesson.setTextUrl(updateRequest.getTextUrl().get());
            } else {
                existingLesson.setTextUrl(null);
            }
        }
        if (updateRequest.getTextIsActive() != null
                && updateRequest.getTextIsActive().isPresent()) {
            existingLesson.setTextIsActive(updateRequest.getTextIsActive().get());
        }

        // Lesson PDF Related info

        if (updateRequest.getPdfTitle() != null) {
            if (updateRequest.getPdfTitle().isPresent()) {
                existingLesson.setPdfTitle(updateRequest.getPdfTitle().get());
            } else {
                existingLesson.setPdfTitle(null);
            }
        }
        if (updateRequest.getPdfDescription() != null) {
            if (updateRequest.getPdfDescription().isPresent()) {
                existingLesson.setPdfDescription(updateRequest.getPdfDescription().get());
            } else {
                existingLesson.setPdfDescription(null);
            }
        }
        if (updateRequest.getPdfUrl() != null) {
            if (updateRequest.getPdfUrl().isPresent()) {
                existingLesson.setPdfUrl(updateRequest.getPdfUrl().get());
            } else {
                existingLesson.setPdfUrl(null);
            }
        }
        if (updateRequest.getPdfSize() != null && updateRequest.getPdfSize().isPresent()) {
            existingLesson.setPdfSize(updateRequest.getPdfSize().get());

        }
        if (updateRequest.getPdfIsActive() != null && updateRequest.getPdfIsActive().isPresent()) {
            existingLesson.setPdfIsActive(updateRequest.getPdfIsActive().get());
        }

        // Lesson Audio Related info

        if (updateRequest.getAudioTitle() != null) {
            if (updateRequest.getAudioTitle().isPresent()) {
                existingLesson.setAudioTitle(updateRequest.getAudioTitle().get());
            } else {
                existingLesson.setAudioTitle(null);
            }
        }
        if (updateRequest.getAudioDescription() != null) {
            if (updateRequest.getAudioDescription().isPresent()) {
                existingLesson.setAudioDescription(updateRequest.getAudioDescription().get());
            } else {
                existingLesson.setAudioDescription(null);
            }
        }
        if (updateRequest.getAudioUrl() != null) {
            if (updateRequest.getAudioUrl().isPresent()) {
                existingLesson.setAudioUrl(updateRequest.getAudioUrl().get());
            } else {
                existingLesson.setAudioUrl(null);
            }
        }

        if (updateRequest.getAudioSize() != null && updateRequest.getAudioSize().isPresent()) {
            existingLesson.setAudioSize(updateRequest.getAudioSize().get());
        }
        if (updateRequest.getAudioIsActive() != null
                && updateRequest.getAudioIsActive().isPresent()) {
            existingLesson.setAudioIsActive(updateRequest.getAudioIsActive().get());
        }

        // Lesson presentation related info

        if (updateRequest.getPresentationTitle() != null) {
            if (updateRequest.getPresentationTitle().isPresent()) {
                existingLesson.setPresentationTitle(updateRequest.getPresentationTitle().get());
            } else {
                existingLesson.setPresentationTitle(null);
            }
        }
        if (updateRequest.getPresentationDescription() != null) {
            if (updateRequest.getPresentationDescription().isPresent()) {
                existingLesson.setPresentationDescription(
                        updateRequest.getPresentationDescription().get());
            } else {
                existingLesson.setPresentationDescription(null);
            }
        }
        if (updateRequest.getPresentationUrl() != null) {
            if (updateRequest.getPresentationUrl().isPresent()) {
                existingLesson.setPresentationUrl(updateRequest.getPresentationUrl().get());
            } else {
                existingLesson.setPresentationUrl(null);
            }
        }
        if (updateRequest.getPresentationSize() != null
                && updateRequest.getPresentationSize().isPresent()) {
            existingLesson.setPresentationSize(updateRequest.getPresentationSize().get());
        }
        if (updateRequest.getPresentationIsActive() != null
                && updateRequest.getPresentationIsActive().isPresent()) {
            existingLesson.setPresentationIsActive(updateRequest.getPresentationIsActive().get());
        }

        // Lesson downloadables related info

        if (updateRequest.getDownloadablesTitle() != null) {
            if (updateRequest.getDownloadablesTitle().isPresent()) {
                existingLesson.setDownloadablesTitle(updateRequest.getDownloadablesTitle().get());
            } else {
                existingLesson.setDownloadablesTitle(null);
            }
        }
        if (updateRequest.getDownloadablesDescription() != null) {
            if (updateRequest.getDownloadablesDescription().isPresent()) {
                existingLesson.setDownloadablesDescription(
                        updateRequest.getDownloadablesDescription().get());
            } else {
                existingLesson.setDownloadablesDescription(null);
            }
        }
        if (updateRequest.getDownloadablesUrl() != null) {
            if (updateRequest.getDownloadablesUrl().isPresent()) {
                existingLesson.setDownloadablesUrl(updateRequest.getDownloadablesUrl().get());
            } else {
                existingLesson.setDownloadablesUrl(null);
            }
        }
        if (updateRequest.getDownloadablesSize() != null
                && updateRequest.getDownloadablesSize().isPresent()) {
            existingLesson.setDownloadablesSize(updateRequest.getDownloadablesSize().get());

        }
        if (updateRequest.getDownloadablesIsActive() != null
                && updateRequest.getDownloadablesIsActive().isPresent()) {
            existingLesson.setDownloadablesIsActive(updateRequest.getDownloadablesIsActive().get());
        }
    }
}
