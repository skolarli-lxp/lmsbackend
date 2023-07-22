package com.skolarli.lmsservice.services.impl.questionbank;

import com.skolarli.lmsservice.exception.OperationNotSupportedException;
import com.skolarli.lmsservice.exception.ResourceNotFoundException;
import com.skolarli.lmsservice.models.db.core.LmsUser;
import com.skolarli.lmsservice.models.db.course.Course;
import com.skolarli.lmsservice.models.db.questionbank.BankQuestionSubjective;
import com.skolarli.lmsservice.models.dto.questionbank.NewBankQuestionSubjectiveRequest;
import com.skolarli.lmsservice.repository.questionbank.QuestionBankSubjectiveRepository;
import com.skolarli.lmsservice.services.course.CourseService;
import com.skolarli.lmsservice.services.questionbank.QuestionBankSubjectiveService;
import com.skolarli.lmsservice.utils.UserUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class QuestionBankSubjectiveServiceImpl implements QuestionBankSubjectiveService {

    final QuestionBankSubjectiveRepository questionBankSubjectiveRepository;
    final UserUtils userUtils;

    final CourseService courseService;

    public QuestionBankSubjectiveServiceImpl(QuestionBankSubjectiveRepository
                                                     questionBankSubjectiveRepository,
                                             UserUtils userUtils,
                                             CourseService courseService) {
        this.questionBankSubjectiveRepository = questionBankSubjectiveRepository;
        this.userUtils = userUtils;
        this.courseService = courseService;
    }

    private Boolean checkPermission() {
        LmsUser currentUser = userUtils.getCurrentUser();
        return currentUser.getIsAdmin() || currentUser.getIsInstructor();
    }


    @Override
    public BankQuestionSubjective toBankQuestionSubjective(
            NewBankQuestionSubjectiveRequest newBankQuestionSubjectiveRequest) {
        BankQuestionSubjective bankQuestionSubjective = new BankQuestionSubjective();

        if (newBankQuestionSubjectiveRequest.getCourseId() != null) {
            Course course = courseService.getCourseById(
                    newBankQuestionSubjectiveRequest.getCourseId());
            bankQuestionSubjective.setCourse(course);
        }
        bankQuestionSubjective.setQuestion(newBankQuestionSubjectiveRequest.getQuestion());
        bankQuestionSubjective.setQuestionType(newBankQuestionSubjectiveRequest.getQuestionType());
        bankQuestionSubjective.setDifficultyLevel(newBankQuestionSubjectiveRequest
                .getDifficultyLevel());

        bankQuestionSubjective.setQuestionFormat(newBankQuestionSubjectiveRequest
                .getQuestionFormat());
        bankQuestionSubjective.setAnswerFormat(newBankQuestionSubjectiveRequest.getAnswerFormat());
        bankQuestionSubjective.setSampleAnswerText(newBankQuestionSubjectiveRequest
                .getSampleAnswerText());
        bankQuestionSubjective.setSampleAnswerUrl(newBankQuestionSubjectiveRequest
                .getSampleAnswerUrl());


        bankQuestionSubjective.setWordCount(newBankQuestionSubjectiveRequest.getWordCount());
        bankQuestionSubjective.setCorrectAnswer(newBankQuestionSubjectiveRequest
                .getCorrectAnswer());

        return bankQuestionSubjective;
    }

    @Override
    public BankQuestionSubjective getQuestion(long id) {
        List<BankQuestionSubjective> bankQuestionMcqs =
                questionBankSubjectiveRepository.findAllById(new ArrayList<>(List.of(id)));
        if (bankQuestionMcqs.size() == 0) {
            throw new ResourceNotFoundException("Question with Id " + id + " not found");
        }
        return bankQuestionMcqs.get(0);
    }

    @Override
    public List<BankQuestionSubjective> getAllQuestions() {
        return questionBankSubjectiveRepository.findAll();
    }

    @Override
    public BankQuestionSubjective saveQuestion(BankQuestionSubjective question) {
        LmsUser currentUser = userUtils.getCurrentUser();
        question.setCreatedBy(currentUser);
        if (checkPermission()) {
            return questionBankSubjectiveRepository.save(question);
        } else {
            throw new UnsupportedOperationException("User does not have permission to perform "
                    + "this operation");
        }
    }

    @Override
    public List<BankQuestionSubjective> saveAllQuestions(List<BankQuestionSubjective> questions) {
        LmsUser currentUser = userUtils.getCurrentUser();
        questions.forEach(question -> question.setCreatedBy(currentUser));
        if (checkPermission()) {
            return questionBankSubjectiveRepository.saveAll(questions);
        } else {
            throw new OperationNotSupportedException("User does not have permission to perform "
                    + "this operation");
        }
    }

    @Override
    public BankQuestionSubjective updateQuestion(BankQuestionSubjective question, long id) {
        LmsUser currentUser = userUtils.getCurrentUser();
        BankQuestionSubjective existingQuestion = getQuestion(id);
        if (!currentUser.getIsAdmin() && existingQuestion.getCreatedBy() != currentUser) {
            throw new OperationNotSupportedException("User does not have permission to perform "
                    + "this operation");
        }
        existingQuestion.update(question);
        return questionBankSubjectiveRepository.save(existingQuestion);
    }

    @Override
    public void hardDeleteQuestion(long id) {
        LmsUser currentUser = userUtils.getCurrentUser();
        BankQuestionSubjective existingQuestion = getQuestion(id);
        if (!currentUser.getIsAdmin() && existingQuestion.getCreatedBy() != currentUser) {
            throw new OperationNotSupportedException("User does not have permission to perform "
                    + "this operation");
        }
        questionBankSubjectiveRepository.delete(existingQuestion);
    }
}
