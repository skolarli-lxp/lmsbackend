package com.skolarli.lmsservice.services;

import com.skolarli.lmsservice.models.db.Exam;
import com.skolarli.lmsservice.models.dto.exam.DeleteExamQuestionsRequest;
import com.skolarli.lmsservice.models.dto.exam.NewExamQuestionsAllTypesRequest;
import com.skolarli.lmsservice.models.dto.exam.NewExamQuestionsAllTypesResponse;
import com.skolarli.lmsservice.models.dto.exam.NewExamRequest;

import java.util.List;

public interface ExamService {
    Exam toExam(NewExamRequest request);

    Exam getExam(long id);

    List<Exam> getAllExams();

    List<Exam> getAllExamsForCourse(Long courseId);

    List<Exam> getAllExamsForBatch(Long batchId);

    NewExamQuestionsAllTypesResponse getAllQuestions(Long id);

    Exam saveExam(Exam exam);

    Exam addQuestionsToExam(NewExamQuestionsAllTypesRequest newExamQuestionRequest, long id);

    Exam updateExams(Exam exam, long id);

    void hardDeleteExam(long id);

    void deleteQuestions(Long examId, DeleteExamQuestionsRequest request);
}
