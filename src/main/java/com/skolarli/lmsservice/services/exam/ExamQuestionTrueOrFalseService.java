package com.skolarli.lmsservice.services.exam;

import com.skolarli.lmsservice.models.db.exam.Exam;
import com.skolarli.lmsservice.models.db.exam.ExamQuestionTrueOrFalse;
import com.skolarli.lmsservice.models.db.questionbank.BankQuestionTrueOrFalse;

import java.util.List;

public interface ExamQuestionTrueOrFalseService {

    List<BankQuestionTrueOrFalse> toBankQuestionTrueOrFalse(List<Long> examQuestionTrueOrFalse);

    ExamQuestionTrueOrFalse getQuestion(long id);

    List<ExamQuestionTrueOrFalse> getAllQuestions();

    Integer getMaxQuestionSortOrder(Long examId);

    ExamQuestionTrueOrFalse saveQuestion(ExamQuestionTrueOrFalse question, Exam exam);

    List<ExamQuestionTrueOrFalse> saveAllQuestions(List<ExamQuestionTrueOrFalse> questions, Exam exam);

    ExamQuestionTrueOrFalse updateQuestion(ExamQuestionTrueOrFalse question, long id);

    void hardDeleteQuestion(long id);

    void hardDeleteQuestions(List<Long> ids, Exam exam);
}
