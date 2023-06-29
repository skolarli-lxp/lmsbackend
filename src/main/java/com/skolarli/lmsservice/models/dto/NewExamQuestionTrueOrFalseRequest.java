package com.skolarli.lmsservice.models.dto;

import com.skolarli.lmsservice.models.constraints.ValidTrueFalseValue;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class NewExamQuestionTrueOrFalseRequest extends NewExamQuestionRequest {


    private String option1 = "True";
    private String option2 = "False";

    // Should be 1 or 2
    @ValidTrueFalseValue
    private Integer correctAnswer;
}