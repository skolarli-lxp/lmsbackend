package com.skolarli.lmsservice.models.dto.feedback;

import com.skolarli.lmsservice.models.FeedbackFrom;
import com.skolarli.lmsservice.models.FeedbackType;
import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class NewFeedbackQuestionnaireRequest {

    private FeedbackType feedbackType;

    private String feedbackQuestionnaireName;

    private FeedbackFrom feedbackFrom;

    private Long courseId;

    private Long batchId;

    private Long batchScheduleId;

    private Long studentId;

    private Long trainerId;

    private List<NewFeedbackQuestionRequest> questions;

    public Boolean validateFields() {
        if (feedbackType == null) {
            return false;
        }
        if (questions != null && !questions.isEmpty()) {
            for (NewFeedbackQuestionRequest question : questions) {
                if (question.getQuestionText() == null || question.getQuestionText().isEmpty()) {
                    return false;
                }
            }
        }
        return true;
    }
}