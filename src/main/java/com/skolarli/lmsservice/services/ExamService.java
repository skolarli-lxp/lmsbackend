package com.skolarli.lmsservice.services;

import com.skolarli.lmsservice.models.db.Exam;
import com.skolarli.lmsservice.models.dto.exam.NewExamQuestionsAllTypesRequest;
import com.skolarli.lmsservice.models.dto.exam.NewExamRequest;

import java.util.List;

public interface ExamService {

    Exam getExam(long id);

    List<Exam> getAllExams();

    List<NewExamQuestionsAllTypesRequest> getAllQuestions(Long id);

    Exam saveExam(NewExamRequest examRequest);

    List<Exam> saveAllExams(List<NewExamRequest> examRequests);

    Exam addQuestionToExam(NewExamQuestionsAllTypesRequest newExamQuestionRequest, long id);

    Exam updateExams(NewExamRequest exam, long id);

    void hardDeleteExam(long id);
}
