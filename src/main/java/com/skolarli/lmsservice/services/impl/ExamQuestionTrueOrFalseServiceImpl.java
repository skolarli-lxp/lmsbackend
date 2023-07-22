package com.skolarli.lmsservice.services.impl;

import com.skolarli.lmsservice.exception.OperationNotSupportedException;
import com.skolarli.lmsservice.exception.ResourceNotFoundException;
import com.skolarli.lmsservice.models.db.core.LmsUser;
import com.skolarli.lmsservice.models.db.exam.Exam;
import com.skolarli.lmsservice.models.db.exam.ExamQuestionTrueOrFalse;
import com.skolarli.lmsservice.models.dto.exam.NewExamQuestionTrueOrFalseRequest;
import com.skolarli.lmsservice.repository.ExamQuestionTrueOrFalseRepository;
import com.skolarli.lmsservice.services.ExamQuestionTrueOrFalseService;
import com.skolarli.lmsservice.utils.UserUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class ExamQuestionTrueOrFalseServiceImpl implements ExamQuestionTrueOrFalseService {

    final ExamQuestionTrueOrFalseRepository examQuestionTrueOrFalseRepository;
    final UserUtils userUtils;


    public ExamQuestionTrueOrFalseServiceImpl(ExamQuestionTrueOrFalseRepository
                                                      examQuestionTrueOrFalseRepository,
                                              UserUtils userUtils) {
        this.examQuestionTrueOrFalseRepository = examQuestionTrueOrFalseRepository;
        this.userUtils = userUtils;
    }

    private Boolean checkPermission(Exam exam) {
        LmsUser currentUser = userUtils.getCurrentUser();
        return currentUser.getIsAdmin() || currentUser == exam.getCreatedBy();
    }


    @Override
    public ExamQuestionTrueOrFalse toExamQuestionTrueOrFalse(
            NewExamQuestionTrueOrFalseRequest newExamQuestionTrueOrFalseRequest) {
        ExamQuestionTrueOrFalse examQuestionTrueOrFalse = new ExamQuestionTrueOrFalse();

        examQuestionTrueOrFalse.setQuestion(newExamQuestionTrueOrFalseRequest.getQuestion());
        if (newExamQuestionTrueOrFalseRequest.getResourceFileRequest() != null) {
            examQuestionTrueOrFalse.setQuestionResourceFile(
                    newExamQuestionTrueOrFalseRequest.getResourceFileRequest().toResourceFile());
        }
        examQuestionTrueOrFalse.setQuestionType(
                newExamQuestionTrueOrFalseRequest.getQuestionType());
        examQuestionTrueOrFalse.setDifficultyLevel(newExamQuestionTrueOrFalseRequest
                .getDifficultyLevel());

        examQuestionTrueOrFalse.setQuestionFormat(
                newExamQuestionTrueOrFalseRequest.getQuestionFormat());
        examQuestionTrueOrFalse.setAnswerFormat(
                newExamQuestionTrueOrFalseRequest.getAnswerFormat());
        examQuestionTrueOrFalse.setSampleAnswerText(newExamQuestionTrueOrFalseRequest
                .getSampleAnswerText());
        examQuestionTrueOrFalse.setSampleAnswerUrl(newExamQuestionTrueOrFalseRequest
                .getSampleAnswerUrl());

        examQuestionTrueOrFalse.setOption1(newExamQuestionTrueOrFalseRequest.getOption1());
        examQuestionTrueOrFalse.setOption2(newExamQuestionTrueOrFalseRequest.getOption2());
        examQuestionTrueOrFalse.setCorrectAnswer(
                newExamQuestionTrueOrFalseRequest.getCorrectAnswer());

        return examQuestionTrueOrFalse;
    }

    @Override
    public ExamQuestionTrueOrFalse getQuestion(long id) {
        List<ExamQuestionTrueOrFalse> examQuestionTrueOrFalses =
                examQuestionTrueOrFalseRepository.findAllById(new ArrayList<>(List.of(id)));
        if (examQuestionTrueOrFalses.size() == 0) {
            throw new ResourceNotFoundException("Question with Id " + id + " not found");
        }
        return examQuestionTrueOrFalses.get(0);
    }

    @Override
    public List<ExamQuestionTrueOrFalse> getAllQuestions() {
        return examQuestionTrueOrFalseRepository.findAll();
    }

    @Override
    public ExamQuestionTrueOrFalse saveQuestion(ExamQuestionTrueOrFalse question, Exam exam) {
        if (checkPermission(exam)) {
            return examQuestionTrueOrFalseRepository.save(question);
        } else {
            throw new OperationNotSupportedException("User does not have permission to perform "
                    + "this operation");
        }
    }

    @Override
    public List<ExamQuestionTrueOrFalse> saveAllQuestions(List<ExamQuestionTrueOrFalse> questions,
                                                          Exam exam) {
        if (checkPermission(exam)) {
            return examQuestionTrueOrFalseRepository.saveAll(questions);
        } else {
            throw new OperationNotSupportedException("User does not have permission to perform "
                    + "this operation");
        }
    }

    @Override
    public ExamQuestionTrueOrFalse updateQuestion(ExamQuestionTrueOrFalse question, long id) {
        ExamQuestionTrueOrFalse existingQuestion = getQuestion(id);
        if (!checkPermission(existingQuestion.getExam())) {
            throw new OperationNotSupportedException("User does not have permission to perform "
                    + "this operation");
        }
        existingQuestion.update(question);
        return examQuestionTrueOrFalseRepository.save(existingQuestion);
    }

    @Override
    public void hardDeleteQuestion(long id) {
        ExamQuestionTrueOrFalse existingQuestion = getQuestion(id);
        if (!checkPermission(existingQuestion.getExam())) {
            throw new OperationNotSupportedException("User does not have permission to perform "
                    + "this operation");
        }

        examQuestionTrueOrFalseRepository.delete(existingQuestion);
    }

    @Override
    public void hardDeleteQuestions(List<Long> ids, Exam exam) {
        if (checkPermission(exam)) {
            examQuestionTrueOrFalseRepository.deleteAllById(ids);
        } else {
            throw new OperationNotSupportedException("User does not have permission to perform "
                    + "this operation");
        }
    }
}
