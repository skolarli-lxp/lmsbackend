package com.skolarli.lmsservice.models.dto.exam.answerbook;

import com.skolarli.lmsservice.models.EvaluationResult;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AnswerEvaulationRequest {
    private Long answerBookAnswerId;
    private Double marksGiven;
    private String evaluatorRemarks;
    private EvaluationResult evaluationResult;
}
