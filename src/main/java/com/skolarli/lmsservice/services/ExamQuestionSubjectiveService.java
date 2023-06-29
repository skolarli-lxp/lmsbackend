package com.skolarli.lmsservice.services;

import com.skolarli.lmsservice.models.db.Exam;
import com.skolarli.lmsservice.models.db.ExamQuestionSubjective;
import com.skolarli.lmsservice.models.dto.NewExamQuestionSubjectiveRequest;

import java.util.List;

public interface ExamQuestionSubjectiveService {

    ExamQuestionSubjective toExamQuestionSubjective(
            NewExamQuestionSubjectiveRequest newExamQuestionSubjectiveRequest);


    ExamQuestionSubjective getQuestion(long id);

    List<ExamQuestionSubjective> getAllQuestions();


    ExamQuestionSubjective saveQuestion(ExamQuestionSubjective question, Exam exam);

    List<ExamQuestionSubjective> saveAllQuestions(List<ExamQuestionSubjective> questions,
                                                  Exam exam);

    ExamQuestionSubjective updateQuestion(ExamQuestionSubjective question, long id);

    void hardDeleteQuestion(long id);
}
