package com.skolarli.lmsservice.models.dto.exam;

import com.skolarli.lmsservice.models.EvaluationResult;

public class AnswerEvaulationRequest {
    private Long answerBookAnswerId;
    private Double marksGiven;
    private String evaluatorRemarks;
    private EvaluationResult evaluationResult;
}
