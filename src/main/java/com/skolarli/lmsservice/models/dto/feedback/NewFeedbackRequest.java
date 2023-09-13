package com.skolarli.lmsservice.models.dto.feedback;

import com.skolarli.lmsservice.models.FeedbackType;
import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class NewFeedbackRequest {

    private FeedbackType feedbackType;

    private Long batchId;

    private Long batchScheduleId;

    private Long studentId;

    private Long trainerId;

    private Long givenByUserId;

    private List<NewFeedbackQuestionRequest> questions;

    public Boolean validateFields() {
        if (feedbackType == null) {
            return false;
        }
        if (feedbackType == FeedbackType.STUDENT) {
            if (studentId == null) {
                return false;
            }
        } else if (feedbackType == FeedbackType.TRAINER) {
            if (trainerId == null) {
                return false;
            }
        } else if (feedbackType == FeedbackType.BATCH) {
            if (batchId == null) {
                return false;
            }
        } else if (feedbackType == FeedbackType.BATCH_SESSION) {
            if (batchScheduleId == null) {
                return false;
            }
        } else {
            return false;
        }
        if (questions != null && !questions.isEmpty()) {
            for (NewFeedbackQuestionRequest question : questions) {
                if (question.getQuestionText() == null || question.getQuestionText().isEmpty()) {
                    return false;
                }
                if (question.getStarRating() < 0 || question.getStarRating() > 5) {
                    return false;
                }
            }
        }
        return true;
    }
}