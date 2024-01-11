package com.skolarli.lmsservice.services.questionbank;

import com.skolarli.lmsservice.models.db.exam.ExamQuestionSubjective;
import com.skolarli.lmsservice.models.db.questionbank.BankQuestionSubjective;
import com.skolarli.lmsservice.models.dto.questionbank.NewBankQuestionSubjectiveRequest;

import java.util.List;

public interface QuestionBankSubjectiveService {

    BankQuestionSubjective toBankQuestionSubjective(
            NewBankQuestionSubjectiveRequest newBankQuestionSubjectiveRequest);

    List<ExamQuestionSubjective> toExamQuestionSubjective(
            List<Long> bankQuestionSubjective,
            List<Integer> marks,
            Long examId);

    BankQuestionSubjective getQuestion(long id);

    List<BankQuestionSubjective> getAllQuestions();

    List<BankQuestionSubjective> getQuestionsByParameters(Long courseId, Long batchId,
                                                           Long lessonId, Long chapterId,
                                                           Long studentId);


    BankQuestionSubjective saveQuestion(BankQuestionSubjective question);

    List<BankQuestionSubjective> saveAllQuestions(List<BankQuestionSubjective> questions);

    BankQuestionSubjective updateQuestion(BankQuestionSubjective question, long id);

    void hardDeleteQuestion(long id);
}
