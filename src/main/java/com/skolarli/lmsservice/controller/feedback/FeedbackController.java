package com.skolarli.lmsservice.controller.feedback;

import com.skolarli.lmsservice.models.db.feedback.Feedback;
import com.skolarli.lmsservice.models.dto.feedback.GetFeedbacksResponse;
import com.skolarli.lmsservice.models.dto.feedback.NewFeedbackRequest;
import com.skolarli.lmsservice.services.feedback.FeedbackService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/feedback")
public class FeedbackController {
    final Logger logger = LoggerFactory.getLogger(FeedbackController.class);

    FeedbackService feedbackService;

    public FeedbackController(FeedbackService feedbackService) {
        this.feedbackService = feedbackService;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    public ResponseEntity<Feedback> getFeedbackById(@PathVariable Long id) {
        UUID uuid = UUID.randomUUID();
        MDC.put("requestId", uuid.toString());
        logger.info("Received request for getFeedbackById for id: " + id);

        Feedback feedback = feedbackService.getFeedbackById(id);
        return ResponseEntity.ok(feedback);
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<GetFeedbacksResponse>> getAllFeedbacks(@RequestParam(required = false)
                                                                      Long questionnaireId,
                                                                      @RequestParam(required = false)
                                                                      Long givenByUserId) {
        UUID uuid = UUID.randomUUID();
        MDC.put("requestId", uuid.toString());
        logger.info("Received request for getAllFeedbacks"
            + (questionnaireId != null ? " for questionnaireId: " + questionnaireId : "")
            + (givenByUserId != null ? " for givenByUserId: " + givenByUserId : ""));

        List<Feedback> response = null;

        if (givenByUserId != null) {
            if (questionnaireId != null) {
                response = feedbackService.getAllFeedbacksGivenByUserForFeedbackQuestionnaire(
                    givenByUserId, questionnaireId);
            } else {
                response = feedbackService.getAllFeedbacksGivenByUser(givenByUserId);
            }
        } else {
            if (questionnaireId != null) {
                response = feedbackService.getAllFeedbackForFeedbackQuestionnaire(questionnaireId);
            } else {
                response = feedbackService.getAllFeedbacks();
            }
        }
        return ResponseEntity.ok(feedbackService.toFeedbackResponse(response));
    }


    @RequestMapping(method = RequestMethod.POST, value = "/createsingle")
    public ResponseEntity<Feedback> createFeedback(@RequestBody NewFeedbackRequest request) {
        UUID uuid = UUID.randomUUID();
        MDC.put("requestId", uuid.toString());
        logger.info("Received request for createFeedback");

        if (!request.validate()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid request");
        }
        Feedback newFeedback = feedbackService.toFeedback(request);
        Feedback createdFeedback = feedbackService.createFeedback(newFeedback);
        return ResponseEntity.ok(createdFeedback);
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<List<GetFeedbacksResponse>> createFeedbacks(@RequestBody List<NewFeedbackRequest> requests,
                                                                      @RequestParam Long questionnaireId) {
        UUID uuid = UUID.randomUUID();
        MDC.put("requestId", uuid.toString());
        logger.info("Received request for createFeedbacks for questionnaireId: " + questionnaireId);

        for (NewFeedbackRequest request : requests) {
            if (!request.validate()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid request");
            }
        }
        List<Feedback> newFeedbacks = feedbackService.toFeedbacks(requests);
        List<Feedback> createdFeedbacks = feedbackService.createFeedbacks(newFeedbacks);
        return ResponseEntity.ok(feedbackService.toFeedbackResponse(createdFeedbacks));
    }


    @RequestMapping(method = RequestMethod.PUT, value = "/{id}")
    public ResponseEntity<Feedback> updateFeedback(@PathVariable Long id,
                                                   @RequestBody NewFeedbackRequest request) {
        UUID uuid = UUID.randomUUID();
        MDC.put("requestId", uuid.toString());
        logger.info("Received request for updateFeedback for id: " + id);

        Feedback newFeedback = feedbackService.toFeedback(request);
        Feedback updated = feedbackService.updateFeedback(newFeedback, id);
        return ResponseEntity.ok(updated);
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
    public ResponseEntity<Void> deleteFeedback(@PathVariable Long id) {
        UUID uuid = UUID.randomUUID();
        MDC.put("requestId", uuid.toString());
        logger.info("Received request for deleteFeedback for id: " + id);

        feedbackService.deleteFeedback(id);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/forquestionnaire/{id}")
    public ResponseEntity<Void> deleteFeedbackForQuestionnaire(@PathVariable Long id) {
        UUID uuid = UUID.randomUUID();
        MDC.put("requestId", uuid.toString());
        logger.info("Received request for deleteFeedbackForQuestionnaire for id: " + id);

        feedbackService.deleteFeedbackForQuestionnaire(id);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/forquestionnaireforuser")
    public ResponseEntity<Void> deleteFeedbackForQuestionnaireForUser(@RequestParam Long questionnaireId,
                                                                       @RequestParam Long userId) {
        UUID uuid = UUID.randomUUID();
        MDC.put("requestId", uuid.toString());
        logger.info("Received request for deleteFeedbackForQuestionnaireForUser for id: "
            + questionnaireId + " and userId: "
            + userId);

        feedbackService.deletFeedbackForQuestionnaireAndUser(questionnaireId, userId);
        return ResponseEntity.ok().build();
    }
}
