package com.skolarli.lmsservice.services.exam;

import com.skolarli.lmsservice.models.db.exam.Exam;
import com.skolarli.lmsservice.models.db.exam.ExamQuestionSubjective;
import com.skolarli.lmsservice.models.db.questionbank.BankQuestionSubjective;

import java.util.List;

public interface ExamQuestionSubjectiveService {

    List<BankQuestionSubjective> toBankQuestionSubjective(List<Long> examQuestionSubjective);

    ExamQuestionSubjective getQuestion(long id);

    List<ExamQuestionSubjective> getAllQuestions();

    Integer getMaxQuestionSortOrder(Long examId);

    ExamQuestionSubjective saveQuestion(ExamQuestionSubjective question, Exam exam);

    List<ExamQuestionSubjective> saveAllQuestions(List<ExamQuestionSubjective> questions, Exam exam);

    ExamQuestionSubjective updateQuestion(ExamQuestionSubjective question, long id);

    void hardDeleteQuestion(long id);

    void hardDeleteQuestions(List<Long> ids, Exam exam);
}
