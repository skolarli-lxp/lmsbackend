package com.skolarli.lmsservice.services.impl.questionbank;

import com.skolarli.lmsservice.models.db.questionbank.BankQuestionMcq;
import org.springframework.batch.item.ItemProcessor;

public class QuestionBankMcqProcessor implements ItemProcessor<BankQuestionMcq, BankQuestionMcq> {
    @Override
    public BankQuestionMcq process(BankQuestionMcq question) {
        return question;
    }
}