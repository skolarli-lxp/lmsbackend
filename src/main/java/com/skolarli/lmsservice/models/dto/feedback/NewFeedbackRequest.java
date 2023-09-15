package com.skolarli.lmsservice.models.dto.feedback;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class NewFeedbackRequest {

    private Long feedbackQuestionId;

    private int starRating;

    private String textRemarks;

    public Boolean validate() {
        return feedbackQuestionId != null && starRating >= 1 && starRating <= 5;
    }
}