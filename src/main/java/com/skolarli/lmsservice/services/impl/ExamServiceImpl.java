package com.skolarli.lmsservice.services.impl;

import com.skolarli.lmsservice.exception.OperationNotSupportedException;
import com.skolarli.lmsservice.exception.ResourceNotFoundException;
import com.skolarli.lmsservice.exception.ValidationFailureException;
import com.skolarli.lmsservice.models.db.Exam;
import com.skolarli.lmsservice.models.db.LmsUser;
import com.skolarli.lmsservice.models.dto.exam.DeleteExamQuestionsRequest;
import com.skolarli.lmsservice.models.dto.exam.NewExamQuestionsAllTypesRequest;
import com.skolarli.lmsservice.models.dto.exam.NewExamQuestionsAllTypesResponse;
import com.skolarli.lmsservice.models.dto.exam.NewExamRequest;
import com.skolarli.lmsservice.repository.ExamRepository;
import com.skolarli.lmsservice.services.*;
import com.skolarli.lmsservice.utils.UserUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class ExamServiceImpl implements ExamService {
    private static final Logger logger = LoggerFactory.getLogger(ExamServiceImpl.class);

    ExamRepository examRepository;

    CourseService courseService;

    BatchService batchService;

    ExamQuestionMcqService examQuestionMcqService;

    ExamQuestionTrueOrFalseService examQuestionTrueOrFalseService;

    ExamQuestionSubjectiveService examQuestionSubjectiveService;
    UserUtils userUtils;

    public ExamServiceImpl(ExamRepository examRepository,
                           CourseService courseService,
                           BatchService batchService,
                           ExamQuestionMcqService examQuestionMcqService,
                           ExamQuestionTrueOrFalseService examQuestionTrueOrFalseService,
                           ExamQuestionSubjectiveService examQuestionSubjectiveService,
                           UserUtils userUtils) {
        this.examRepository = examRepository;
        this.courseService = courseService;
        this.batchService = batchService;
        this.examQuestionMcqService = examQuestionMcqService;
        this.examQuestionTrueOrFalseService = examQuestionTrueOrFalseService;
        this.examQuestionSubjectiveService = examQuestionSubjectiveService;
        this.userUtils = userUtils;
    }

    private Boolean checkPermission() {
        LmsUser currentUser = userUtils.getCurrentUser();
        return currentUser.getIsAdmin() || currentUser.getIsInstructor();
    }

    public Exam toExam(NewExamRequest request) {
        Exam exam = new Exam();

        if (request.getCourseId() != null) {
            exam.setCourse(courseService.getCourseById(request.getCourseId()));
        }
        if (request.getBatchId() != null) {
            exam.setBatch(batchService.getBatch(request.getBatchId()));
        }

        exam.setExamName(request.getExamName());
        exam.setExamType(request.getExamType());
        exam.setDurationMins(request.getDurationMins());
        exam.setExamPublishDate(request.getExamPublishDate());
        exam.setExamExpiryDate(request.getExamExpiryDate());
        exam.setTotalMarks(request.getTotalMarks());
        exam.setPassingMarks(request.getPassingMarks());

        if (request.getMcqQuestions() != null) {
            exam.setExamQuestionMcqs(request.toExamQuestionMcqList());
            exam.getExamQuestionMcqs().forEach(examQuestionMcq -> examQuestionMcq.setExam(exam));
        }
        if (request.getSubjectiveQuestions() != null) {
            exam.setExamQuestionSubjectives(request.toExamQuestionSubjectiveList());
            exam.getExamQuestionSubjectives().forEach(examQuestionSubjective
                    -> examQuestionSubjective.setExam(exam));
        }
        if (request.getTrueOrFalseQuestions() != null) {
            exam.setExamQuestionTrueOrFalses(request.toExamQuestionTrueOrFalseList());
            exam.getExamQuestionTrueOrFalses().forEach(examQuestionTrueOrFalse
                    -> examQuestionTrueOrFalse.setExam(exam));
        }
        return exam;
    }


    @Override
    public Exam getExam(long id) {
        List<Exam> exams =
                examRepository.findAllById(new ArrayList<>(List.of(id)));
        if (exams.size() == 0) {
            throw new ResourceNotFoundException("Exam with Id " + id + " not found");
        }
        return exams.get(0);
    }

    @Override
    public List<Exam> getAllExams() {
        return examRepository.findAll();
    }

    @Override
    public List<Exam> getAllExamsForCourse(Long courseId) {
        return examRepository.findAllByCourse_Id(courseId);
    }

    @Override
    public List<Exam> getAllExamsForBatch(Long batchId) {
        return examRepository.findAllByBatch_Id(batchId);
    }

    @Override
    public NewExamQuestionsAllTypesResponse getAllQuestions(Long id) {
        Exam existingExam = getExam(id);
        if (existingExam != null) {
            return existingExam.fetchAllExamQuestions();
        }
        return null;
    }

    @Override
    public Exam saveExam(Exam exam) {
        LmsUser currentUser = userUtils.getCurrentUser();

        if (!exam.validateFields()) {
            logger.error("Exam fields validation failed");
            throw new ValidationFailureException("Exam fields are not valid");
        }
        if (!checkPermission()) {
            logger.error("User does not have permission to perform this operation");
            throw new OperationNotSupportedException("User does not have permission to perform "
                    + "this operation");
        }

        exam.setCreatedBy(currentUser);
        return examRepository.save(exam);
    }


    @Override
    public Exam addQuestionsToExam(NewExamQuestionsAllTypesRequest newExamQuestionRequest,
                                   long id) {
        Exam existingExam = getExam(id);
        if (existingExam == null) {
            logger.error("Exam with Id " + id + " not found");
            throw new ResourceNotFoundException("Exam with Id " + id + " not found");
        }
        if (!checkPermission()) {
            logger.error("User does not have permission to perform this operation");
            throw new OperationNotSupportedException("User does not have permission to perform "
                    + "this operation");
        }

        if (newExamQuestionRequest.getMcqQuestions() != null) {
            existingExam.addMcqQuestions(newExamQuestionRequest.getMcqQuestions());
        }
        if (newExamQuestionRequest.getSubjectiveQuestions() != null) {
            existingExam.addSubjectiveQuestions(newExamQuestionRequest.getSubjectiveQuestions());
        }
        if (newExamQuestionRequest.getTrueOrFalseQuestions() != null) {
            existingExam.addTrueOrFalseQuestions(newExamQuestionRequest.getTrueOrFalseQuestions());
        }

        if (!existingExam.validateFields()) {
            logger.error("Exam fields validation failed");
            throw new ValidationFailureException("Exam fields are not valid");
        }

        LmsUser currentUser = userUtils.getCurrentUser();
        existingExam.setUpdatedBy(currentUser);

        return examRepository.save(existingExam);
    }

    @Override
    public Exam updateExams(Exam exam, long id) {
        Exam existingExam = getExam(id);
        if (existingExam == null) {
            logger.error("Exam with Id " + id + " not found");
            throw new ResourceNotFoundException("Exam with Id " + id + " not found");
        }
        existingExam.update(exam);
        if (!existingExam.validateFields()) {
            logger.error("Exam fields validation failed");
            throw new ValidationFailureException("Exam fields are not valid");
        }
        if (!checkPermission()) {
            logger.error("User does not have permission to perform this operation");
            throw new OperationNotSupportedException("User does not have permission to perform "
                    + "this operation");
        }

        LmsUser currentUser = userUtils.getCurrentUser();
        exam.setUpdatedBy(currentUser);

        return examRepository.save(existingExam);
    }

    @Override
    public void hardDeleteExam(long id) {
        Exam existingExam = getExam(id);
        if (existingExam == null) {
            logger.error("Exam with Id " + id + " not found");
            throw new ResourceNotFoundException("Exam with Id " + id + " not found");
        }
        if (!checkPermission()) {
            logger.error("User does not have permission to perform this operation");
            throw new OperationNotSupportedException("User does not have permission to perform "
                    + "this operation");
        }
        examRepository.delete(existingExam);
    }

    @Override
    public void deleteQuestions(Long examId, DeleteExamQuestionsRequest questionIds) {
        Exam existingExam = getExam(examId);
        if (existingExam == null) {
            logger.error("Exam with Id " + examId + " not found");
            throw new ResourceNotFoundException("Exam with Id " + examId + " not found");
        }
        if (!checkPermission()) {
            logger.error("User does not have permission to perform this operation");
            throw new OperationNotSupportedException("User does not have permission to perform "
                    + "this operation");
        }
        examQuestionMcqService.hardDeleteQuestions(questionIds.getMcqQuestionsIds(), existingExam);
        examQuestionSubjectiveService.hardDeleteQuestions(questionIds.getSubjectiveQuestionsIds(),
                existingExam);
        examQuestionTrueOrFalseService.hardDeleteQuestions(questionIds.getTrueOrFalseQuestionsIds(),
                existingExam);
    }
}
