package com.skolarli.lmsservice.services.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.skolarli.lmsservice.exception.OperationNotSupportedException;
import com.skolarli.lmsservice.exception.ResourceNotFoundException;
import com.skolarli.lmsservice.models.db.Lesson;
import com.skolarli.lmsservice.models.db.LmsUser;
import com.skolarli.lmsservice.repository.LessonRepository;
import com.skolarli.lmsservice.services.LessonService;
import com.skolarli.lmsservice.utils.UserUtils;

@Service
public class LessonServiceImpl implements LessonService {

    Logger logger = (Logger) LoggerFactory.getLogger(LessonServiceImpl.class);
    
    LessonRepository lessonRepository;
    UserUtils userUtils;

    public LessonServiceImpl(LessonRepository lessonRepository, UserUtils userUtils) {
        super();
        this.lessonRepository = lessonRepository;
        this.userUtils = userUtils;
    }

    @Override
    public Lesson saveLesson(Lesson lesson) {
        return lessonRepository.save(lesson);
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

    private Boolean checkPermission(Lesson lesson) {
        LmsUser currentUser = userUtils.getCurrentUser();
        if (currentUser.getIsAdmin() != true && currentUser != lesson.getChapter().getCourse().getCourseOwner()) {
            return false;
        }
        return true;
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