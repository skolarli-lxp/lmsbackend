package com.skolarli.lmsservice.services.impl.exam;

import com.skolarli.lmsservice.exception.OperationNotSupportedException;
import com.skolarli.lmsservice.exception.ResourceNotFoundException;
import com.skolarli.lmsservice.models.db.core.LmsUser;
import com.skolarli.lmsservice.models.db.exam.Exam;
import com.skolarli.lmsservice.models.db.exam.ExamQuestionSubjective;
import com.skolarli.lmsservice.models.db.questionbank.BankQuestionSubjective;
import com.skolarli.lmsservice.repository.exam.ExamQuestionSubjectiveRepository;
import com.skolarli.lmsservice.services.exam.ExamQuestionSubjectiveService;
import com.skolarli.lmsservice.utils.UserUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class ExamQuestionSubjectiveServiceImpl implements ExamQuestionSubjectiveService {

    final ExamQuestionSubjectiveRepository examQuestionSubjectiveRepository;
    final UserUtils userUtils;


    public ExamQuestionSubjectiveServiceImpl(ExamQuestionSubjectiveRepository
                                                     examQuestionSubjectiveRepository,
                                             UserUtils userUtils) {
        this.examQuestionSubjectiveRepository = examQuestionSubjectiveRepository;
        this.userUtils = userUtils;
    }

    private Boolean checkPermission(Exam exam) {
        LmsUser currentUser = userUtils.getCurrentUser();
        return currentUser.getIsAdmin() || currentUser == exam.getCreatedBy();
    }

    public BankQuestionSubjective toBankQuestionSubjective(ExamQuestionSubjective
                                                                   examQuestionSubjective) {
        return new BankQuestionSubjective(examQuestionSubjective);
    }

    @Override
    public List<BankQuestionSubjective> toBankQuestionSubjective(
            List<Long> examQuestionSubjectiveIds) {

        List<ExamQuestionSubjective> examQuestionSubjectives =
                examQuestionSubjectiveRepository.findAllById(examQuestionSubjectiveIds);
        return examQuestionSubjectives.stream().map(this::toBankQuestionSubjective)
                .collect(Collectors.toList());
    }

    @Override
    public ExamQuestionSubjective getQuestion(long id) {
        List<ExamQuestionSubjective> bankQuestionMcqs =
                examQuestionSubjectiveRepository.findAllById(new ArrayList<>(List.of(id)));
        if (bankQuestionMcqs.size() == 0) {
            throw new ResourceNotFoundException("Question with Id " + id + " not found");
        }
        return bankQuestionMcqs.get(0);
    }

    @Override
    public List<ExamQuestionSubjective> getAllQuestions() {
        return examQuestionSubjectiveRepository.findAll();
    }

    @Override
    public Integer getMaxQuestionSortOrder(Long examId) {
        return examQuestionSubjectiveRepository.findMaxQuestionSortOrder(examId);
    }

    @Override
    public ExamQuestionSubjective saveQuestion(ExamQuestionSubjective question, Exam exam) {
        LmsUser currentUser = userUtils.getCurrentUser();
        if (checkPermission(exam)) {
            return examQuestionSubjectiveRepository.save(question);
        } else {
            throw new UnsupportedOperationException("User does not have permission to perform "
                    + "this operation");
        }
    }

    @Override
    public List<ExamQuestionSubjective> saveAllQuestions(List<ExamQuestionSubjective> questions,
                                                         Exam exam) {
        if (checkPermission(exam)) {
            return examQuestionSubjectiveRepository.saveAll(questions);
        } else {
            throw new OperationNotSupportedException("User does not have permission to perform "
                    + "this operation");
        }
    }

    @Override
    public ExamQuestionSubjective updateQuestion(ExamQuestionSubjective question, long id) {
        ExamQuestionSubjective existingQuestion = getQuestion(id);
        if (!checkPermission(existingQuestion.getExam())) {
            throw new OperationNotSupportedException("User does not have permission to perform "
                    + "this operation");
        }
        existingQuestion.update(question);
        return examQuestionSubjectiveRepository.save(existingQuestion);
    }

    @Override
    public void hardDeleteQuestion(long id) {
        ExamQuestionSubjective existingQuestion = getQuestion(id);
        if (!checkPermission(existingQuestion.getExam())) {
            throw new OperationNotSupportedException("User does not have permission to perform "
                    + "this operation");
        }
        examQuestionSubjectiveRepository.delete(existingQuestion);
    }

    @Override
    public void hardDeleteQuestions(List<Long> ids, Exam exam) {
        if (checkPermission(exam)) {
            examQuestionSubjectiveRepository.deleteAllById(ids);
        } else {
            throw new OperationNotSupportedException("User does not have permission to perform "
                    + "this operation");
        }
    }
}
