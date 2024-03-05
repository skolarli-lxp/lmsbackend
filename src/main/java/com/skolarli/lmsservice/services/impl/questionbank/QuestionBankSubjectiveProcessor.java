package com.skolarli.lmsservice.services.impl.questionbank;

import com.skolarli.lmsservice.models.db.questionbank.BankQuestionSubjective;
import com.skolarli.lmsservice.models.dto.questionbank.NewBankQuestionSubjectiveRequest;
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
public class QuestionBankSubjectiveProcessor extends QuestionBankProcessor
    implements ItemProcessor<NewBankQuestionSubjectiveRequest, BankQuestionSubjective> {


    CourseService courseService;
    BatchService batchService;
    LessonService lessonService;
    ChapterService chapterService;
    LmsUserService lmsUserService;

    UserUtils userUtils;

    public QuestionBankSubjectiveProcessor(CourseService courseService,
                                           BatchService batchService,
                                           LessonService lessonService,
                                           ChapterService chapterService,
                                           LmsUserService lmsUserService,
                                           UserUtils userUtils) {
        super(courseService, batchService, lessonService, chapterService, lmsUserService, userUtils);
    }

    @Override
    public BankQuestionSubjective process(NewBankQuestionSubjectiveRequest question) {
        BankQuestionSubjective bankQuestionSubjective = new BankQuestionSubjective();
        super.process(question, bankQuestionSubjective);
        bankQuestionSubjective.setWordCount(question.getWordCount());
        bankQuestionSubjective.setCorrectAnswer(question.getCorrectAnswer());
        return bankQuestionSubjective;
    }
}