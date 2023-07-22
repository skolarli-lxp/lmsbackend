package com.skolarli.lmsservice.services;

import com.skolarli.lmsservice.models.db.questionbank.BankQuestionTrueOrFalse;
import com.skolarli.lmsservice.models.dto.questionbank.NewBankQuestionTrueOrFalseRequest;

import java.util.List;

public interface QuestionBankTrueOrFalseService {

    BankQuestionTrueOrFalse toBankQuestionTrueOrFalse(
            NewBankQuestionTrueOrFalseRequest newBankQuestionTrueOrFalseRequest);


    BankQuestionTrueOrFalse getQuestion(long id);

    List<BankQuestionTrueOrFalse> getAllQuestions();


    BankQuestionTrueOrFalse saveQuestion(BankQuestionTrueOrFalse question);

    List<BankQuestionTrueOrFalse> saveAllQuestions(List<BankQuestionTrueOrFalse> questions);

    BankQuestionTrueOrFalse updateQuestion(BankQuestionTrueOrFalse question, long id);

    void hardDeleteQuestion(long id);
}
