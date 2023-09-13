package com.skolarli.lmsservice.controller.feedback;

import com.skolarli.lmsservice.models.FeedbackType;
import com.skolarli.lmsservice.models.db.exam.Exam;
import com.skolarli.lmsservice.models.db.feedback.Feedback;
import com.skolarli.lmsservice.models.db.feedback.FeedbackQuestion;
import com.skolarli.lmsservice.models.dto.feedback.NewFeedbackQuestionRequest;
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
import java.util.stream.Collectors;

@RestController
@RequestMapping("/feedback")
public class FeedbackController {
    final Logger logger = LoggerFactory.getLogger(FeedbackController.class);

    FeedbackService feedbackService;

    public FeedbackController(FeedbackService feedbackService) {
        this.feedbackService = feedbackService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<Feedback>> getAllFeedbacks(@RequestParam(required = false) Long targetId,
                                                          @RequestParam(required = false) FeedbackType feedbackType,
                                                          @RequestParam(required = false) Long givenByUserId) {
        UUID uuid = UUID.randomUUID();
        MDC.put("requestId", uuid.toString());
        logger.info("Received request for getAllFeedbacks"
            + (targetId != null ? " for targetId: " + targetId : "")
            + (feedbackType != null ? " for feedbackType: " + feedbackType : "")
            + (givenByUserId != null ? " for givenByUserId: " + givenByUserId : ""));


        List<Feedback> feedbacks = feedbackService.queryFeedbacks(targetId, feedbackType, givenByUserId);
        return ResponseEntity.ok(feedbacks);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    public ResponseEntity<Feedback> getFeedbackById(@PathVariable Long id) {
        UUID uuid = UUID.randomUUID();
        MDC.put("requestId", uuid.toString());
        logger.info("Received request for getFeedbackById for id: " + id);

        Feedback feedback = feedbackService.getFeedbackById(id);
        return ResponseEntity.ok(feedback);
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Feedback> createFeedback(@RequestBody NewFeedbackRequest request) {
        UUID uuid = UUID.randomUUID();
        MDC.put("requestId", uuid.toString());
        logger.info("Received request for createFeedback for : " + request.getFeedbackType());

        if (!request.validateFields()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid request");
        }
        Feedback newFeedback = feedbackService.toFeedback(request);
        feedbackService.createFeedback(newFeedback);
        return ResponseEntity.ok(newFeedback);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/{id}")
    public ResponseEntity<Feedback> updateFeedback(@PathVariable Long id, @RequestBody NewFeedbackRequest request) {
        UUID uuid = UUID.randomUUID();
        MDC.put("requestId", uuid.toString());
        logger.info("Received request for updateFeedback for id: " + id);

        Feedback updatedFeedback = feedbackService.toFeedback(request);
        return ResponseEntity.ok(feedbackService.updateFeedback(updatedFeedback, id));
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/{id}/addQuestion")
    public ResponseEntity<Feedback> addQuestionToFeedback(@PathVariable Long id,
                                                          @RequestBody NewFeedbackQuestionRequest request) {
        UUID uuid = UUID.randomUUID();
        MDC.put("requestId", uuid.toString());
        logger.info("Received request for addQuestionToFeedback for id: " + id);

        if (!request.validate()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid request");
        }
        FeedbackQuestion question = feedbackService.toFeedbackQuestion(request);
        return ResponseEntity.ok(feedbackService.addQuestionToFeedback(id, question));
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/{id}/addQuestions")
    public ResponseEntity<Feedback> addQuestionsToFeedback(@PathVariable Long id,
                                                           @RequestBody List<NewFeedbackQuestionRequest> request) {
        UUID uuid = UUID.randomUUID();
        MDC.put("requestId", uuid.toString());
        logger.info("Received request for addQuestionsToFeedback for id: " + id);

        for (NewFeedbackQuestionRequest question : request) {
            if (!question.validate()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid request");
            }
        }
        List<FeedbackQuestion> questions = request.stream().map(feedbackService::toFeedbackQuestion).collect(
            Collectors.toList());
        return ResponseEntity.ok(feedbackService.addQuestionsToFeedback(id, questions));
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/{id}/updateQuestion")
    public ResponseEntity<Feedback> updateQuestionInFeedback(@PathVariable Long feedbackId,
                                                             @RequestParam Long questionId,
                                                             @RequestBody NewFeedbackQuestionRequest request) {
        UUID uuid = UUID.randomUUID();
        MDC.put("requestId", uuid.toString());
        logger.info("Received request for updateQuestionInFeedback for id: " + feedbackId);

        if (!request.validate()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid request");
        }
        FeedbackQuestion question = feedbackService.toFeedbackQuestion(request);
        question.setId(questionId);
        return ResponseEntity.ok(feedbackService.updateQuestionInFeedback(feedbackId, question));
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
    public ResponseEntity<Void> deleteFeedback(@PathVariable Long id) {
        UUID uuid = UUID.randomUUID();
        MDC.put("requestId", uuid.toString());
        logger.info("Received request for deleteFeedback for id: " + id);

        feedbackService.deleteFeedback(id);
        return ResponseEntity.ok().build();
    }
}
