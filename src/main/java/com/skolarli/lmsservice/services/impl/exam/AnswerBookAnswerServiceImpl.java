package com.skolarli.lmsservice.services.impl.exam;

import com.skolarli.lmsservice.models.db.exam.AnswerBook;
import com.skolarli.lmsservice.models.db.exam.AnswerMcq;
import com.skolarli.lmsservice.models.db.exam.AnswerSubjective;
import com.skolarli.lmsservice.models.db.exam.AnswerTrueFalse;
import com.skolarli.lmsservice.models.dto.exam.AnswerEvaulationRequest;
import com.skolarli.lmsservice.models.dto.exam.NewAnswerMcqRequest;
import com.skolarli.lmsservice.models.dto.exam.NewAnswerSubjectiveRequest;
import com.skolarli.lmsservice.models.dto.exam.NewAnswerTrueFalseRequest;
import com.skolarli.lmsservice.services.exam.AnswerBookAnswerService;
import com.skolarli.lmsservice.services.exam.ExamQuestionMcqService;
import com.skolarli.lmsservice.services.exam.ExamQuestionSubjectiveService;
import com.skolarli.lmsservice.services.exam.ExamQuestionTrueOrFalseService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AnswerBookAnswerServiceImpl implements AnswerBookAnswerService {

    ExamQuestionSubjectiveService examQuestionSubjectiveService;
    ExamQuestionTrueOrFalseService examQuestionTrueOrFalseService;
    ExamQuestionMcqService examQuestionMcqService;

    public AnswerBookAnswerServiceImpl(ExamQuestionSubjectiveService examQuestionSubjectiveService,
                                       ExamQuestionTrueOrFalseService examQuestionTrueOrFalseService,
                                       ExamQuestionMcqService examQuestionMcqService) {
        this.examQuestionSubjectiveService = examQuestionSubjectiveService;
        this.examQuestionTrueOrFalseService = examQuestionTrueOrFalseService;
        this.examQuestionMcqService = examQuestionMcqService;
    }

    @Override
    public AnswerMcq toAnswerMcq(NewAnswerMcqRequest newAnswerMcqRequest, AnswerBook answerBook) {

        AnswerMcq answerMcq = new AnswerMcq();
        answerMcq.setAnswerBook(answerBook);
        answerMcq.setAnswer(
                newAnswerMcqRequest.getAnswer().stream().map(
                        String::valueOf).collect(Collectors.joining(","))
        );
        answerMcq.setQuestion(examQuestionMcqService.getQuestion(newAnswerMcqRequest
                .getQuestionId()));
        answerMcq.setStudentRemarks(newAnswerMcqRequest.getStudentRemarks());
        answerMcq.autoEvaluate();
        return answerMcq;
    }

    @Override
    public AnswerSubjective toAnswerSubjective(NewAnswerSubjectiveRequest newAnswerSubjectiveRequest,
                                               AnswerBook answerBook) {
        AnswerSubjective answerSubjective = new AnswerSubjective();
        answerSubjective.setAnswerBook(answerBook);
        answerSubjective.setAnswer(newAnswerSubjectiveRequest.getAnswer());
        answerSubjective.setQuestion(examQuestionSubjectiveService.getQuestion(
                newAnswerSubjectiveRequest.getQuestionId()));
        answerSubjective.setStudentRemarks(newAnswerSubjectiveRequest.getStudentRemarks());
        return answerSubjective;
    }

    @Override
    public AnswerTrueFalse toAnswerTrueFalse(NewAnswerTrueFalseRequest newAnswerTrueFalseRequest,
                                             AnswerBook answerBook) {
        AnswerTrueFalse answerTrueFalse = new AnswerTrueFalse();
        answerTrueFalse.setAnswerBook(answerBook);
        answerTrueFalse.setAnswer(newAnswerTrueFalseRequest.getAnswer());
        answerTrueFalse.setQuestion(examQuestionTrueOrFalseService.getQuestion(
                newAnswerTrueFalseRequest.getQuestionId()));
        answerTrueFalse.setStudentRemarks(newAnswerTrueFalseRequest.getStudentRemarks());
        answerTrueFalse.autoEvaluate();
        return answerTrueFalse;
    }

    @Override
    public void manualEvaluateMcqAnswers(AnswerBook answerBook,
                                         List<AnswerEvaulationRequest> mcqEvaluationRequests) {
        List<AnswerMcq> mcqAnswers = answerBook.getMcqAnswers();
        if (mcqAnswers == null) {
            return;
        }
        List<Long> idList = mcqAnswers.stream().map(AnswerMcq::getId).collect(Collectors.toList());


        for (AnswerEvaulationRequest mcqEvaluationRequest : mcqEvaluationRequests) {
            Long mcqAnswerId = mcqEvaluationRequest.getAnswerBookAnswerId();
            if (idList.contains(mcqAnswerId)) {
                AnswerMcq answerMcq = mcqAnswers.stream().filter(
                        answer -> answer.getId().equals(mcqAnswerId)).findFirst().get();
                answerMcq.manualEvaluate(mcqEvaluationRequest.getMarksGiven(),
                        mcqEvaluationRequest.getEvaluatorRemarks(),
                        mcqEvaluationRequest.getEvaluationResult());
            }
        }
    }

    @Override
    public void manualEvaluateSubjectiveAnswers(AnswerBook answerBook,
                                                List<AnswerEvaulationRequest> subjectiveEvaluationRequests) {
        List<AnswerSubjective> subjectiveAnswers = answerBook.getSubjectiveAnswers();
        if (subjectiveAnswers == null) {
            return;
        }
        List<Long> idList = subjectiveAnswers.stream().map(AnswerSubjective::getId).collect(Collectors.toList());

        for (AnswerEvaulationRequest subjectiveEvalRequest : subjectiveEvaluationRequests) {
            Long subjectiveAnswerId = subjectiveEvalRequest.getAnswerBookAnswerId();
            if (idList.contains(subjectiveAnswerId)) {
                AnswerSubjective answerSubjective = subjectiveAnswers.stream().filter(
                        answer -> answer.getId().equals(subjectiveAnswerId)).findFirst().get();
                answerSubjective.manualEvaluate(subjectiveEvalRequest.getMarksGiven(),
                        subjectiveEvalRequest.getEvaluatorRemarks(),
                        subjectiveEvalRequest.getEvaluationResult());
            }
        }
    }

    @Override
    public void manualEvaluateTrueFalseAnswers(AnswerBook answerBook,
                                               List<AnswerEvaulationRequest> trueFalseEvaluationRequests) {
        List<AnswerTrueFalse> trueFalseAnswers = answerBook.getTrueFalseAnswers();
        if (trueFalseAnswers == null) {
            return;
        }
        List<Long> idList = trueFalseAnswers.stream().map(AnswerTrueFalse::getId).collect(Collectors.toList());

        for (AnswerEvaulationRequest trueFalseEvalRequest : trueFalseEvaluationRequests) {
            Long trueFalseAnswerId = trueFalseEvalRequest.getAnswerBookAnswerId();
            if (idList.contains(trueFalseAnswerId)) {
                AnswerTrueFalse answerTrueFalse = trueFalseAnswers.stream().filter(
                        answer -> answer.getId().equals(trueFalseAnswerId)).findFirst().get();
                answerTrueFalse.manualEvaluate(trueFalseEvalRequest.getMarksGiven(),
                        trueFalseEvalRequest.getEvaluatorRemarks(),
                        trueFalseEvalRequest.getEvaluationResult());
            }
        }
    }
}
