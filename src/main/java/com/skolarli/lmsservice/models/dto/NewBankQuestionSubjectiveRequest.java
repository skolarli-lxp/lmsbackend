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
public class NewBankQuestionSubjectiveRequest extends NewQuestionBankQuestionRequest {
    private int wordCount;

    private String correctAnswer;
}