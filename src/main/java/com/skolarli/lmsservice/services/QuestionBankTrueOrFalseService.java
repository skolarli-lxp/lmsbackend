package com.skolarli.lmsservice.services;

import com.skolarli.lmsservice.models.db.BankQuestionMcq;
import com.skolarli.lmsservice.models.db.BankQuestionSubjective;
import com.skolarli.lmsservice.models.db.BankQuestionTrueOrFalse;
import com.skolarli.lmsservice.models.dto.NewBankQuestionMcqRequest;
import com.skolarli.lmsservice.models.dto.NewBankQuestionTrueOrFalseRequest;

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