package com.skolarli.lmsservice.services.exam;

import com.skolarli.lmsservice.models.db.exam.AnswerBook;
import com.skolarli.lmsservice.models.db.exam.AnswerMcq;
import com.skolarli.lmsservice.models.db.exam.AnswerSubjective;
import com.skolarli.lmsservice.models.db.exam.AnswerTrueFalse;
import com.skolarli.lmsservice.models.dto.exam.answerbook.AnswerEvaulationRequest;
import com.skolarli.lmsservice.models.dto.exam.answerbook.NewAnswerMcqRequest;
import com.skolarli.lmsservice.models.dto.exam.answerbook.NewAnswerSubjectiveRequest;
import com.skolarli.lmsservice.models.dto.exam.answerbook.NewAnswerTrueFalseRequest;

import java.util.List;

public interface AnswerBookAnswerService {
    AnswerMcq toAnswerMcq(NewAnswerMcqRequest newAnswerMcqRequest, AnswerBook answerBook);

    AnswerMcq getAnswerMcqByQuestionId(Long answerBookId, Long questionId);

    AnswerTrueFalse getAnswerTrueFalseByQuestionId(Long answerBookId, Long questionId);

    AnswerSubjective getAnswerSubjectiveByQuestionId(Long answerBookId, Long questionId);

    AnswerSubjective toAnswerSubjective(NewAnswerSubjectiveRequest newAnswerSubjectiveRequest, AnswerBook answerBook);

    void manualEvaluateTrueFalseAnswers(AnswerBook answerBook,
                                        List<AnswerEvaulationRequest> trueFalseEvaluationRequests);

    void manualEvaluateSubjectiveAnswers(AnswerBook answerBook,
                                         List<AnswerEvaulationRequest> subjectiveEvaluationRequests);

    void manualEvaluateMcqAnswers(AnswerBook answerBook, List<AnswerEvaulationRequest> mcqEvaluationRequests);

    AnswerTrueFalse toAnswerTrueFalse(NewAnswerTrueFalseRequest newAnswerTrueFalseRequest, AnswerBook answerBook);

    void calculateMcqScores(AnswerBook answerBook);

    void calculateSubjectiveScores(AnswerBook answerBook);

    void calculateTrueOrFalseScores(AnswerBook answerBook);

}