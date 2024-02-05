package com.skolarli.lmsservice.services.questionbank;

import com.skolarli.lmsservice.models.db.exam.ExamQuestionMcq;
import com.skolarli.lmsservice.models.db.questionbank.BankQuestionMcq;
import com.skolarli.lmsservice.models.dto.questionbank.NewBankQuestionMcqRequest;

import java.util.List;

public interface QuestionBankMcqService {

    BankQuestionMcq toBankQuestionMcq(NewBankQuestionMcqRequest newBankQuestionMcqRequest);

    List<ExamQuestionMcq> toExamQuestionMcq(List<Long> bankQuestionMcq,
                                            List<Integer> marks,
                                            Long examId);

    BankQuestionMcq getQuestion(long id);

    List<BankQuestionMcq> getAllQuestions();

    List<BankQuestionMcq> getQuestionsByParameters(Long courseId, Long batchId,
                                                   Long lessonId, Long chapterId,
                                                   Long studentId);

    BankQuestionMcq saveQuestion(BankQuestionMcq question);


    List<BankQuestionMcq> saveAllQuestions(List<BankQuestionMcq> questions);

    Long uploadQuestionsFromCsvBatchJob(String filePath);

    BankQuestionMcq updateQuestion(BankQuestionMcq question, long id);

    void hardDeleteQuestion(long id);
}
