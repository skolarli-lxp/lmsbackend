package com.skolarli.lmsservice.services;

import com.skolarli.lmsservice.models.db.Exam;
import com.skolarli.lmsservice.models.dto.NewExamQuestionRequest;

import java.util.List;

public interface ExamService {

    Exam getExam(long id);

    List<Exam> getAllExams();

    Exam saveExam(Exam exam);

    List<Exam> saveAllExams(List<Exam> exams);

    Exam addQuestionToExam(NewExamQuestionRequest newExamQuestionRequest, long id);

    Exam updateExams(Exam exam, long id);

    void hardDeleteExam(long id);
}
