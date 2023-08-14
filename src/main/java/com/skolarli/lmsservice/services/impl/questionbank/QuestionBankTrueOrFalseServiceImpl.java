package com.skolarli.lmsservice.services.impl.questionbank;

import com.skolarli.lmsservice.exception.OperationNotSupportedException;
import com.skolarli.lmsservice.exception.ResourceNotFoundException;
import com.skolarli.lmsservice.models.db.core.LmsUser;
import com.skolarli.lmsservice.models.db.course.Course;
import com.skolarli.lmsservice.models.db.exam.Exam;
import com.skolarli.lmsservice.models.db.exam.ExamQuestionTrueOrFalse;
import com.skolarli.lmsservice.models.db.questionbank.BankQuestionTrueOrFalse;
import com.skolarli.lmsservice.models.dto.questionbank.NewBankQuestionTrueOrFalseRequest;
import com.skolarli.lmsservice.repository.exam.ExamQuestionTrueOrFalseRepository;
import com.skolarli.lmsservice.repository.questionbank.QuestionBankTrueOrFalseRepository;
import com.skolarli.lmsservice.services.course.CourseService;
import com.skolarli.lmsservice.services.exam.ExamService;
import com.skolarli.lmsservice.services.questionbank.QuestionBankTrueOrFalseService;
import com.skolarli.lmsservice.utils.UserUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class QuestionBankTrueOrFalseServiceImpl implements QuestionBankTrueOrFalseService {

    final QuestionBankTrueOrFalseRepository questionBankTrueOrFalseRepository;

    final ExamQuestionTrueOrFalseRepository examQuestionTrueOrFalseRepository;
    final UserUtils userUtils;

    final CourseService courseService;

    ExamService examService;

    public QuestionBankTrueOrFalseServiceImpl(QuestionBankTrueOrFalseRepository
                                                      questionBankTrueOrFalseRepository,
                                              ExamQuestionTrueOrFalseRepository
                                                      examQuestionTrueOrFalseRepository,
                                              UserUtils userUtils,
                                              CourseService courseService,
                                              ExamService examService) {
        this.questionBankTrueOrFalseRepository = questionBankTrueOrFalseRepository;
        this.examQuestionTrueOrFalseRepository = examQuestionTrueOrFalseRepository;
        this.userUtils = userUtils;
        this.courseService = courseService;
        this.examService = examService;
    }

    private Boolean checkPermission() {
        LmsUser currentUser = userUtils.getCurrentUser();
        return currentUser.getIsAdmin() || currentUser.getIsInstructor();
    }


    @Override
    public BankQuestionTrueOrFalse toBankQuestionTrueOrFalse(
            NewBankQuestionTrueOrFalseRequest newBankQuestionTrueOrFalseRequest) {
        BankQuestionTrueOrFalse bankQuestionTrueOrFalse = new BankQuestionTrueOrFalse();

        if (newBankQuestionTrueOrFalseRequest.getCourseId() != null) {
            Course course = courseService.getCourseById(
                    newBankQuestionTrueOrFalseRequest.getCourseId());
            bankQuestionTrueOrFalse.setCourse(course);
        }
        bankQuestionTrueOrFalse.setQuestion(newBankQuestionTrueOrFalseRequest.getQuestion());
        bankQuestionTrueOrFalse.setQuestionType(
                newBankQuestionTrueOrFalseRequest.getQuestionType());
        bankQuestionTrueOrFalse.setDifficultyLevel(newBankQuestionTrueOrFalseRequest
                .getDifficultyLevel());

        bankQuestionTrueOrFalse.setQuestionFormat(
                newBankQuestionTrueOrFalseRequest.getQuestionFormat());
        bankQuestionTrueOrFalse.setAnswerFormat(
                newBankQuestionTrueOrFalseRequest.getAnswerFormat());
        bankQuestionTrueOrFalse.setSampleAnswerText(newBankQuestionTrueOrFalseRequest
                .getSampleAnswerText());
        bankQuestionTrueOrFalse.setSampleAnswerUrl(newBankQuestionTrueOrFalseRequest
                .getSampleAnswerUrl());

        bankQuestionTrueOrFalse.setOption1(newBankQuestionTrueOrFalseRequest.getOption1());
        bankQuestionTrueOrFalse.setOption2(newBankQuestionTrueOrFalseRequest.getOption2());
        bankQuestionTrueOrFalse.setCorrectAnswer(
                newBankQuestionTrueOrFalseRequest.getCorrectAnswer());

        return bankQuestionTrueOrFalse;
    }

    public ExamQuestionTrueOrFalse toExamQuestionTrueOrFalse(
            BankQuestionTrueOrFalse bankQuestionTrueOrFalse, Integer marks,
            Exam existingExam) {

        ExamQuestionTrueOrFalse examQuestionTrueOrFalse = new ExamQuestionTrueOrFalse(
                bankQuestionTrueOrFalse, marks, existingExam);
        return examQuestionTrueOrFalse;
    }

    @Override
    public List<ExamQuestionTrueOrFalse> toExamQuestionTrueOrFalse(
            List<Long> bankQuestionTrueOrFalseIds, List<Integer> marks,
            Long examId) {
        Exam existingExam = examService.getExam(examId);
        if (existingExam == null) {
            throw new ResourceNotFoundException("Exam with Id " + examId + " not found");
        }
        List<BankQuestionTrueOrFalse> bankQuestionTrueOrFalse =
                questionBankTrueOrFalseRepository.findAllById(bankQuestionTrueOrFalseIds);
        List<ExamQuestionTrueOrFalse> examQuestionTrueOrFalses = new ArrayList<>();
        for (int i = 0; i < bankQuestionTrueOrFalse.size(); i++) {
            examQuestionTrueOrFalses.add(toExamQuestionTrueOrFalse(
                    bankQuestionTrueOrFalse.get(i), marks.get(i), existingExam));
        }

        List<ExamQuestionTrueOrFalse> savedQuestions =
                examQuestionTrueOrFalseRepository.saveAll(examQuestionTrueOrFalses);
        return savedQuestions;
    }

    @Override
    public BankQuestionTrueOrFalse getQuestion(long id) {
        List<BankQuestionTrueOrFalse> bankQuestionTrueOrFalses =
                questionBankTrueOrFalseRepository.findAllById(new ArrayList<>(List.of(id)));
        if (bankQuestionTrueOrFalses.size() == 0) {
            throw new ResourceNotFoundException("Question with Id " + id + " not found");
        }
        return bankQuestionTrueOrFalses.get(0);
    }

    @Override
    public List<BankQuestionTrueOrFalse> getAllQuestions() {
        return questionBankTrueOrFalseRepository.findAll();
    }

    @Override
    public BankQuestionTrueOrFalse saveQuestion(BankQuestionTrueOrFalse question) {
        LmsUser currentUser = userUtils.getCurrentUser();
        question.setCreatedBy(currentUser);
        if (checkPermission()) {
            return questionBankTrueOrFalseRepository.save(question);
        } else {
            throw new OperationNotSupportedException("User does not have permission to perform "
                    + "this operation");
        }
    }

    @Override
    public List<BankQuestionTrueOrFalse> saveAllQuestions(List<BankQuestionTrueOrFalse> questions) {
        LmsUser currentUser = userUtils.getCurrentUser();
        questions.forEach(question -> question.setCreatedBy(currentUser));
        if (checkPermission()) {
            return questionBankTrueOrFalseRepository.saveAll(questions);
        } else {
            throw new OperationNotSupportedException("User does not have permission to perform "
                    + "this operation");
        }
    }

    @Override
    public BankQuestionTrueOrFalse updateQuestion(BankQuestionTrueOrFalse question, long id) {
        LmsUser currentUser = userUtils.getCurrentUser();
        BankQuestionTrueOrFalse existingQuestion = getQuestion(id);
        if (!currentUser.getIsAdmin() && existingQuestion.getCreatedBy() != currentUser) {
            throw new OperationNotSupportedException("User does not have permission to perform "
                    + "this operation");
        }
        existingQuestion.setUpdatedBy(currentUser);
        existingQuestion.update(question);
        return questionBankTrueOrFalseRepository.save(existingQuestion);
    }

    @Override
    public void hardDeleteQuestion(long id) {
        LmsUser currentUser = userUtils.getCurrentUser();
        BankQuestionTrueOrFalse existingQuestion = getQuestion(id);
        if (!currentUser.getIsAdmin() && existingQuestion.getCreatedBy() != currentUser) {
            throw new OperationNotSupportedException("User does not have permission to perform "
                    + "this operation");
        }
        questionBankTrueOrFalseRepository.delete(existingQuestion);
    }
}
