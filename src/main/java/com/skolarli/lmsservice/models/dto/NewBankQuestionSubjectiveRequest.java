package com.skolarli.lmsservice.models.dto;

import com.skolarli.lmsservice.models.AnswerFormat;
import com.skolarli.lmsservice.models.QuestionFormat;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class NewBankQuestionSubjectiveRequest {
    Long courseId;
    private String question;
    private QuestionFormat questionType;
    private AnswerFormat answerType;
    private int wordCount;
    private String sampleAnswerText;
    private String sampleAnswerUrl;
    private String correctAnswer;
}