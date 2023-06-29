package com.skolarli.lmsservice.services;

import com.skolarli.lmsservice.models.db.Exam;
import com.skolarli.lmsservice.models.db.ExamQuestionMcq;
import com.skolarli.lmsservice.models.dto.exam.NewExamQuestionMcqRequest;

import java.util.List;

public interface ExamQuestionMcqService {

    ExamQuestionMcq toExamQuestionMcq(NewExamQuestionMcqRequest newExamQuestionMcqRequest);

    ExamQuestionMcq getQuestion(long id);

    List<ExamQuestionMcq> getAllQuestions();

    ExamQuestionMcq saveQuestion(ExamQuestionMcq question, Exam exam);

    List<ExamQuestionMcq> saveAllQuestions(List<ExamQuestionMcq> questions, Exam exam);

    ExamQuestionMcq updateQuestion(ExamQuestionMcq question, long id);

    void hardDeleteQuestion(long id);
}
