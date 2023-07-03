package com.skolarli.lmsservice.services;

import com.skolarli.lmsservice.models.db.Exam;
import com.skolarli.lmsservice.models.dto.exam.NewExamQuestionsAllTypesRequest;
import com.skolarli.lmsservice.models.dto.exam.NewExamQuestionsAllTypesResponse;
import com.skolarli.lmsservice.models.dto.exam.NewExamRequest;

import java.util.List;

public interface ExamService {

    Exam getExam(long id);

    List<Exam> getAllExams();

    NewExamQuestionsAllTypesResponse getAllQuestions(Long id);

    Exam saveExam(Exam exam);

    Exam addQuestionsToExam(NewExamQuestionsAllTypesRequest newExamQuestionRequest, long id);

    Exam updateExams(Exam exam, long id);

    void hardDeleteExam(long id);
}
