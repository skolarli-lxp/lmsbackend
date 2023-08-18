package com.skolarli.lmsservice.services.impl.exam;

import com.skolarli.lmsservice.exception.OperationNotSupportedException;
import com.skolarli.lmsservice.exception.ResourceNotFoundException;
import com.skolarli.lmsservice.exception.ValidationFailureException;
import com.skolarli.lmsservice.models.db.core.LmsUser;
import com.skolarli.lmsservice.models.db.exam.Exam;
import com.skolarli.lmsservice.models.db.exam.ExamQuestionMcq;
import com.skolarli.lmsservice.models.db.questionbank.BankQuestionMcq;
import com.skolarli.lmsservice.models.dto.exam.NewExamQuestionMcqRequest;
import com.skolarli.lmsservice.repository.exam.ExamQuestionMcqRepository;
import com.skolarli.lmsservice.services.exam.ExamQuestionMcqService;
import com.skolarli.lmsservice.utils.UserUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class ExamQuestionMcqServiceImpl implements ExamQuestionMcqService {

    final ExamQuestionMcqRepository examQuestionMcqRepository;

    final UserUtils userUtils;

    public ExamQuestionMcqServiceImpl(ExamQuestionMcqRepository examQuestionMcqRepository,
                                      UserUtils userUtils) {
        this.examQuestionMcqRepository = examQuestionMcqRepository;
        this.userUtils = userUtils;
    }


    public BankQuestionMcq toBankQuestionMcq(ExamQuestionMcq examQuestionMcq) {
        return new BankQuestionMcq(examQuestionMcq);
    }

    @Override
    public List<BankQuestionMcq> toBankQuestionMcq(List<Long> examQuestionMcqIds) {
        List<ExamQuestionMcq> examQuestionMcqs =
                examQuestionMcqRepository.findAllById(examQuestionMcqIds);
        return examQuestionMcqs.stream().map(this::toBankQuestionMcq).collect(Collectors.toList());
    }

    private Boolean checkPermission(Exam exam) {
        LmsUser currentUser = userUtils.getCurrentUser();
        return currentUser.getIsAdmin() || currentUser == exam.getCreatedBy();
    }

    @Override
    public ExamQuestionMcq getQuestion(long id) {
        List<ExamQuestionMcq> examQuestionMcqs =
                examQuestionMcqRepository.findAllById(new ArrayList<>(List.of(id)));
        if (examQuestionMcqs.size() == 0) {
            throw new ResourceNotFoundException("Question with Id " + id + " not found");
        }
        return examQuestionMcqs.get(0);
    }

    @Override
    public List<ExamQuestionMcq> getAllQuestions() {
        return examQuestionMcqRepository.findAll();
    }

    @Override
    public ExamQuestionMcq saveQuestion(ExamQuestionMcq question, Exam exam) {
        if (!question.validateFields()) {
            throw new ValidationFailureException("Question fields are not valid");
        }

        if (checkPermission(exam)) {
            question.setExam(exam);
            return examQuestionMcqRepository.save(question);
        } else {
            throw new OperationNotSupportedException("User does not have permission to perform "
                    + "this operation");
        }
    }

    @Override
    public Integer getMaxQuestionSortOrder(Long examId) {
        return examQuestionMcqRepository.findMaxQuestionSortOrder(examId);
    }

    @Override
    public List<ExamQuestionMcq> saveAllQuestions(List<ExamQuestionMcq> questions, Exam exam) {
        if (checkPermission(exam)) {
            questions.forEach(question -> {
                if (!question.validateFields()) {
                    throw new ValidationFailureException("Question fields are not valid");
                }
                question.setExam(exam);
            });
            return examQuestionMcqRepository.saveAll(questions);
        } else {
            throw new OperationNotSupportedException("User does not have permission to perform "
                    + "this operation");
        }
    }

    @Override
    public ExamQuestionMcq updateQuestion(ExamQuestionMcq question, long id) {
        ExamQuestionMcq existingQuestion = getQuestion(id);
        if (!checkPermission(existingQuestion.getExam())) {
            throw new OperationNotSupportedException("User does not have permission to perform "
                    + "this operation");
        }
        existingQuestion.update(question);
        if (!question.validateFields()) {
            throw new ValidationFailureException("Question fields are not valid");
        }
        return examQuestionMcqRepository.save(existingQuestion);
    }

    @Override
    public void hardDeleteQuestion(long id) {
        ExamQuestionMcq existingQuestion = getQuestion(id);
        if (!checkPermission(existingQuestion.getExam())) {
            throw new OperationNotSupportedException("User does not have permission to perform "
                    + "this operation");
        }
        examQuestionMcqRepository.deleteById(id);
    }

    @Override
    public void hardDeleteQuestions(List<Long> ids, Exam exam) {
        if (checkPermission(exam)) {
            examQuestionMcqRepository.deleteAllById(ids);
        } else {
            throw new OperationNotSupportedException("User does not have permission to perform "
                    + "this operation");
        }
    }
}
