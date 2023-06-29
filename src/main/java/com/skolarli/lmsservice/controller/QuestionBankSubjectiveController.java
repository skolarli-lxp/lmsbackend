package com.skolarli.lmsservice.controller;

import com.skolarli.lmsservice.models.db.BankQuestionSubjective;
import com.skolarli.lmsservice.models.dto.questionbank.NewBankQuestionSubjectiveRequest;
import com.skolarli.lmsservice.services.QuestionBankSubjectiveService;
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
@RequestMapping("/questionbanksubjective")
public class QuestionBankSubjectiveController {

    final Logger logger = LoggerFactory.getLogger(QuestionBankSubjectiveController.class);

    final QuestionBankSubjectiveService questionBankSubjectiveService;

    public QuestionBankSubjectiveController(QuestionBankSubjectiveService
                                                    questionBankSubjectiveService) {
        this.questionBankSubjectiveService = questionBankSubjectiveService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<BankQuestionSubjective>> getAllQuestions(
            @RequestParam(required = false) Long courseId) {
        UUID uuid = UUID.randomUUID();
        MDC.put("requestId", uuid.toString());
        logger.info("Received request for getAllquestions" + (courseId != null
                ? " for courseId: " + courseId
                : ""));
        try {
            List<BankQuestionSubjective> questions = questionBankSubjectiveService
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
    public ResponseEntity<BankQuestionSubjective> getQuestion(@PathVariable long id) {
        UUID uuid = UUID.randomUUID();
        MDC.put("requestId", uuid.toString());
        logger.info("Received request for get question with id: " + id);

        try {
            BankQuestionSubjective question = questionBankSubjectiveService.getQuestion(id);
            return new ResponseEntity<>(question, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error in getAllquestions: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        } finally {
            MDC.remove("requestId");
        }
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<BankQuestionSubjective> saveQuestion(
            @Valid @RequestBody NewBankQuestionSubjectiveRequest request) {
        UUID uuid = UUID.randomUUID();
        MDC.put("requestId", uuid.toString());
        logger.info("Received request for save question");
        BankQuestionSubjective question = null;
        try {

            question = questionBankSubjectiveService.toBankQuestionSubjective(request);
        } catch (Exception e) {
            logger.error("Error in saveQuestion: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } finally {
            MDC.remove("requestId");
        }

        try {
            BankQuestionSubjective savedQuestion = questionBankSubjectiveService
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
    public ResponseEntity<List<BankQuestionSubjective>> saveQuestions(
            @Valid @RequestBody List<NewBankQuestionSubjectiveRequest> request) {
        UUID uuid = UUID.randomUUID();
        MDC.put("requestId", uuid.toString());
        logger.info("Received request for save questions");

        List<BankQuestionSubjective> questions = null;

        try {
            questions = request.stream()
                    .map(questionBankSubjectiveService::toBankQuestionSubjective)
                    .collect(java.util.stream.Collectors.toList());
        } catch (Exception e) {
            logger.error("Error in saveQuestion: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } finally {
            MDC.remove("requestId");
        }


        try {
            List<BankQuestionSubjective> savedQuestions =
                    questionBankSubjectiveService.saveAllQuestions(questions);
            return new ResponseEntity<>(savedQuestions, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error in saveQuestion: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        } finally {
            MDC.remove("requestId");
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<BankQuestionSubjective> updateQuestion(
             @RequestBody NewBankQuestionSubjectiveRequest request, @PathVariable long id) {
        UUID uuid = UUID.randomUUID();
        MDC.put("requestId", uuid.toString());
        logger.info("Received request for update question with id: " + id);

        BankQuestionSubjective question = null;
        try {
            question = questionBankSubjectiveService.toBankQuestionSubjective(request);
        } catch (Exception e) {
            logger.error("Error in updateQuestion: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } finally {
            MDC.remove("requestId");
        }

        try {
            BankQuestionSubjective updatedQuestion = questionBankSubjectiveService
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
    public ResponseEntity<BankQuestionSubjective> deleteQuestion(@PathVariable long id) {
        UUID uuid = UUID.randomUUID();
        MDC.put("requestId", uuid.toString());
        logger.info("Received request for delete question with id: " + id);

        try {
            questionBankSubjectiveService.hardDeleteQuestion(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error in deleteQuestion: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        } finally {
            MDC.remove("requestId");
        }
    }
}
