package com.skolarli.lmsservice.services.impl.questionbank;

import com.skolarli.lmsservice.models.db.questionbank.BankQuestionMcq;
import com.skolarli.lmsservice.models.dto.questionbank.NewBankQuestionMcqRequest;
import com.skolarli.lmsservice.services.core.LmsUserService;
import com.skolarli.lmsservice.services.course.BatchService;
import com.skolarli.lmsservice.services.course.ChapterService;
import com.skolarli.lmsservice.services.course.CourseService;
import com.skolarli.lmsservice.services.course.LessonService;
import com.skolarli.lmsservice.utils.UserUtils;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
public class QuestionBankMcqProcessor implements ItemProcessor<NewBankQuestionMcqRequest, BankQuestionMcq> {

    CourseService courseService;
    BatchService batchService;
    LessonService lessonService;
    ChapterService chapterService;
    LmsUserService lmsUserService;

    UserUtils userUtils;

    public QuestionBankMcqProcessor(CourseService courseService,
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

    @Override
    public BankQuestionMcq process(NewBankQuestionMcqRequest question) {
        BankQuestionMcq bankQuestionMcq = new BankQuestionMcq();
        bankQuestionMcq.setQuestion(question.getQuestion());
        bankQuestionMcq.setNumberOfOptions(question.getNumberOfOptions());
        bankQuestionMcq.setOption1(question.getOption1());
        bankQuestionMcq.setOption2(question.getOption2());
        bankQuestionMcq.setOption3(question.getOption3());
        bankQuestionMcq.setOption4(question.getOption4());
        bankQuestionMcq.setOption5(question.getOption5());
        bankQuestionMcq.setOption6(question.getOption6());
        bankQuestionMcq.setNumberOfCorrectAnswers(question.getNumberOfCorrectAnswers());

        if (question.getCourseId() != null) {
            bankQuestionMcq.setCourse(courseService.getCourseById(question.getCourseId()));
        }
        if (question.getBatchId() != null) {
            bankQuestionMcq.setBatch(batchService.getBatch(question.getBatchId()));
        }
        if (question.getChapterId() != null) {
            bankQuestionMcq.setChapter(chapterService.getChapterById(question.getChapterId()));
        }
        if (question.getLessonId() != null) {
            bankQuestionMcq.setLesson(lessonService.getLessonById(question.getLessonId()));
        }
        if (question.getStudentId() != null) {
            bankQuestionMcq.setStudent(lmsUserService.getLmsUserById(question.getStudentId()));
        }
        bankQuestionMcq.setCreatedBy(userUtils.getCurrentUser());
        return bankQuestionMcq;
    }
}