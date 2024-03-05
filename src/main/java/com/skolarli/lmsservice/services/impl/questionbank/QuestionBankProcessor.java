package com.skolarli.lmsservice.services.impl.questionbank;

import com.skolarli.lmsservice.models.db.questionbank.BankQuestion;
import com.skolarli.lmsservice.models.db.questionbank.BankQuestionMcq;
import com.skolarli.lmsservice.models.dto.questionbank.NewBankQuestionMcqRequest;
import com.skolarli.lmsservice.models.dto.questionbank.NewBankQuestionSubjectiveRequest;
import com.skolarli.lmsservice.models.dto.questionbank.NewQuestionBankQuestionRequest;
import com.skolarli.lmsservice.services.core.LmsUserService;
import com.skolarli.lmsservice.services.course.BatchService;
import com.skolarli.lmsservice.services.course.ChapterService;
import com.skolarli.lmsservice.services.course.CourseService;
import com.skolarli.lmsservice.services.course.LessonService;
import com.skolarli.lmsservice.utils.UserUtils;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

public abstract class QuestionBankProcessor {

    CourseService courseService;
    BatchService batchService;
    LessonService lessonService;
    ChapterService chapterService;
    LmsUserService lmsUserService;

    UserUtils userUtils;

    public QuestionBankProcessor(CourseService courseService,
                                 BatchService batchService,
                                 LessonService lessonService,
                                 ChapterService chapterService,
                                 LmsUserService lmsUserService,
                                 UserUtils userUtils) {
        this.courseService = courseService;
        this.batchService = batchService;
        this.lessonService = lessonService;
        this.chapterService = chapterService;
        this.lmsUserService = lmsUserService;
        this.userUtils = userUtils;
    }

    public BankQuestion process(NewQuestionBankQuestionRequest question, BankQuestion bankQuestion) {

        bankQuestion.setQuestion(question.getQuestion());
        bankQuestion.setMarks(question.getMarks());
        bankQuestion.setQuestionType(question.getQuestionType());
        bankQuestion.setDifficultyLevel(question.getDifficultyLevel());
        bankQuestion.setQuestionFormat(question.getQuestionFormat());
        bankQuestion.setAnswerFormat(question.getAnswerFormat());
        bankQuestion.setSampleAnswerText(question.getSampleAnswerText());
        bankQuestion.setSampleAnswerUrl(question.getSampleAnswerUrl());

        if (question.getCourseId() != null) {
            bankQuestion.setCourse(courseService.getCourseById(question.getCourseId()));
        }
        if (question.getBatchId() != null) {
            bankQuestion.setBatch(batchService.getBatch(question.getBatchId()));
        }
        if (question.getChapterId() != null) {
            bankQuestion.setChapter(chapterService.getChapterById(question.getChapterId()));
        }
        if (question.getLessonId() != null) {
            bankQuestion.setLesson(lessonService.getLessonById(question.getLessonId()));
        }
        if (question.getStudentId() != null) {
            bankQuestion.setStudent(lmsUserService.getLmsUserById(question.getStudentId()));
        }
        bankQuestion.setCreatedBy(userUtils.getCurrentUser());
        return bankQuestion;
    }
}