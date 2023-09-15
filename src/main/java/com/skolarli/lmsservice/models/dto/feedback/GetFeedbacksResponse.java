package com.skolarli.lmsservice.models.dto.feedback;

import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class GetFeedbacksResponse {

    private Long feedbackQuestionnaireId;

    private Long userId;

    List<FeedbackAnswerResponse> feedbackAnswers;
}