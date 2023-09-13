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
}