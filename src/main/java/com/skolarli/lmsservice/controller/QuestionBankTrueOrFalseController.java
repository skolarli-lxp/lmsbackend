package com.skolarli.lmsservice.controller;

import com.skolarli.lmsservice.models.db.questionbank.BankQuestionTrueOrFalse;
import com.skolarli.lmsservice.models.dto.questionbank.NewBankQuestionTrueOrFalseRequest;
import com.skolarli.lmsservice.services.QuestionBankTrueOrFalseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;
import javax.validation.Valid;

@RestController
@RequestMapping("/questionbanktrueorfalse")
public class QuestionBankTrueOrFalseController {

    final Logger logger = LoggerFactory.getLogger(QuestionBankTrueOrFalseController.class);

    final QuestionBankTrueOrFalseService questionBankTrueOrFalseService;

    public QuestionBankTrueOrFalseController(QuestionBankTrueOrFalseService
                                                     questionBankTrueOrFalseService) {
        this.questionBankTrueOrFalseService = questionBankTrueOrFalseService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<BankQuestionTrueOrFalse>> getAllQuestions(
            @RequestParam(required = false) Long courseId) {
        UUID uuid = UUID.randomUUID();
        MDC.put("requestId", uuid.toString());
        logger.info("Received request for getAllquestions" + (courseId != null
                ? " for courseId: " + courseId
                : ""));
        try {
            List<BankQuestionTrueOrFalse> questions = questionBankTrueOrFalseService
                    .getAllQuestions();
            return new ResponseEntity<>(questions, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error in getAllquestions: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        } finally {
            MDC.remove("requestId");
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<BankQuestionTrueOrFalse> getQuestion(@PathVariable long id) {
        UUID uuid = UUID.randomUUID();
        MDC.put("requestId", uuid.toString());
        logger.info("Received request for get question with id: " + id);

        try {
            BankQuestionTrueOrFalse question = questionBankTrueOrFalseService.getQuestion(id);
            return new ResponseEntity<>(question, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error in getAllquestions: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        } finally {
            MDC.remove("requestId");
        }
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<BankQuestionTrueOrFalse> saveQuestion(
            @Valid @RequestBody NewBankQuestionTrueOrFalseRequest request) {
        UUID uuid = UUID.randomUUID();
        MDC.put("requestId", uuid.toString());
        logger.info("Received request for save question");
        BankQuestionTrueOrFalse question = null;
        try {

            question = questionBankTrueOrFalseService.toBankQuestionTrueOrFalse(request);
        } catch (Exception e) {
            logger.error("Error in saveQuestion: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } finally {
            MDC.remove("requestId");
        }

        try {
            BankQuestionTrueOrFalse savedQuestion = questionBankTrueOrFalseService
                    .saveQuestion(question);
            return new ResponseEntity<>(savedQuestion, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error in saveQuestion: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        } finally {
            MDC.remove("requestId");
        }
    }

    @RequestMapping(value = "/saveall", method = RequestMethod.POST)
    public ResponseEntity<List<BankQuestionTrueOrFalse>> saveQuestions(
            @Valid @RequestBody List<NewBankQuestionTrueOrFalseRequest> request) {
        UUID uuid = UUID.randomUUID();
        MDC.put("requestId", uuid.toString());
        logger.info("Received request for save questions");

        List<BankQuestionTrueOrFalse> questions = null;

        try {
            questions = request.stream()
                    .map(questionBankTrueOrFalseService::toBankQuestionTrueOrFalse)
                    .collect(java.util.stream.Collectors.toList());
        } catch (Exception e) {
            logger.error("Error in saveQuestion: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } finally {
            MDC.remove("requestId");
        }


        try {
            List<BankQuestionTrueOrFalse> savedQuestions =
                    questionBankTrueOrFalseService.saveAllQuestions(questions);
            return new ResponseEntity<>(savedQuestions, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error in saveQuestion: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        } finally {
            MDC.remove("requestId");
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<BankQuestionTrueOrFalse> updateQuestion(
            @RequestBody NewBankQuestionTrueOrFalseRequest request, @PathVariable long id) {
        UUID uuid = UUID.randomUUID();
        MDC.put("requestId", uuid.toString());
        logger.info("Received request for update question with id: " + id);

        BankQuestionTrueOrFalse question = null;
        try {
            question = questionBankTrueOrFalseService.toBankQuestionTrueOrFalse(request);
        } catch (Exception e) {
            logger.error("Error in updateQuestion: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } finally {
            MDC.remove("requestId");
        }

        try {
            BankQuestionTrueOrFalse updatedQuestion = questionBankTrueOrFalseService
                    .updateQuestion(question, id);
            return new ResponseEntity<>(updatedQuestion, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error in updateQuestion: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        } finally {
            MDC.remove("requestId");
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<BankQuestionTrueOrFalse> deleteQuestion(@PathVariable long id) {
        UUID uuid = UUID.randomUUID();
        MDC.put("requestId", uuid.toString());
        logger.info("Received request for delete question with id: " + id);

        try {
            questionBankTrueOrFalseService.hardDeleteQuestion(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error in deleteQuestion: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        } finally {
            MDC.remove("requestId");
        }
    }
}
