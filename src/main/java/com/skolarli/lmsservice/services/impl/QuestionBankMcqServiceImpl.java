package com.skolarli.lmsservice.services.impl;

import com.skolarli.lmsservice.exception.OperationNotSupportedException;
import com.skolarli.lmsservice.exception.ResourceNotFoundException;
import com.skolarli.lmsservice.models.db.BankQuestionMcq;
import com.skolarli.lmsservice.models.db.Course;
import com.skolarli.lmsservice.models.db.LmsUser;
import com.skolarli.lmsservice.models.dto.NewBankQuestionMcqRequest;
import com.skolarli.lmsservice.repository.QuestionBankMcqRepository;
import com.skolarli.lmsservice.services.CourseService;
import com.skolarli.lmsservice.services.QuestionBankMcqService;
import com.skolarli.lmsservice.utils.UserUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


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
        bankQuestionMcq.setQuestion(newBankQuestionMcqRequest.getQuestion());
        bankQuestionMcq.setNumberOfAnswers(newBankQuestionMcqRequest.getNumberOfAnswers());
        bankQuestionMcq.setQuestionType(newBankQuestionMcqRequest.getQuestionType());
        bankQuestionMcq.setAnswerType(newBankQuestionMcqRequest.getAnswerType());
        bankQuestionMcq.setAnswer1(newBankQuestionMcqRequest.getAnswer1());
        bankQuestionMcq.setAnswer2(newBankQuestionMcqRequest.getAnswer2());
        bankQuestionMcq.setAnswer3(newBankQuestionMcqRequest.getAnswer3());
        bankQuestionMcq.setAnswer4(newBankQuestionMcqRequest.getAnswer4());
        bankQuestionMcq.setAnswer5(newBankQuestionMcqRequest.getAnswer5());
        bankQuestionMcq.setAnswer6(newBankQuestionMcqRequest.getAnswer6());
        bankQuestionMcq.setCorrectAnswer(newBankQuestionMcqRequest.getCorrectAnswer());
        if (newBankQuestionMcqRequest.getCourseId() != null) {
            Course course = courseService.getCourseById(newBankQuestionMcqRequest.getCourseId());
            bankQuestionMcq.setCourse(course);
        }
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
        questions.forEach(question -> question.setCreatedBy(currentUser));
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
        existingQuestion.update(question);
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
