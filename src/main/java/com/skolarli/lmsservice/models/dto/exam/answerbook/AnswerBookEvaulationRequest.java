package com.skolarli.lmsservice.models.dto.exam.answerbook;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AnswerBookEvaulationRequest {
    private List<AnswerEvaulationRequest> mcqAnswerEvaluations;
    private List<AnswerEvaulationRequest> subjectiveAnswerEvaluations;
    private List<AnswerEvaulationRequest> trueFalseAnswerEvaluations;
}
