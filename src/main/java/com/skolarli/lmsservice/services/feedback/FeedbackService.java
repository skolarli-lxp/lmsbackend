package com.skolarli.lmsservice.services.feedback;

import com.skolarli.lmsservice.models.FeedbackType;
import com.skolarli.lmsservice.models.db.feedback.Feedback;
import com.skolarli.lmsservice.models.db.feedback.FeedbackQuestion;
import com.skolarli.lmsservice.models.dto.feedback.NewFeedbackQuestionRequest;
import com.skolarli.lmsservice.models.dto.feedback.NewFeedbackRequest;

import java.util.List;

public interface FeedbackService {

    Feedback toFeedback(NewFeedbackRequest feedback);

    FeedbackQuestion toFeedbackQuestion(NewFeedbackQuestionRequest question, Feedback feedback);

    FeedbackQuestion toFeedbackQuestion(NewFeedbackQuestionRequest question);

    Feedback getFeedbackById(Long id);

    List<Feedback> getAllFeedbacks();

    List<Feedback> getFeedbacksByRelatedIdAndType(Long relatedId, FeedbackType type);

    List<Feedback> getAllFeedbacksGivenByUser(Long userId);

    Feedback createFeedback(Feedback feedback);

    Feedback updateFeedback(Feedback feedback);

    Feedback addQuestionToFeedback(Long feedbackId, FeedbackQuestion question);

    Feedback addQuestionsToFeedback(Long feedbackId, List<FeedbackQuestion> questions);

    Feedback updateQuestionInFeedback(Long feedbackId, FeedbackQuestion question);

    Feedback removeQuestionFromFeedback(Long feedbackId, Long questionId);

    void deleteFeedback(Long id);

}
