package com.skolarli.lmsservice.services;

import com.skolarli.lmsservice.models.db.BankQuestionSubjective;
import com.skolarli.lmsservice.models.dto.NewBankQuestionSubjectiveRequest;

import java.util.List;

public interface ExamQuestionSubjectiveService {

    BankQuestionSubjective toBankQuestionSubjective(
            NewBankQuestionSubjectiveRequest newBankQuestionSubjectiveRequest);


    BankQuestionSubjective getQuestion(long id);

    List<BankQuestionSubjective> getAllQuestions();


    BankQuestionSubjective saveQuestion(BankQuestionSubjective question);

    List<BankQuestionSubjective> saveAllQuestions(List<BankQuestionSubjective> questions);

    BankQuestionSubjective updateQuestion(BankQuestionSubjective question, long id);

    void hardDeleteQuestion(long id);
}
