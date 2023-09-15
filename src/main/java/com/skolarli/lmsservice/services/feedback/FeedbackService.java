package com.skolarli.lmsservice.services.feedback;

import com.skolarli.lmsservice.models.FeedbackType;
import com.skolarli.lmsservice.models.db.feedback.Feedback;
import com.skolarli.lmsservice.models.db.feedback.FeedbackQuestion;
import com.skolarli.lmsservice.models.db.feedback.FeedbackQuestionnaire;
import com.skolarli.lmsservice.models.dto.feedback.GetFeedbacksResponse;
import com.skolarli.lmsservice.models.dto.feedback.NewFeedbackQuestionRequest;
import com.skolarli.lmsservice.models.dto.feedback.NewFeedbackQuestionnaireRequest;
import com.skolarli.lmsservice.models.dto.feedback.NewFeedbackRequest;

import java.util.List;

public interface FeedbackService {

    Feedback toFeedback(NewFeedbackRequest feedbackRequest);

    List<Feedback> toFeedbacks(List<NewFeedbackRequest> feedbackRequests);

    Feedback getFeedbackById(Long id);

    List<Feedback> getAllFeedbacks();

    List<Feedback> getAllFeedbacksGivenByUser(Long userId);

    List<Feedback> getAllFeedbackForFeedbackQuestionnaire(Long feedbackQuestionnaireId);

    List<Feedback> getAllFeedbacksGivenByUserForFeedbackQuestionnaire(Long userId, Long feedbackQuestionnaireId);

    Feedback createFeedback(Feedback feedback);

    List<Feedback> createFeedbacks(List<Feedback> feedbacks);

    Feedback updateFeedback(Feedback feedback, Long id);

    List<GetFeedbacksResponse> toFeedbackResponse(List<Feedback> feedbacks);

    void deleteFeedback(Long id);


    void deleteFeedbackForQuestionnaire(Long questionnaireId);

    void deletFeedbackForQuestionnaireAndUser(Long questionnaireId, Long userId);
}
