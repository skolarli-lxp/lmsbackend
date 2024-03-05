package com.skolarli.lmsservice.services.impl.questionbank;

import com.skolarli.lmsservice.models.db.questionbank.BankQuestionTrueOrFalse;
import com.skolarli.lmsservice.models.dto.questionbank.NewBankQuestionTrueOrFalseRequest;
import com.skolarli.lmsservice.services.core.LmsUserService;
import com.skolarli.lmsservice.services.course.BatchService;
import com.skolarli.lmsservice.services.course.ChapterService;
import com.skolarli.lmsservice.services.course.CourseService;
import com.skolarli.lmsservice.services.course.LessonService;
import com.skolarli.lmsservice.utils.UserUtils;
import lombok.NoArgsConstructor;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
public class QuestionBankTrueFalseProcessor extends QuestionBankProcessor
    implements ItemProcessor<NewBankQuestionTrueOrFalseRequest, BankQuestionTrueOrFalse> {

    public QuestionBankTrueFalseProcessor(CourseService courseService,
                                          BatchService batchService,
                                          LessonService lessonService,
                                          ChapterService chapterService,
                                          LmsUserService lmsUserService,
                                          UserUtils userUtils) {
        super(courseService, batchService, lessonService, chapterService, lmsUserService, userUtils);
    }

    @Override
    public BankQuestionTrueOrFalse process(NewBankQuestionTrueOrFalseRequest question) {
        BankQuestionTrueOrFalse bankQuestionTrueOrFalse = new BankQuestionTrueOrFalse();
        super.process(question, bankQuestionTrueOrFalse);
        bankQuestionTrueOrFalse.setOption1(question.getOption1());
        bankQuestionTrueOrFalse.setOption2(question.getOption2());
        bankQuestionTrueOrFalse.setCorrectAnswer(question.getCorrectAnswer());
        return bankQuestionTrueOrFalse;
    }
}