package com.skolarli.lmsservice.models.dto.exam;

import java.util.List;

public class AnswerBookEvaulationRequest {
    private List<AnswerEvaulationRequest> mcqAnswerEvaluations;
    private List<AnswerEvaulationRequest> subjectiveAnswerEvaluations;
    private List<AnswerEvaulationRequest> trueFalseAnswerEvaluations;
}
