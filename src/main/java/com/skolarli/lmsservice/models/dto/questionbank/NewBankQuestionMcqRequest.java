package com.skolarli.lmsservice.models.dto.questionbank;

import lombok.*;

import java.util.List;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class NewBankQuestionMcqRequest extends NewQuestionBankQuestionRequest {
    @NotNull
    private int numberOfOptions;

    private String option1;
    private String option2;
    private String option3;
    private String option4;
    private String option5;
    private String option6;

    private int numberOfCorrectAnswers;
    List<Integer> correctAnswer;
}