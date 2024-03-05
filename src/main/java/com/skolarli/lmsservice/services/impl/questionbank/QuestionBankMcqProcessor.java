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

import java.util.stream.Collectors;

@Component
public class QuestionBankMcqProcessor extends QuestionBankProcessor
    implements ItemProcessor<NewBankQuestionMcqRequest, BankQuestionMcq> {

    public QuestionBankMcqProcessor(CourseService courseService,
                                    BatchService batchService,
                                    LessonService lessonService,
                                    ChapterService chapterService,
                                    LmsUserService lmsUserService,
                                    UserUtils userUtils) {
        super(courseService, batchService, lessonService, chapterService, lmsUserService, userUtils);
    }

    @Override
    public BankQuestionMcq process(NewBankQuestionMcqRequest question) {
        BankQuestionMcq bankQuestionMcq = new BankQuestionMcq();
        super.process(question, bankQuestionMcq);

        bankQuestionMcq.setNumberOfOptions(question.getNumberOfOptions());
        bankQuestionMcq.setOption1(question.getOption1());
        bankQuestionMcq.setOption2(question.getOption2());
        bankQuestionMcq.setOption3(question.getOption3());
        bankQuestionMcq.setOption4(question.getOption4());
        bankQuestionMcq.setOption5(question.getOption5());
        bankQuestionMcq.setOption6(question.getOption6());
        bankQuestionMcq.setNumberOfCorrectAnswers(question.getNumberOfCorrectAnswers());
        if (question.getCorrectAnswer() != null) {
            bankQuestionMcq.setCorrectAnswer(question.getCorrectAnswer()
                .stream().map(String::valueOf).collect(Collectors.joining(",")));
        } else {
            bankQuestionMcq.setCorrectAnswer("");
        }
        return bankQuestionMcq;
    }
}