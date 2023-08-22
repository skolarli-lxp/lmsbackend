package com.skolarli.lmsservice.services.exam;

import com.skolarli.lmsservice.models.AnswerBookStatus;
import com.skolarli.lmsservice.models.db.exam.AnswerBook;
import com.skolarli.lmsservice.models.db.exam.AnswerMcq;
import com.skolarli.lmsservice.models.db.exam.AnswerSubjective;
import com.skolarli.lmsservice.models.db.exam.AnswerTrueFalse;
import com.skolarli.lmsservice.models.dto.exam.*;

import java.util.List;

public interface AnswerBookService {

    AnswerBook toAnswerBook(NewAnswerBookRequest newAnswerBookRequest);

    List<AnswerBook> getAllByExamId(Long examId);

    List<AnswerBook> getAllByStudentId(Long studentId);

    List<AnswerBook> getAllByExamIdAndStudentId(Long examId, Long studentId);

    AnswerBook getAnswerBookById(Long id);

    List<AnswerBook> getAllAnswerBooks();

    AnswerBook saveAnswerBook(AnswerBook answerBook);

    AnswerBook updateAnswerBook(AnswerBook answerBook, Long id);

    AnswerBook updateStatus(AnswerBookStatus status, Long id);

    NewAnswerResponse addAnswerToAnswerBook(NewAnswerMcqRequest newAnswerMcqRequest, Long id);

    NewAnswerResponse addAnswerToAnswerBook(NewAnswerSubjectiveRequest newAnswerMcqRequest, Long id);

    NewAnswerResponse addAnswerToAnswerBook(NewAnswerTrueFalseRequest newAnswerMcqRequest, Long id);

    void addMcqAnswers(List<AnswerMcq> answerMcqs, AnswerBook answerBook);

    void addTrueFalseAnswers(List<AnswerTrueFalse> answerTrueFalses, AnswerBook answerBook);

    void addSubjectiveAnswers(List<AnswerSubjective> answerSubjectives, AnswerBook answerBook);

    AnswerBook addAnswers(AddAnswerBookAnswerRequest addAnswerBookAnswerRequest, Long id);

    void evaluateAnswerBook(Long answerBookId, AnswerBookEvaulationRequest request);

    NewAnswerResponse updateAnswerBookAnswer(UpdateAnswerMcqRequest updateAnswerMcqRequest, Long answerBookId,
                                             Long answerId);

    NewAnswerResponse updateAnswerBookAnswer(UpdateAnswerSubjectiveRequest updateAnswerMcqRequest, Long answerBookId,
                                             Long answerId);

    NewAnswerResponse updateAnswerBookAnswer(UpdateAnswerTrueFalseRequest updateAnswerMcqRequest, Long answerBookId,
                                             Long answerId);

    void deleteAnswerBook(Long id);
}