package com.skolarli.lmsservice.services.impl.exam;

import com.skolarli.lmsservice.exception.ResourceNotFoundException;
import com.skolarli.lmsservice.models.EvaluationResult;
import com.skolarli.lmsservice.models.db.exam.AnswerBook;
import com.skolarli.lmsservice.models.db.exam.AnswerMcq;
import com.skolarli.lmsservice.models.db.exam.AnswerSubjective;
import com.skolarli.lmsservice.models.db.exam.AnswerTrueFalse;
import com.skolarli.lmsservice.models.dto.exam.answerbook.AnswerEvaulationRequest;
import com.skolarli.lmsservice.models.dto.exam.answerbook.NewAnswerMcqRequest;
import com.skolarli.lmsservice.models.dto.exam.answerbook.NewAnswerSubjectiveRequest;
import com.skolarli.lmsservice.models.dto.exam.answerbook.NewAnswerTrueFalseRequest;
import com.skolarli.lmsservice.repository.exam.AnswerMcqRepository;
import com.skolarli.lmsservice.repository.exam.AnswerSubjectiveRepository;
import com.skolarli.lmsservice.repository.exam.AnswerTrueFalseRepository;
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
    AnswerMcqRepository answerMcqRepository;
    AnswerSubjectiveRepository answerSubjectiveRepository;
    AnswerTrueFalseRepository answerTrueFalseRepository;

    public AnswerBookAnswerServiceImpl(ExamQuestionSubjectiveService examQuestionSubjectiveService,
                                       ExamQuestionTrueOrFalseService examQuestionTrueOrFalseService,
                                       ExamQuestionMcqService examQuestionMcqService,
                                       AnswerMcqRepository answerMcqRepository,
                                       AnswerSubjectiveRepository answerSubjectiveRepository,
                                       AnswerTrueFalseRepository answerTrueFalseRepository) {
        this.examQuestionSubjectiveService = examQuestionSubjectiveService;
        this.examQuestionTrueOrFalseService = examQuestionTrueOrFalseService;
        this.examQuestionMcqService = examQuestionMcqService;
        this.answerMcqRepository = answerMcqRepository;
        this.answerSubjectiveRepository = answerSubjectiveRepository;
        this.answerTrueFalseRepository = answerTrueFalseRepository;
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
    public AnswerMcq getAnswerMcqByQuestionId(Long answerBookId, Long questionId) {
        List<AnswerMcq> answerMcqs = answerMcqRepository.findAllByAnswerBook_IdAndQuestion_Id(answerBookId,
            questionId);
        if (answerMcqs == null || answerMcqs.isEmpty()) {
            throw new ResourceNotFoundException("Answer not found");
        } else {
            return answerMcqs.get(0);
        }
    }

    @Override
    public AnswerTrueFalse getAnswerTrueFalseByQuestionId(Long answerBookId, Long questionId) {
        List<AnswerTrueFalse> answerTrueFalses = answerTrueFalseRepository.findAllByAnswerBook_IdAndQuestion_Id(
            answerBookId, questionId);
        if (answerTrueFalses == null || answerTrueFalses.isEmpty()) {
            throw new ResourceNotFoundException("Answer not found");
        } else {
            return answerTrueFalses.get(0);
        }
    }

    @Override
    public AnswerSubjective getAnswerSubjectiveByQuestionId(Long answerBookId, Long questionId) {
        List<AnswerSubjective> answerSubjectives = answerSubjectiveRepository.findAllByAnswerBook_IdAndQuestion_Id(
            answerBookId, questionId);
        if (answerSubjectives == null || answerSubjectives.isEmpty()) {
            throw new ResourceNotFoundException("Answer not found");
        } else {
            return answerSubjectives.get(0);
        }
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

    @Override
    public void calculateMcqScores(AnswerBook answerBook) {
        List<AnswerMcq> mcqAnswers = answerBook.getMcqAnswers();
        if (mcqAnswers == null) {
            return;
        }
        int totalMarks = answerBook.getTotalMarks();
        double marksObtained = answerBook.getObtainedMarks();
        int corretAnswers = answerBook.getCorrectAnswers();
        int incorrectAnswers = answerBook.getIncorrectAnswers();
        int partiallyCorrectAnswers = answerBook.getPartiallyCorrectAnswers();


        for (AnswerMcq answerMcq : mcqAnswers) {
            totalMarks += answerMcq.getQuestion().getMarks();
            marksObtained += answerMcq.getMarksGiven();
            if (answerMcq.getEvaluationResult() == null) {
                continue;
            } else if (answerMcq.getEvaluationResult().equals(EvaluationResult.CORRECT)) {
                corretAnswers++;
            } else if (answerMcq.getEvaluationResult().equals(EvaluationResult.INCORRECT)) {
                incorrectAnswers++;
            } else if (answerMcq.getEvaluationResult().equals(EvaluationResult.PARTIALLY_CORRECT)) {
                partiallyCorrectAnswers++;
            }
        }
        answerBook.setTotalMarks(totalMarks);
        answerBook.setObtainedMarks(marksObtained);
        answerBook.setAttemptedQuestions(mcqAnswers.size());
        answerBook.setCorrectAnswers(corretAnswers);
        answerBook.setIncorrectAnswers(incorrectAnswers);
        answerBook.setPartiallyCorrectAnswers(partiallyCorrectAnswers);
    }

    @Override
    public void calculateSubjectiveScores(AnswerBook answerBook) {
        List<AnswerSubjective> subjectiveAnswers = answerBook.getSubjectiveAnswers();
        if (subjectiveAnswers == null) {
            return;
        }
        int totalMarks = answerBook.getTotalMarks();
        double marksObtained = answerBook.getObtainedMarks();
        int corretAnswers = answerBook.getCorrectAnswers();
        int incorrectAnswers = answerBook.getIncorrectAnswers();
        int partiallyCorrectAnswers = answerBook.getPartiallyCorrectAnswers();

        for (AnswerSubjective answerSubjective : subjectiveAnswers) {
            totalMarks += answerSubjective.getQuestion().getMarks();
            marksObtained += answerSubjective.getMarksGiven();
            if (answerSubjective.getEvaluationResult() == null) {
                continue;
            } else if (answerSubjective.getEvaluationResult().equals(EvaluationResult.CORRECT)) {
                corretAnswers++;
            } else if (answerSubjective.getEvaluationResult().equals(EvaluationResult.INCORRECT)) {
                incorrectAnswers++;
            } else if (answerSubjective.getEvaluationResult().equals(EvaluationResult.PARTIALLY_CORRECT)) {
                partiallyCorrectAnswers++;
            }
        }

        answerBook.setTotalMarks(totalMarks);
        answerBook.setObtainedMarks(marksObtained);
        answerBook.setAttemptedQuestions(subjectiveAnswers.size());
        answerBook.setCorrectAnswers(corretAnswers);
        answerBook.setIncorrectAnswers(incorrectAnswers);
        answerBook.setPartiallyCorrectAnswers(partiallyCorrectAnswers);
    }

    @Override
    public void calculateTrueOrFalseScores(AnswerBook answerBook) {
        List<AnswerTrueFalse> trueFalseAnswers = answerBook.getTrueFalseAnswers();
        if (trueFalseAnswers == null) {
            return;
        }
        int totalMarks = answerBook.getTotalMarks();
        double marksObtained = answerBook.getObtainedMarks();
        int corretAnswers = answerBook.getCorrectAnswers();
        int incorrectAnswers = answerBook.getIncorrectAnswers();
        int partiallyCorrectAnswers = answerBook.getPartiallyCorrectAnswers();

        for (AnswerTrueFalse answerTrueFalse : trueFalseAnswers) {
            totalMarks += answerTrueFalse.getQuestion().getMarks();
            marksObtained += answerTrueFalse.getMarksGiven();
            if (answerTrueFalse.getEvaluationResult() == null) {
                continue;
            } else if (answerTrueFalse.getEvaluationResult().equals(EvaluationResult.CORRECT)) {
                corretAnswers++;
            } else if (answerTrueFalse.getEvaluationResult().equals(EvaluationResult.INCORRECT)) {
                incorrectAnswers++;
            } else if (answerTrueFalse.getEvaluationResult().equals(EvaluationResult.PARTIALLY_CORRECT)) {
                partiallyCorrectAnswers++;
            }
        }

        answerBook.setTotalMarks(totalMarks);
        answerBook.setObtainedMarks(marksObtained);
        answerBook.setAttemptedQuestions(trueFalseAnswers.size());
        answerBook.setCorrectAnswers(corretAnswers);
        answerBook.setIncorrectAnswers(incorrectAnswers);
        answerBook.setPartiallyCorrectAnswers(partiallyCorrectAnswers);
    }
}
