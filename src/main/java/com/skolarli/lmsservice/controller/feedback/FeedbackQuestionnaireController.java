package com.skolarli.lmsservice.controller.feedback;

import com.skolarli.lmsservice.models.FeedbackType;
import com.skolarli.lmsservice.models.db.feedback.FeedbackQuestion;
import com.skolarli.lmsservice.models.db.feedback.FeedbackQuestionnaire;
import com.skolarli.lmsservice.models.dto.feedback.NewFeedbackQuestionRequest;
import com.skolarli.lmsservice.models.dto.feedback.NewFeedbackQuestionnaireRequest;
import com.skolarli.lmsservice.services.feedback.FeedbackQuestionnaireService;
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
@RequestMapping("/feedbackquestionnaire")
public class FeedbackQuestionnaireController {
    final Logger logger = LoggerFactory.getLogger(FeedbackQuestionnaireController.class);

    FeedbackQuestionnaireService feedbackQuestionnaireService;

    public FeedbackQuestionnaireController(FeedbackQuestionnaireService feedbackQuestionnaireService) {
        this.feedbackQuestionnaireService = feedbackQuestionnaireService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<FeedbackQuestionnaire>> getAllFeedbackQuestionnaires(@RequestParam(required = false)
                                                                                    Long targetId,
                                                                                    @RequestParam(required = false)
                                                                                    FeedbackType feedbackType) {
        UUID uuid = UUID.randomUUID();
        MDC.put("requestId", uuid.toString());
        logger.info("Received request for getAllFeedbackQuestionnaires"
            + (targetId != null ? " for targetId: " + targetId : "")
            + (feedbackType != null ? " for feedbackType: " + feedbackType : ""));

        List<FeedbackQuestionnaire> feedbackQuestionnaires = null;

        if (feedbackType != null) {
            if (targetId != null) {
                feedbackQuestionnaires = feedbackQuestionnaireService
                    .getAllFeedbackQuestionnaireByFeedbackTypeAndTargetId(feedbackType, targetId);
            } else {
                feedbackQuestionnaires = feedbackQuestionnaireService
                    .getAllFeedbackQuestionnairesByFeedbackType(feedbackType);
            }
        } else {
            feedbackQuestionnaires = feedbackQuestionnaireService.getAllFeedbackQuestionnaires();
        }

        return ResponseEntity.ok(feedbackQuestionnaires);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    public ResponseEntity<FeedbackQuestionnaire> getFeedbackQuestionnaireById(@PathVariable Long id) {
        UUID uuid = UUID.randomUUID();
        MDC.put("requestId", uuid.toString());
        logger.info("Received request for getFeedbackQuestionnaireById for id: " + id);

        FeedbackQuestionnaire feedbackQuestionnaire = feedbackQuestionnaireService.getFeedbackQuestionnaireById(id);
        return ResponseEntity.ok(feedbackQuestionnaire);
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<FeedbackQuestionnaire> createFeedbackkQuestionnaire(@RequestBody
                                                                              NewFeedbackQuestionnaireRequest
                                                                                  request) {
        UUID uuid = UUID.randomUUID();
        MDC.put("requestId", uuid.toString());
        logger.info("Received request for createFeedback for : " + request.getFeedbackType());

        if (!request.validateFields()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid request");
        }
        FeedbackQuestionnaire newFeedbackQuestionnaire = feedbackQuestionnaireService.toFeedbackQuestionnaire(request);
        feedbackQuestionnaireService.createFeedbackQuestionnaire(newFeedbackQuestionnaire);
        return ResponseEntity.ok(newFeedbackQuestionnaire);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/{id}")
    public ResponseEntity<FeedbackQuestionnaire> updateFeedbackQuestionnaire(@PathVariable Long id,
                                                                             @RequestBody
                                                                             NewFeedbackQuestionnaireRequest request) {
        UUID uuid = UUID.randomUUID();
        MDC.put("requestId", uuid.toString());
        logger.info("Received request for updateFeedbackkQuestionnaire for id: " + id);

        FeedbackQuestionnaire updatedFeedbackQuestionnaire = feedbackQuestionnaireService.toFeedbackQuestionnaire(
            request);
        return ResponseEntity.ok(feedbackQuestionnaireService.updateFeedbackQuestionnaire(updatedFeedbackQuestionnaire,
            id));
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/{id}/addQuestion")
    public ResponseEntity<FeedbackQuestionnaire> addQuestionToFeedback(@PathVariable Long id,
                                                                       @RequestBody NewFeedbackQuestionRequest
                                                                           request) {
        UUID uuid = UUID.randomUUID();
        MDC.put("requestId", uuid.toString());
        logger.info("Received request for addQuestionToFeedbackkQuestionnaire for id: " + id);

        if (!request.validate()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid request");
        }
        FeedbackQuestion question = feedbackQuestionnaireService.toFeedbackQuestion(request);
        return ResponseEntity.ok(feedbackQuestionnaireService.addQuestionToFeedbackQuestionnaire(id, question));
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/{id}/addQuestions")
    public ResponseEntity<FeedbackQuestionnaire> addQuestionsToFeedback(@PathVariable Long id,
                                                                        @RequestBody List<NewFeedbackQuestionRequest>
                                                                            request) {
        UUID uuid = UUID.randomUUID();
        MDC.put("requestId", uuid.toString());
        logger.info("Received request for addQuestionsToFeedbackkQuestionnaire for id: " + id);

        for (NewFeedbackQuestionRequest question : request) {
            if (!question.validate()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid request");
            }
        }
        List<FeedbackQuestion> questions = request.stream().map(feedbackQuestionnaireService::toFeedbackQuestion)
            .collect(Collectors.toList());
        return ResponseEntity.ok(feedbackQuestionnaireService.addQuestionsToFeedbackQuestionnaire(id, questions));
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/{id}/updateQuestion")
    public ResponseEntity<FeedbackQuestionnaire> updateQuestionInFeedback(@PathVariable Long feedbackId,
                                                                          @RequestParam Long questionId,
                                                                          @RequestBody NewFeedbackQuestionRequest
                                                                              request) {
        UUID uuid = UUID.randomUUID();
        MDC.put("requestId", uuid.toString());
        logger.info("Received request for updateQuestionInFeedback for id: " + feedbackId);

        if (!request.validate()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid request");
        }
        FeedbackQuestion question = feedbackQuestionnaireService.toFeedbackQuestion(request);
        question.setId(questionId);
        return ResponseEntity.ok(feedbackQuestionnaireService.updateQuestionInFeedbackQuestionnaire(feedbackId,
            question));
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
    public ResponseEntity<Void> deleteFeedback(@PathVariable Long id) {
        UUID uuid = UUID.randomUUID();
        MDC.put("requestId", uuid.toString());
        logger.info("Received request for deleteFeedback for id: " + id);

        feedbackQuestionnaireService.deleteFeedbackQuestionnaire(id);
        return ResponseEntity.ok().build();
    }
}
