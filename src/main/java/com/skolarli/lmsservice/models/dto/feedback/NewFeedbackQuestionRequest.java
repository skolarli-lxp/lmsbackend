package com.skolarli.lmsservice.models.dto.feedback;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class NewFeedbackQuestionRequest {

    private String questionText;

    private int starRating;

    private String textRemark;

    public Boolean validate() {
        if (questionText == null || questionText.isEmpty()) {
            return false;
        }
        if (starRating < 0 || starRating > 5) {
            return false;
        }
        return true;
    }
}