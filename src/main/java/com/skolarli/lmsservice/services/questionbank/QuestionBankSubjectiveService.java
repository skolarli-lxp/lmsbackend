package com.skolarli.lmsservice.services.questionbank;

import com.skolarli.lmsservice.models.db.questionbank.BankQuestionSubjective;
import com.skolarli.lmsservice.models.dto.questionbank.NewBankQuestionSubjectiveRequest;

import java.util.List;

public interface QuestionBankSubjectiveService {

    BankQuestionSubjective toBankQuestionSubjective(
            NewBankQuestionSubjectiveRequest newBankQuestionSubjectiveRequest);


    BankQuestionSubjective getQuestion(long id);

    List<BankQuestionSubjective> getAllQuestions();


    BankQuestionSubjective saveQuestion(BankQuestionSubjective question);

    List<BankQuestionSubjective> saveAllQuestions(List<BankQuestionSubjective> questions);

    BankQuestionSubjective updateQuestion(BankQuestionSubjective question, long id);

    void hardDeleteQuestion(long id);
}
