package com.skolarli.lmsservice.services.impl;

import com.skolarli.lmsservice.exception.OperationNotSupportedException;
import com.skolarli.lmsservice.exception.ResourceNotFoundException;
import com.skolarli.lmsservice.exception.ValidationFailureException;
import com.skolarli.lmsservice.models.db.BankQuestionMcq;
import com.skolarli.lmsservice.models.db.Course;
import com.skolarli.lmsservice.models.db.LmsUser;
import com.skolarli.lmsservice.models.dto.questionbank.NewBankQuestionMcqRequest;
import com.skolarli.lmsservice.repository.QuestionBankMcqRepository;
import com.skolarli.lmsservice.services.CourseService;
import com.skolarli.lmsservice.services.QuestionBankMcqService;
import com.skolarli.lmsservice.utils.UserUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class QuestionBankMcqServiceImpl implements QuestionBankMcqService {

    final QuestionBankMcqRepository questionBankMcqRepository;
    final CourseService courseService;

    final UserUtils userUtils;

    public QuestionBankMcqServiceImpl(QuestionBankMcqRepository questionBankMcqRepository,
                                      UserUtils userUtils,
                                      CourseService courseService) {
        this.questionBankMcqRepository = questionBankMcqRepository;
        this.userUtils = userUtils;
        this.courseService = courseService;
    }

    public BankQuestionMcq toBankQuestionMcq(NewBankQuestionMcqRequest newBankQuestionMcqRequest) {
        BankQuestionMcq bankQuestionMcq = new BankQuestionMcq();
        if (newBankQuestionMcqRequest.getCourseId() != null) {
            Course course = courseService.getCourseById(newBankQuestionMcqRequest.getCourseId());
            bankQuestionMcq.setCourse(course);
        }
        bankQuestionMcq.setQuestion(newBankQuestionMcqRequest.getQuestion());
        bankQuestionMcq.setQuestionType(newBankQuestionMcqRequest.getQuestionType());
        bankQuestionMcq.setDifficultyLevel(newBankQuestionMcqRequest.getDifficultyLevel());
        bankQuestionMcq.setQuestionFormat(newBankQuestionMcqRequest.getQuestionFormat());
        bankQuestionMcq.setAnswerFormat(newBankQuestionMcqRequest.getAnswerFormat());

        bankQuestionMcq.setNumberOfOptions(newBankQuestionMcqRequest.getNumberOfOptions());
        bankQuestionMcq.setOption1(newBankQuestionMcqRequest.getOption1());
        bankQuestionMcq.setOption2(newBankQuestionMcqRequest.getOption2());
        bankQuestionMcq.setOption3(newBankQuestionMcqRequest.getOption3());
        bankQuestionMcq.setOption4(newBankQuestionMcqRequest.getOption4());
        bankQuestionMcq.setOption5(newBankQuestionMcqRequest.getOption5());
        bankQuestionMcq.setOption6(newBankQuestionMcqRequest.getOption6());

        if (newBankQuestionMcqRequest.getCorrectAnswer() != null) {
            bankQuestionMcq.setCorrectAnswer(newBankQuestionMcqRequest.getCorrectAnswer()
                    .stream().map(String::valueOf).collect(Collectors.joining(",")));
        } else {
            bankQuestionMcq.setCorrectAnswer("");
        }

        bankQuestionMcq.setSampleAnswerText(newBankQuestionMcqRequest.getSampleAnswerText());
        bankQuestionMcq.setSampleAnswerUrl(newBankQuestionMcqRequest.getSampleAnswerUrl());
        bankQuestionMcq.setNumberOfCorrectAnswers(newBankQuestionMcqRequest
                .getNumberOfCorrectAnswers());

        return bankQuestionMcq;
    }

    private Boolean checkPermission() {
        LmsUser currentUser = userUtils.getCurrentUser();
        return currentUser.getIsAdmin() || currentUser.getIsInstructor();
    }

    @Override
    public BankQuestionMcq getQuestion(long id) {
        List<BankQuestionMcq> bankQuestionMcqs =
                questionBankMcqRepository.findAllById(new ArrayList<>(List.of(id)));
        if (bankQuestionMcqs.size() == 0) {
            throw new ResourceNotFoundException("Question with Id " + id + " not found");
        }
        return bankQuestionMcqs.get(0);
    }

    @Override
    public List<BankQuestionMcq> getAllQuestions() {
        return questionBankMcqRepository.findAll();
    }

    @Override
    public BankQuestionMcq saveQuestion(BankQuestionMcq question) {
        if (!question.validateFields()) {
            throw new ValidationFailureException("Question fields are not valid");
        }
        LmsUser currentUser = userUtils.getCurrentUser();
        question.setCreatedBy(currentUser);
        if (checkPermission()) {
            return questionBankMcqRepository.save(question);
        } else {
            throw new OperationNotSupportedException("User does not have permission to perform "
                    + "this operation");
        }
    }

    @Override
    public List<BankQuestionMcq> saveAllQuestions(List<BankQuestionMcq> questions) {
        LmsUser currentUser = userUtils.getCurrentUser();
        questions.forEach(question -> {
            question.setCreatedBy(currentUser);
            if (!question.validateFields()) {
                throw new ValidationFailureException("Question fields are not valid");
            }
        });
        if (checkPermission()) {
            return questionBankMcqRepository.saveAll(questions);
        } else {
            throw new OperationNotSupportedException("User does not have permission to perform "
                    + "this operation");
        }
    }

    @Override
    public BankQuestionMcq updateQuestion(BankQuestionMcq question, long id) {
        LmsUser currentUser = userUtils.getCurrentUser();
        BankQuestionMcq existingQuestion = getQuestion(id);
        if (!currentUser.getIsAdmin() && existingQuestion.getCreatedBy() != currentUser) {
            throw new OperationNotSupportedException("User does not have permission to perform "
                    + "this operation");
        }
        existingQuestion.setUpdatedBy(currentUser);
        existingQuestion.update(question);
        if (!question.validateFields()) {
            throw new ValidationFailureException("Question fields are not valid");
        }
        return questionBankMcqRepository.save(existingQuestion);
    }

    @Override
    public void hardDeleteQuestion(long id) {
        LmsUser currentUser = userUtils.getCurrentUser();
        BankQuestionMcq existingQuestion = getQuestion(id);
        if (!currentUser.getIsAdmin() && existingQuestion.getCreatedBy() != currentUser) {
            throw new OperationNotSupportedException("User does not have permission to perform "
                    + "this operation");
        }
        questionBankMcqRepository.deleteById(id);
    }
}
