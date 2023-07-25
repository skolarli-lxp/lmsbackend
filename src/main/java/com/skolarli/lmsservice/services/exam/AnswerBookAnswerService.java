package com.skolarli.lmsservice.services.exam;

import com.skolarli.lmsservice.models.db.exam.AnswerBook;
import com.skolarli.lmsservice.models.db.exam.AnswerMcq;
import com.skolarli.lmsservice.models.db.exam.AnswerSubjective;
import com.skolarli.lmsservice.models.db.exam.AnswerTrueFalse;
import com.skolarli.lmsservice.models.dto.exam.NewAnswerMcqRequest;
import com.skolarli.lmsservice.models.dto.exam.NewAnswerSubjectiveRequest;
import com.skolarli.lmsservice.models.dto.exam.NewAnswerTrueFalseRequest;

public interface AnswerBookAnswerService {
    AnswerMcq toAnswerMcq(NewAnswerMcqRequest newAnswerMcqRequest, AnswerBook answerBook);

    AnswerSubjective toAnswerSubjective(NewAnswerSubjectiveRequest newAnswerSubjectiveRequest,
                                        AnswerBook answerBook);

    AnswerTrueFalse toAnswerTrueFalse(NewAnswerTrueFalseRequest newAnswerTrueFalseRequest,
                                      AnswerBook answerBook);

}