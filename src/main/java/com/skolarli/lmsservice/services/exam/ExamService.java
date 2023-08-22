package com.skolarli.lmsservice.services.exam;

import com.skolarli.lmsservice.models.db.exam.Exam;
import com.skolarli.lmsservice.models.dto.exam.exam.*;

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

    Exam updateSortOrder(QuestionSortOrderRequest questionSortOrderRequest, Long examId);

    Exam updateExamQuestion(NewExamQuestionRequest newExamQuestionRequest, Long examId,
                            Long questionId, String questionType);

    Exam nullifyFields(List<String> fieldNames, long id);

    void hardDeleteExam(long id);

    void deleteQuestions(Long examId, DeleteExamQuestionsRequest request);

    AddExamQuestionToQbResponse addExamQuestionToQuestionBank(AddExamQuestionToQbRequest
                                                                      request);
}
