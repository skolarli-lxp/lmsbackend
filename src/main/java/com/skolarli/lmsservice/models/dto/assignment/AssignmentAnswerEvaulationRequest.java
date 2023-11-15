package com.skolarli.lmsservice.models.dto.assignment;

import com.skolarli.lmsservice.models.EvaluationResult;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AssignmentAnswerEvaulationRequest {
    private Long answerBookAnswerId;
    private Double marksGiven;
    private String evaluatorRemarks;
    private EvaluationResult evaluationResult;
}
