package com.skolarli.lmsservice.services.impl.feedback;

import com.skolarli.lmsservice.exception.ResourceNotFoundException;
import com.skolarli.lmsservice.models.db.feedback.Feedback;
import com.skolarli.lmsservice.models.db.feedback.FeedbackQuestion;
import com.skolarli.lmsservice.models.dto.feedback.FeedbackAnswerResponse;
import com.skolarli.lmsservice.models.dto.feedback.GetFeedbacksResponse;
import com.skolarli.lmsservice.models.dto.feedback.NewFeedbackRequest;
import com.skolarli.lmsservice.repository.feedback.FeedbackRepository;
import com.skolarli.lmsservice.services.core.LmsUserService;
import com.skolarli.lmsservice.services.feedback.FeedbackQuestionnaireService;
import com.skolarli.lmsservice.services.feedback.FeedbackService;
import com.skolarli.lmsservice.utils.UserUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FeedbackServiceImpl implements FeedbackService {

    FeedbackRepository feedbackRepository;

    FeedbackQuestionnaireService feedbackQuestionnaireService;


    LmsUserService lmsUserService;
    UserUtils userUtils;

    public FeedbackServiceImpl(FeedbackRepository feedbackRepository, UserUtils userUtils,
                               FeedbackQuestionnaireService feedbackQuestionnaireService,
                               LmsUserService lmsUserService) {
        this.feedbackRepository = feedbackRepository;
        this.userUtils = userUtils;
        this.lmsUserService = lmsUserService;
        this.feedbackQuestionnaireService = feedbackQuestionnaireService;
    }


    @Override
    public Feedback toFeedback(NewFeedbackRequest feedbackRequest) {
        Feedback feedback = new Feedback();
        FeedbackQuestion feedbackQuestion = feedbackQuestionnaireService.getFeedbackQuestionById(
            feedbackRequest.getFeedbackQuestionId());
        feedback.setFeedbackQuestion(feedbackQuestion);
        feedback.setFeedbackQuestionnaire(feedbackQuestion.getFeedbackQuestionnaire());
        feedback.setStarRating(feedbackRequest.getStarRating());
        feedback.setTextRemark(feedbackRequest.getTextRemarks());
        return feedback;
    }

    @Override
    public List<Feedback> toFeedbacks(List<NewFeedbackRequest> feedbackRequests) {
        List<Feedback> feedbacks = new ArrayList<>();
        for (NewFeedbackRequest feedbackRequest : feedbackRequests) {
            feedbacks.add(toFeedback(feedbackRequest));
        }
        return feedbacks;
    }

    @Override
    public Feedback getFeedbackById(Long id) {
        List<Feedback> feedbacks = feedbackRepository.findAllById(new ArrayList<>(List.of(id)));
        if (feedbacks.size() == 0) {
            throw new ResourceNotFoundException("Feedback with Id " + id + " not found");
        }
        return feedbacks.get(0);
    }


    @Override
    public List<Feedback> getAllFeedbacks() {
        return feedbackRepository.findAll();
    }

    @Override
    public List<Feedback> getAllFeedbacksGivenByUser(Long userId) {
        return feedbackRepository.findAllByCreatedBy_Id(userId);
    }

    @Override
    public List<Feedback> getAllFeedbackForFeedbackQuestionnaire(Long feedbackQuestionnaireId) {
        return feedbackRepository.findAllByFeedbackQuestionnaire_Id(feedbackQuestionnaireId);
    }

    @Override
    public List<Feedback> getAllFeedbacksGivenByUserForFeedbackQuestionnaire(Long userId,
                                                                             Long feedbackQuestionnaireId) {
        return feedbackRepository.findAllByFeedbackQuestionnaire_IdAndCreatedBy_Id(feedbackQuestionnaireId, userId);
    }

    @Override
    public Feedback createFeedback(Feedback feedback) {
        feedback.setCreatedBy(userUtils.getCurrentUser());
        return feedbackRepository.save(feedback);
    }

    @Override
    public List<Feedback> createFeedbacks(List<Feedback> feedbacks) {
        return feedbackRepository.saveAll(feedbacks);
    }

    @Override
    public Feedback updateFeedback(Feedback feedback, Long id) {
        Feedback existingFeedback = getFeedbackById(id);
        existingFeedback.update(feedback);
        existingFeedback.setUpdatedBy(userUtils.getCurrentUser());
        return feedbackRepository.save(existingFeedback);
    }

    @Override
    public List<GetFeedbacksResponse> toFeedbackResponse(List<Feedback> feedbacks) {
        List<GetFeedbacksResponse> feedbackResponses = new ArrayList<>();

        for (Feedback feedback : feedbacks) {
            FeedbackAnswerResponse feedbackAnswerResponse = feedback.toFeedbackAnswerResponse();

            Long questionnaireId = feedback.getFeedbackQuestionnaire().getId();
            Long userId = feedback.getCreatedBy().getId();

            for (GetFeedbacksResponse feedbackResponse : feedbackResponses) {
                if (feedbackResponse.getFeedbackQuestionnaireId().equals(questionnaireId)
                    && feedbackResponse.getUserId().equals(userId)) {
                    feedbackResponse.getFeedbackAnswers().add(feedback.toFeedbackAnswerResponse());
                    break;
                }
                GetFeedbacksResponse newFeedbackResponse = new GetFeedbacksResponse();
                newFeedbackResponse.setFeedbackQuestionnaireId(questionnaireId);
                newFeedbackResponse.setUserId(userId);
                newFeedbackResponse.setFeedbackAnswers(new ArrayList<>(List.of(feedbackAnswerResponse)));
                feedbackResponses.add(newFeedbackResponse);
            }
        }
        return feedbackResponses;
    }

    @Override
    public void deleteFeedback(Long id) {
        Feedback existingFeedback = getFeedbackById(id);
        feedbackRepository.delete(existingFeedback);
    }
}