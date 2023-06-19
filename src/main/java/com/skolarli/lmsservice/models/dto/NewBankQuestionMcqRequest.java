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
public class NewBankQuestionMcqRequest {

    Long courseId;

    @NotNull
    private String question;
    @NotNull
    private int numberOfAnswers;
    private QuestionFormat questionType;
    @NotNull
    private AnswerFormat answerType;
    private String answer1;
    private String answer2;
    private String answer3;
    private String answer4;
    private String answer5;
    private String answer6;
    private String correctAnswer;
}