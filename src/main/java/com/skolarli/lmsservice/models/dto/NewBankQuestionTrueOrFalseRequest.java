package com.skolarli.lmsservice.models.dto;

import com.skolarli.lmsservice.models.AnswerFormat;
import com.skolarli.lmsservice.models.QuestionFormat;
import com.skolarli.lmsservice.models.constraints.ValidTrueFalseValue;
import lombok.*;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class NewBankQuestionTrueOrFalseRequest extends NewQuestionBankQuestionRequest {



    private String option1 = "True";
    private String option2 = "False";

    // Should be 1 or 2
    @ValidTrueFalseValue
    private Integer correctAnswer;
}