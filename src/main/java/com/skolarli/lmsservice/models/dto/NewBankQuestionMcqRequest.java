package com.skolarli.lmsservice.models.dto;

import com.skolarli.lmsservice.models.AnswerFormat;
import com.skolarli.lmsservice.models.QuestionFormat;
import lombok.*;

import java.util.List;
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

    private  String questionType;

    @NotNull
    private QuestionFormat questionFormat;
    @NotNull
    private AnswerFormat answerFormat;

    private String sampleAnswerText;

    private String sampleAnswerUrl;
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