package com.skolarli.lmsservice.controller.exam;

import com.skolarli.lmsservice.models.db.exam.Exam;
import com.skolarli.lmsservice.models.dto.exam.DeleteExamQuestionsRequest;
import com.skolarli.lmsservice.models.dto.exam.NewExamQuestionsAllTypesRequest;
import com.skolarli.lmsservice.models.dto.exam.NewExamQuestionsAllTypesResponse;
import com.skolarli.lmsservice.models.dto.exam.NewExamRequest;
import com.skolarli.lmsservice.services.exam.ExamService;
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
@RequestMapping("/exam")
public class ExamController {
    /*
    TODO:
    add exam qn to QB
    add QB qn to exam

    Choose Question Bank questions for an exam
     */
    final Logger logger = LoggerFactory.getLogger(ExamController.class);

    ExamService examService;

    public ExamController(ExamService examService) {
        this.examService = examService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<Exam>> getAllExams(
            @RequestParam(required = false) Long courseId,
            @RequestParam(required = false) Long batchId) {

        UUID uuid = UUID.randomUUID();
        MDC.put("requestId", uuid.toString());
        logger.info("Received request for getAllExams"
                + (courseId != null ? " for couseId: " + courseId : "")
                + (batchId != null ? " for batchId: " + batchId : ""));

        if (batchId != null) {
            try {
                return new ResponseEntity<>(examService.getAllExamsForBatch(batchId),
                        HttpStatus.OK);
            } catch (Exception e) {
                logger.error("Error in getAllExams: " + e.getMessage());
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
            } finally {
                MDC.remove("requestId");
            }
        }

        if (courseId != null) {
            try {
                return new ResponseEntity<>(examService.getAllExamsForCourse(courseId),
                        HttpStatus.OK);
            } catch (Exception e) {
                logger.error("Error in getAllExams: " + e.getMessage());
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
            } finally {
                MDC.remove("requestId");
            }
        }

        try {
            return new ResponseEntity<>(examService.getAllExams(), HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error in getAllExams: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        } finally {
            MDC.remove("requestId");
        }
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    public ResponseEntity<Exam> getExam(@PathVariable Long id) {
        UUID uuid = UUID.randomUUID();
        MDC.put("requestId", uuid.toString());
        logger.info("Received request for getExam for id: " + id);

        try {
            return new ResponseEntity<>(examService.getExam(id), HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error in getExam: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        } finally {
            MDC.remove("requestId");
        }
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{id}/questions")
    public ResponseEntity<NewExamQuestionsAllTypesResponse> getAllQuestions(@PathVariable Long id) {
        UUID uuid = UUID.randomUUID();
        MDC.put("requestId", uuid.toString());
        logger.info("Received request for getAllQuestions for Exam id: " + id);

        try {
            return new ResponseEntity<>(examService.getAllQuestions(id), HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error in getAllQuestions: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        } finally {
            MDC.remove("requestId");
        }
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Exam> saveExam(@Valid @RequestBody NewExamRequest request) {
        UUID uuid = UUID.randomUUID();
        MDC.put("requestId", uuid.toString());
        logger.info("Received request adding exam");

        Exam exam = examService.toExam(request);

        try {
            return new ResponseEntity<>(examService.saveExam(exam), HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error in saveExam: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        } finally {
            MDC.remove("requestId");
        }
    }

    @RequestMapping(method = RequestMethod.POST, value = "/{id}/questions")
    public ResponseEntity<Exam> addQuestions(@RequestBody NewExamQuestionsAllTypesRequest request,
                                             @PathVariable Long id) {
        UUID uuid = UUID.randomUUID();
        MDC.put("requestId", uuid.toString());
        logger.info("Received request adding questions to exam for id: " + id);

        try {
            return new ResponseEntity<>(examService.addQuestionsToExam(request, id),
                    HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error in addQuestions: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        } finally {
            MDC.remove("requestId");
        }
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/{id}")
    public ResponseEntity<Exam> updateExam(@RequestBody NewExamRequest request,
                                           @PathVariable Long id) {
        UUID uuid = UUID.randomUUID();
        MDC.put("requestId", uuid.toString());
        logger.info("Received request updating exam for id: " + id);

        Exam exam = examService.toExam(request);

        try {
            return new ResponseEntity<>(examService.updateExams(exam, id), HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error in updateExam: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        } finally {
            MDC.remove("requestId");
        }
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
    public ResponseEntity<Void> deleteExam(@PathVariable Long id) {
        UUID uuid = UUID.randomUUID();
        MDC.put("requestId", uuid.toString());
        logger.info("Received request deleting exam for id: " + id);

        try {
            examService.hardDeleteExam(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error in deleteExam: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        } finally {
            MDC.remove("requestId");
        }
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/{examId}/questions")
    public ResponseEntity<Void> deleteQuestions(@PathVariable Long examId,
                                                @RequestBody DeleteExamQuestionsRequest request) {
        UUID uuid = UUID.randomUUID();
        MDC.put("requestId", uuid.toString());
        logger.info("Received request deleting questions for exam id: " + examId);

        try {
            examService.deleteQuestions(examId, request);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error in deleteQuestions: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        } finally {
            MDC.remove("requestId");
        }
    }
}
