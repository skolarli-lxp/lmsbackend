package com.skolarli.lmsservice.models.dto.exam;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class NewExamQuestionSubjectiveRequest extends NewExamQuestionRequest {
    private int wordCount;

    private String correctAnswer;
}