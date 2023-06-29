package com.skolarli.lmsservice.services;

import com.skolarli.lmsservice.models.db.BankQuestionMcq;
import com.skolarli.lmsservice.models.dto.NewBankQuestionMcqRequest;

import java.util.List;

public interface ExamQuestionMcqService {

    BankQuestionMcq toBankQuestionMcq(NewBankQuestionMcqRequest newBankQuestionMcqRequest);

    BankQuestionMcq getQuestion(long id);

    List<BankQuestionMcq> getAllQuestions();

    BankQuestionMcq saveQuestion(BankQuestionMcq question);

    List<BankQuestionMcq> saveAllQuestions(List<BankQuestionMcq> questions);

    BankQuestionMcq updateQuestion(BankQuestionMcq question, long id);

    void hardDeleteQuestion(long id);
}
