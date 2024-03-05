package com.skolarli.lmsservice.models.dto.questionbank;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class NewBankQuestionSubjectiveRequest extends NewQuestionBankQuestionRequest {
    private Integer wordCount;

    private String correctAnswer;
}