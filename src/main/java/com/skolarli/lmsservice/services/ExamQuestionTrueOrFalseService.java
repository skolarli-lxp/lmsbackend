package com.skolarli.lmsservice.services;

import com.skolarli.lmsservice.models.db.Exam;
import com.skolarli.lmsservice.models.db.ExamQuestionTrueOrFalse;
import com.skolarli.lmsservice.models.dto.exam.NewExamQuestionTrueOrFalseRequest;

import java.util.List;

public interface ExamQuestionTrueOrFalseService {

    ExamQuestionTrueOrFalse toExamQuestionTrueOrFalse(
            NewExamQuestionTrueOrFalseRequest newExamQuestionTrueOrFalseRequest);


    ExamQuestionTrueOrFalse getQuestion(long id);

    List<ExamQuestionTrueOrFalse> getAllQuestions();


    ExamQuestionTrueOrFalse saveQuestion(ExamQuestionTrueOrFalse question, Exam exam);

    List<ExamQuestionTrueOrFalse> saveAllQuestions(List<ExamQuestionTrueOrFalse> questions,
                                                   Exam exam);

    ExamQuestionTrueOrFalse updateQuestion(ExamQuestionTrueOrFalse question, long id);

    void hardDeleteQuestion(long id);

    void hardDeleteQuestions(List<Long> ids, Exam exam);
}
