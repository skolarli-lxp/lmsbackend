package com.skolarli.lmsservice.services.questionbank;

import com.skolarli.lmsservice.models.db.exam.ExamQuestionTrueOrFalse;
import com.skolarli.lmsservice.models.db.questionbank.BankQuestionTrueOrFalse;
import com.skolarli.lmsservice.models.dto.questionbank.NewBankQuestionTrueOrFalseRequest;

import java.util.List;

public interface QuestionBankTrueOrFalseService {

    BankQuestionTrueOrFalse toBankQuestionTrueOrFalse(
        NewBankQuestionTrueOrFalseRequest newBankQuestionTrueOrFalseRequest);

    List<ExamQuestionTrueOrFalse> toExamQuestionTrueOrFalse(
        List<Long> bankQuestionTrueOrFalse, List<Integer> marks,
        Long examId);

    BankQuestionTrueOrFalse getQuestion(long id);

    List<BankQuestionTrueOrFalse> getAllQuestions();

    List<BankQuestionTrueOrFalse> getQuestionsByParameters(Long courseId, Long batchId,
                                                           Long lessonId, Long chapterId,
                                                           Long studentId);


    BankQuestionTrueOrFalse saveQuestion(BankQuestionTrueOrFalse question);

    List<BankQuestionTrueOrFalse> saveAllQuestions(List<BankQuestionTrueOrFalse> questions);

    Long uploadQuestionsFromCsvBatchJob(String filePath, Long tenantId, String userName);

    BankQuestionTrueOrFalse updateQuestion(BankQuestionTrueOrFalse question, long id);

    void hardDeleteQuestion(long id);
}
