package com.skolarli.lmsservice.services.exam;

import com.skolarli.lmsservice.models.db.exam.Exam;
import com.skolarli.lmsservice.models.db.exam.ExamQuestionMcq;
import com.skolarli.lmsservice.models.db.questionbank.BankQuestionMcq;

import java.util.List;

public interface ExamQuestionMcqService {

    List<BankQuestionMcq> toBankQuestionMcq(List<Long> examQuestionMcq);

    ExamQuestionMcq getQuestion(long id);

    List<ExamQuestionMcq> getAllQuestions();

    Integer getMaxQuestionSortOrder(Long examId);

    ExamQuestionMcq saveQuestion(ExamQuestionMcq question, Exam exam);

    List<ExamQuestionMcq> saveAllQuestions(List<ExamQuestionMcq> questions, Exam exam);

    ExamQuestionMcq updateQuestion(ExamQuestionMcq question, long id);

    void hardDeleteQuestion(long id);

    void hardDeleteQuestions(List<Long> ids, Exam exam);
}