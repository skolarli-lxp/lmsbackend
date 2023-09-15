package com.skolarli.lmsservice.models.dto.feedback;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class NewFeedbackQuestionRequest {

    private String questionText;

    public Boolean validate() {
        if (questionText == null || questionText.isEmpty()) {
            return false;
        }
        return true;
    }
}