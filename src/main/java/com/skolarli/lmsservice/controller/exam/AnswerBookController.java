package com.skolarli.lmsservice.controller.exam;

import com.skolarli.lmsservice.models.AnswerBookStatus;
import com.skolarli.lmsservice.models.db.exam.AnswerBook;
import com.skolarli.lmsservice.models.dto.exam.AddAnswerBookAnswerRequest;
import com.skolarli.lmsservice.models.dto.exam.AnswerBookEvaulationRequest;
import com.skolarli.lmsservice.models.dto.exam.NewAnswerBookRequest;
import com.skolarli.lmsservice.services.exam.AnswerBookService;
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
@RequestMapping("/answerbook")
public class AnswerBookController {
    final Logger logger = LoggerFactory.getLogger(AnswerBookController.class);

    AnswerBookService answerBookService;

    public AnswerBookController(AnswerBookService answerBookService) {
        this.answerBookService = answerBookService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<AnswerBook>> getAllAnswerBooks(@RequestParam(required = false) Long examId,
                                                              @RequestParam(required = false) Long studentId) {

        UUID uuid = UUID.randomUUID();
        MDC.put("requestId", uuid.toString());
        logger.info("Received request for getAllAnswerBooks"
                + (examId != null ? " for examId: " + examId : "")
                + (studentId != null ? " for studentId: " + studentId : ""));

        if (examId != null) {
            if (studentId != null) {
                try {
                    return ResponseEntity.ok(answerBookService.getAllByExamIdAndStudentId(examId, studentId));
                } catch (Exception e) {
                    logger.error("Error in getAllAnswerBooks: " + e.getMessage());
                    throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
                } finally {
                    MDC.remove("requestId");
                }
            } else {
                try {
                    return ResponseEntity.ok(answerBookService.getAllByExamId(examId));
                } catch (Exception e) {
                    logger.error("Error in getAllAnswerBooks: " + e.getMessage());
                    throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
                } finally {
                    MDC.remove("requestId");
                }
            }
        }
        if (studentId != null) {
            try {
                return ResponseEntity.ok(answerBookService.getAllByStudentId(studentId));
            } catch (Exception e) {
                logger.error("Error in getAllAnswerBooks: " + e.getMessage());
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
            } finally {
                MDC.remove("requestId");
            }
        }
        try {
            return ResponseEntity.ok(answerBookService.getAllAnswerBooks());
        } catch (Exception e) {
            logger.error("Error in getAllAnswerBooks: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        } finally {
            MDC.remove("requestId");
        }
    }

    @RequestMapping(method = RequestMethod.GET, path = "/{id}")
    public ResponseEntity<AnswerBook> getAnswerBook(@PathVariable Long id) {
        UUID uuid = UUID.randomUUID();
        MDC.put("requestId", uuid.toString());
        logger.info("Received request for getAnswerBook for id: " + id);

        try {
            return ResponseEntity.ok(answerBookService.getAnswerBookById(id));
        } catch (Exception e) {
            logger.error("Error in getAnswerBook: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        } finally {
            MDC.remove("requestId");
        }
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<AnswerBook> createAnswerBook(@Valid @RequestBody NewAnswerBookRequest request) {
        UUID uuid = UUID.randomUUID();
        MDC.put("requestId", uuid.toString());
        logger.info("Received request for createAnswerBook for exam: " + request.getExamId());

        AnswerBook answerBook = answerBookService.toAnswerBook(request);

        try {
            return ResponseEntity.ok(answerBookService.saveAnswerBook(answerBook));
        } catch (Exception e) {
            logger.error("Error in createAnswerBook: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        } finally {
            MDC.remove("requestId");
        }
    }

    @RequestMapping(method = RequestMethod.POST, path = "/{id}/questions")
    public ResponseEntity<AnswerBook> addAnswerToAnswerBook(@PathVariable Long id,
                                                            @RequestBody AddAnswerBookAnswerRequest request) {
        UUID uuid = UUID.randomUUID();
        MDC.put("requestId", uuid.toString());
        logger.info("Received request for add answers to answebook for id: " + id);

        try {
            return ResponseEntity.ok(answerBookService.addAnswers(request, id));
        } catch (Exception e) {
            logger.error("Error in submitAnswerBook: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        } finally {
            MDC.remove("requestId");
        }
    }

    @RequestMapping(method = RequestMethod.PUT, path = "/{id}/evaluate")
    public ResponseEntity<String> evaluateAnswerBook(@PathVariable Long id,
                                                     @RequestBody AnswerBookEvaulationRequest request) {
        UUID uuid = UUID.randomUUID();
        MDC.put("requestId", uuid.toString());
        logger.info("Received request for evaluateAnswerBook for id: " + id);

        try {
            answerBookService.evaluateAnswerBook(id, request);
            return ResponseEntity.ok("OK");
        } catch (Exception e) {
            logger.error("Error in evaluateAnswerBook: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        } finally {
            MDC.remove("requestId");
        }
    }

    @RequestMapping(method = RequestMethod.PUT, path = "/{id}")
    public ResponseEntity<AnswerBook> updateAnswerBook(@RequestBody NewAnswerBookRequest request,
                                                       @PathVariable Long id) {
        UUID uuid = UUID.randomUUID();
        MDC.put("requestId", uuid.toString());
        logger.info("Received request for updateAnswerBook for id: " + id);

        AnswerBook answerBook = answerBookService.toAnswerBook(request);

        try {
            return ResponseEntity.ok(answerBookService.updateAnswerBook(answerBook, id));
        } catch (Exception e) {
            logger.error("Error in updateAnswerBook: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        } finally {
            MDC.remove("requestId");
        }
    }

    @RequestMapping(method = RequestMethod.PUT, path = "/status/{id}")
    public ResponseEntity<AnswerBook> updateAnswerBookStatus(@RequestParam AnswerBookStatus status,
                                                             @PathVariable Long id) {
        UUID uuid = UUID.randomUUID();
        MDC.put("requestId", uuid.toString());
        logger.info("Received request for updateAnswerBookStatus for id: " + id);

        try {
            return ResponseEntity.ok(answerBookService.updateStatus(status, id));
        } catch (Exception e) {
            logger.error("Error in updateAnswerBookStatus: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        } finally {
            MDC.remove("requestId");
        }
    }

    @RequestMapping(method = RequestMethod.DELETE, path = "/{id}")
    public ResponseEntity<String> deleteAnswerBook(@PathVariable Long id) {
        UUID uuid = UUID.randomUUID();
        MDC.put("requestId", uuid.toString());
        logger.info("Received request for deleteAnswerBook for id: " + id);

        try {
            answerBookService.deleteAnswerBook(id);
            return ResponseEntity.ok("AnswerBook deleted successfully");
        } catch (Exception e) {
            logger.error("Error in deleteAnswerBook: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        } finally {
            MDC.remove("requestId");
        }
    }
}
