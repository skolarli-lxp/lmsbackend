package com.skolarli.lmsservice.controller.questionbank;

import com.skolarli.lmsservice.exception.ValidationFailureException;
import com.skolarli.lmsservice.models.db.exam.ExamQuestionMcq;
import com.skolarli.lmsservice.models.db.questionbank.BankQuestionMcq;
import com.skolarli.lmsservice.models.dto.questionbank.NewBankQuestionMcqRequest;
import com.skolarli.lmsservice.models.dto.questionbank.ToExamQuestionRequest;
import com.skolarli.lmsservice.services.questionbank.QuestionBankMcqService;
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
@RequestMapping("/questionbankmcq")
public class QuestionBankMcqController {

    final Logger logger = LoggerFactory.getLogger(QuestionBankMcqController.class);

    final QuestionBankMcqService questionBankMcqService;

    public QuestionBankMcqController(QuestionBankMcqService questionBankMcqService) {
        this.questionBankMcqService = questionBankMcqService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<BankQuestionMcq>> getAllQuestions(@RequestParam(required = false)
                                                                 Long courseId,
                                                                 @RequestParam(required = false)
                                                                 Long batchId,
                                                                 @RequestParam(required = false)
                                                                     Long lessonId,
                                                                 @RequestParam(required = false)
                                                                     Long chapterId,
                                                                 @RequestParam(required = false)
                                                                     Long studentId) {
        UUID uuid = UUID.randomUUID();
        MDC.put("requestId", uuid.toString());
        logger.info("Received request for getAllquestions" + (courseId != null
                ? " for courseId: " + courseId
                : ""));
        try {
            List<BankQuestionMcq> questions = questionBankMcqService.getQuestionsByParameters(
                    courseId, batchId, lessonId, chapterId, studentId);
            return new ResponseEntity<>(questions, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error in getAllquestions: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        } finally {
            MDC.remove("requestId");
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<BankQuestionMcq> getQuestion(@PathVariable long id) {
        UUID uuid = UUID.randomUUID();
        MDC.put("requestId", uuid.toString());
        logger.info("Received request for get question with id: " + id);

        try {
            BankQuestionMcq question = questionBankMcqService.getQuestion(id);
            return new ResponseEntity<>(question, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error in getAllquestions: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        } finally {
            MDC.remove("requestId");
        }
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<BankQuestionMcq> saveQuestion(
            @Valid @RequestBody NewBankQuestionMcqRequest request) {
        UUID uuid = UUID.randomUUID();
        MDC.put("requestId", uuid.toString());
        logger.info("Received request for save question");
        BankQuestionMcq question = null;
        try {
            question = questionBankMcqService.toBankQuestionMcq(request);
        } catch (Exception e) {
            logger.error("Error in saveQuestion: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } finally {
            MDC.remove("requestId");
        }

        try {
            BankQuestionMcq savedQuestion = questionBankMcqService.saveQuestion(question);
            return new ResponseEntity<>(savedQuestion, HttpStatus.OK);
        } catch (ValidationFailureException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            logger.error("Error in saveQuestion: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        } finally {
            MDC.remove("requestId");
        }
    }

    @RequestMapping(value = "/saveall", method = RequestMethod.POST)
    public ResponseEntity<List<BankQuestionMcq>> saveQuestions(
            @Valid @RequestBody List<NewBankQuestionMcqRequest> request) {
        UUID uuid = UUID.randomUUID();
        MDC.put("requestId", uuid.toString());
        logger.info("Received request for save questions");

        List<BankQuestionMcq> questions = null;

        try {
            questions = request.stream()
                    .map(questionBankMcqService::toBankQuestionMcq)
                    .collect(java.util.stream.Collectors.toList());
        } catch (Exception e) {
            logger.error("Error in saveQuestion: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } finally {
            MDC.remove("requestId");
        }


        try {
            List<BankQuestionMcq> savedQuestions =
                    questionBankMcqService.saveAllQuestions(questions);
            return new ResponseEntity<>(savedQuestions, HttpStatus.OK);
        } catch (ValidationFailureException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            logger.error("Error in saveQuestion: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        } finally {
            MDC.remove("requestId");
        }
    }

    @RequestMapping(value = "/addtoexam", method = RequestMethod.POST)
    public ResponseEntity<List<ExamQuestionMcq>> addQuestionsToExam(
            @RequestParam Long examId, @Valid @RequestBody ToExamQuestionRequest request) {
        UUID uuid = UUID.randomUUID();
        MDC.put("requestId", uuid.toString());
        logger.info("Received request for add questions to exam with id: " + examId);

        if (!request.isValid()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Invalid request. Number of questions should be equal to number of marks");
        }

        List<ExamQuestionMcq> questions = null;
        try {
            questions = questionBankMcqService.toExamQuestionMcq(request.getBankQuestionIds(),
                    request.getMarks(), examId);
        } catch (Exception e) {
            logger.error("Error in addQuestionsToExam: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } finally {
            MDC.remove("requestId");
        }

        return new ResponseEntity<>(questions, HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<BankQuestionMcq> updateQuestion(
            @RequestBody NewBankQuestionMcqRequest request, @PathVariable long id) {
        UUID uuid = UUID.randomUUID();
        MDC.put("requestId", uuid.toString());
        logger.info("Received request for update question with id: " + id);

        BankQuestionMcq question = null;
        try {
            question = questionBankMcqService.toBankQuestionMcq(request);
        } catch (Exception e) {
            logger.error("Error in updateQuestion: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } finally {
            MDC.remove("requestId");
        }

        try {
            BankQuestionMcq updatedQuestion = questionBankMcqService.updateQuestion(question, id);
            return new ResponseEntity<>(updatedQuestion, HttpStatus.OK);
        } catch (ValidationFailureException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            logger.error("Error in updateQuestion: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        } finally {
            MDC.remove("requestId");
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<BankQuestionMcq> deleteQuestion(@PathVariable long id) {
        UUID uuid = UUID.randomUUID();
        MDC.put("requestId", uuid.toString());
        logger.info("Received request for delete question with id: " + id);

        try {
            questionBankMcqService.hardDeleteQuestion(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error in deleteQuestion: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        } finally {
            MDC.remove("requestId");
        }
    }
}
