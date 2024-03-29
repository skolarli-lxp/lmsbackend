package com.skolarli.lmsservice.controller.exam;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.skolarli.lmsservice.models.AnswerBookStatus;
import com.skolarli.lmsservice.models.db.exam.AnswerBook;
import com.skolarli.lmsservice.models.dto.exam.answerbook.*;
import com.skolarli.lmsservice.services.exam.AnswerBookService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    @RequestMapping(method = RequestMethod.GET, path = "/getStatusForExamId")
    public ResponseEntity<Map<Long, Map<AnswerBookStatus, Long>>> getAllAnswerBookStatus(@RequestParam(required = false)
                                                                                  Long examId) {
        UUID uuid = UUID.randomUUID();
        MDC.put("requestId", uuid.toString());
        logger.info("Received request for getAllAnswerBookStatus for examId: " + examId);

        try {
            if (examId == null) {
                return ResponseEntity.ok(answerBookService.getAllAnswerBookStatuses());
            } else {
                Map<AnswerBookStatus, Long> statusMap = answerBookService.getAllAnswerBookStatusesForExam(examId);
                HashMap<Long, Map<AnswerBookStatus, Long>> response = new HashMap<>();
                response.put(examId, statusMap);
                return ResponseEntity.ok(response);
            }

        } catch (Exception e) {
            logger.error("Error in getAllAnswerBookStatus: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        } finally {
            MDC.remove("requestId");
        }
    }

    @RequestMapping(method = RequestMethod.GET, path = "/{id}/singleanswer")
    public ResponseEntity<GetAnswerResponse> getSingleAnswerFromAnswerBook(@PathVariable Long id,
                                                                           @RequestParam String questionType,
                                                                           @RequestParam Long questionId) {
        UUID uuid = UUID.randomUUID();
        MDC.put("requestId", uuid.toString());
        logger.info("Received request for getSingleAnswerFromAnswerBook for id: " + id);

        try {
            GetAnswerResponse response = answerBookService.getAnswerByAnswerBookIdAndQuestionId(id,
                questionId, questionType);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error in getSingleAnswerFromAnswerBook: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        } finally {
            MDC.remove("requestId");
        }
    }

    @RequestMapping(method = RequestMethod.GET, path = "/{id}/allanswers")
    public ResponseEntity<GetAllAnswersResponse> getAllAnswersFromAnswerBook(@PathVariable Long id) {
        UUID uuid = UUID.randomUUID();
        MDC.put("requestId", uuid.toString());
        logger.info("Received request for getAllAnswersFromAnswerBook for id: " + id);

        try {
            GetAllAnswersResponse response = answerBookService.getAnswersByAnswerBookId(id);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error in getAllAnswersFromAnswerBook: " + e.getMessage());
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

    @RequestMapping(method = RequestMethod.POST, path = "/{id}/answers")
    public ResponseEntity<AnswerBook> addAnswerToAnswerBook(@PathVariable Long id,
                                                            @RequestBody AddAnswerBookAnswerRequest request) {
        UUID uuid = UUID.randomUUID();
        MDC.put("requestId", uuid.toString());
        logger.info("Received request for add answers to answebook for id: " + id);

        try {
            return ResponseEntity.ok(answerBookService.addAnswers(request, id));
        } catch (Exception e) {
            logger.error("Error in addAnswersToAnswerBook: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        } finally {
            MDC.remove("requestId");
        }
    }

    @RequestMapping(method = RequestMethod.POST, path = "/{id}/singleanswer")
    public ResponseEntity<NewAnswerResponse> addSingleAnswerToAnswerBook(@PathVariable Long id,
                                                                         @RequestParam String questionType,
                                                                         @RequestBody String answer) {
        UUID uuid = UUID.randomUUID();
        MDC.put("requestId", uuid.toString());
        logger.info("Received request for add answer to answebook for id: " + id);
        NewAnswerMcqRequest newAnswerMcqRequest = null;
        NewAnswerSubjectiveRequest newAnswerSubjectiveRequest = null;
        NewAnswerTrueFalseRequest newAnswerTrueFalseRequest = null;

        try {
            if (questionType.equalsIgnoreCase("MCQ")) {
                newAnswerMcqRequest = deserialize(answer, NewAnswerMcqRequest.class);
            } else if (questionType.equalsIgnoreCase("Subjective")) {
                newAnswerSubjectiveRequest = deserialize(answer, NewAnswerSubjectiveRequest.class);
            } else if (questionType.equalsIgnoreCase("TrueOrFalse")) {
                newAnswerTrueFalseRequest = deserialize(answer, NewAnswerTrueFalseRequest.class);
            } else {
                throw new Exception("Invalid question type");
            }
        } catch (Exception e) {
            logger.error("Error in processing Json: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } finally {
            MDC.remove("requestId");
        }

        NewAnswerResponse response = null;
        try {
            if (questionType.equalsIgnoreCase("MCQ")) {
                response = answerBookService.addAnswerToAnswerBook(newAnswerMcqRequest, id);
            } else if (questionType.equalsIgnoreCase("Subjective")) {
                response = answerBookService.addAnswerToAnswerBook(newAnswerSubjectiveRequest, id);
            } else if (questionType.equalsIgnoreCase("TrueOrFalse")) {
                response = answerBookService.addAnswerToAnswerBook(newAnswerTrueFalseRequest, id);
            }
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error in addSingleAnswerToAnswerBook: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        } finally {
            MDC.remove("requestId");
        }
    }

    private <T> T deserialize(String json, Class<T> targetType) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(json, targetType);
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

    @RequestMapping(method = RequestMethod.PUT, path = "/{id}/finalscores")
    public ResponseEntity<GetScoresResponse> calculateFinalScores(@PathVariable Long id) {
        UUID uuid = UUID.randomUUID();
        MDC.put("requestId", uuid.toString());
        logger.info("Received request for calculateFinalScores for id: " + id);

        try {
            return ResponseEntity.ok(answerBookService.calculateFinalScores(id));
        } catch (Exception e) {
            logger.error("Error in calculateFinalScores: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        } finally {
            MDC.remove("requestId");
        }
    }

    @RequestMapping(method = RequestMethod.PUT, path = "/{id}")
    public ResponseEntity<AnswerBook> updateAnswerBook(@RequestBody UpdateAnswerBookRequest request,
                                                       @PathVariable Long id) {
        UUID uuid = UUID.randomUUID();
        MDC.put("requestId", uuid.toString());
        logger.info("Received request for updateAnswerBook for id: " + id);

        AnswerBook answerBook = request.toAnswerBook();

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

    @RequestMapping(method = RequestMethod.PUT, path = "/{id}/answer")
    public ResponseEntity<NewAnswerResponse> updateAnswerBookAnswer(@PathVariable Long id,
                                                                    @RequestParam String questionType,
                                                                    @RequestParam Long answerId,
                                                                    @RequestBody String answer) {
        UUID uuid = UUID.randomUUID();
        MDC.put("requestId", uuid.toString());
        logger.info("Received request for updateAnswerBookAnswer for id: " + id);

        UpdateAnswerMcqRequest updateAnswerMcqRequest = null;
        UpdateAnswerSubjectiveRequest updateAnswerSubjectiveRequest = null;
        UpdateAnswerTrueFalseRequest updateAnswerTrueFalseRequest = null;

        try {
            if (questionType.equalsIgnoreCase("MCQ")) {
                updateAnswerMcqRequest = deserialize(answer, UpdateAnswerMcqRequest.class);
            } else if (questionType.equalsIgnoreCase("Subjective")) {
                updateAnswerSubjectiveRequest = deserialize(answer, UpdateAnswerSubjectiveRequest.class);
            } else if (questionType.equalsIgnoreCase("TrueOrFalse")) {
                updateAnswerTrueFalseRequest = deserialize(answer, UpdateAnswerTrueFalseRequest.class);
            } else {
                throw new Exception("Invalid question type");
            }
        } catch (Exception e) {
            logger.error("Error in processing Json: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } finally {
            MDC.remove("requestId");
        }

        try {
            if (questionType.equalsIgnoreCase("MCQ")) {
                return ResponseEntity.ok(answerBookService.updateAnswerBookAnswer(updateAnswerMcqRequest, id,
                    answerId));
            } else if (questionType.equalsIgnoreCase("Subjective")) {
                return ResponseEntity.ok(answerBookService.updateAnswerBookAnswer(updateAnswerSubjectiveRequest, id,
                    answerId));
            } else if (questionType.equalsIgnoreCase("TrueOrFalse")) {
                return ResponseEntity.ok(answerBookService.updateAnswerBookAnswer(updateAnswerTrueFalseRequest, id,
                    answerId));
            } else {
                throw new Exception("Invalid question type");
            }
        } catch (Exception e) {
            logger.error("Error in updateAnswerBookAnswer: " + e.getMessage());
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

    @RequestMapping(method = RequestMethod.DELETE, path = "/answer")
    public ResponseEntity<String> deleteAnswer(@RequestParam Long answerId,
                                               @RequestParam String questionType) {
        UUID uuid = UUID.randomUUID();
        MDC.put("requestId", uuid.toString());
        logger.info("Received request for deleteAnswer for id: " + answerId);

        try {
            answerBookService.deleteAnswerBookAnswer(answerId, questionType);
            return ResponseEntity.ok("Answer deleted successfully");
        } catch (Exception e) {
            logger.error("Error in deleteAnswer: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        } finally {
            MDC.remove("requestId");
        }
    }
}
