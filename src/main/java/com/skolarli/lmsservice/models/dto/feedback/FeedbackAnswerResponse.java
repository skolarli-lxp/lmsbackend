package com.skolarli.lmsservice.models.dto.feedback;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class FeedbackAnswerResponse {

    Long feedbackQuestionId;

    Long feedbackAnswerId;

    private int starRating;

    private String textRemarks;
}