package com.skolarli.lmsservice.models.dto.exam;

import com.skolarli.lmsservice.models.constraints.ValidTrueFalseValue;
import com.skolarli.lmsservice.models.db.ExamQuestionTrueOrFalse;
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

    public ExamQuestionTrueOrFalse toExamQuestionTrueOrFalse() {
        ExamQuestionTrueOrFalse examQuestionTrueOrFalse = new ExamQuestionTrueOrFalse();
        super.toExamQuestion(examQuestionTrueOrFalse);
        examQuestionTrueOrFalse.setOption1(option1);
        examQuestionTrueOrFalse.setOption2(option2);
        examQuestionTrueOrFalse.setCorrectAnswer(correctAnswer);
        return examQuestionTrueOrFalse;
    }
}