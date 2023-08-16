package com.skolarli.lmsservice.models.dto.exam;

import com.skolarli.lmsservice.models.EvaluationResult;

public class NewAnswerBookEvaulationRequest {
    Long answerBookAnswerId;
    Double marksGiven;
    String evaluatorRemarks;

    EvaluationResult evaluationResult;
}
