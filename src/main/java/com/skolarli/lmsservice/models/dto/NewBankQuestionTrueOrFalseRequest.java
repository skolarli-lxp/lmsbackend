package com.skolarli.lmsservice.models.dto;

import com.skolarli.lmsservice.models.AnswerFormat;
import com.skolarli.lmsservice.models.QuestionFormat;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class NewBankQuestionTrueOrFalseRequest {
    Long courseId;
    private String question;
    private QuestionFormat questionType;
    private AnswerFormat answerType;
    private String option1 = "True";
    private String option2 = "False";
    private String sampleAnswerText;
    private String sampleAnswerUrl;
    private String correctAnswer;

}