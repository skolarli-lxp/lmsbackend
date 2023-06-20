package com.skolarli.lmsservice.models.dto;

import com.skolarli.lmsservice.models.AnswerFormat;
import com.skolarli.lmsservice.models.QuestionFormat;
import lombok.*;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class NewBankQuestionSubjectiveRequest {

    Long courseId;

    @NotNull
    private String question;

    private String questionType;

    @NotNull
    private QuestionFormat questionFormat;
    @NotNull
    private AnswerFormat answerFormat;

    private String sampleAnswerText;

    private String sampleAnswerUrl;

    private int wordCount;

    private String correctAnswer;
}