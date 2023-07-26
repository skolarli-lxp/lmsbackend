package com.skolarli.lmsservice.services.exam;

import com.skolarli.lmsservice.models.db.exam.AnswerBook;
import com.skolarli.lmsservice.models.db.exam.AnswerMcq;
import com.skolarli.lmsservice.models.db.exam.AnswerSubjective;
import com.skolarli.lmsservice.models.db.exam.AnswerTrueFalse;
import com.skolarli.lmsservice.models.dto.exam.AddAnswerBookAnswerRequest;
import com.skolarli.lmsservice.models.dto.exam.NewAnswerBookRequest;

import java.util.List;

public interface AnswerBookService {

    AnswerBook toAnswerBook(NewAnswerBookRequest newAnswerBookRequest);

    List<AnswerBook> findAllByExamId(Long examId);

    List<AnswerBook> findAllByExamIdAndStudentId(Long examId, Long studentId);

    AnswerBook getAnswerBookById(Long id);

    List<AnswerBook> findAllAnswerBooks();

    AnswerBook saveAnswerBook(AnswerBook answerBook);

    AnswerBook updateAnswerBook(AnswerBook answerBook, Long id);

    void addMcqAnswers(List<AnswerMcq> answerMcqs, AnswerBook answerBook);

    void addTrueFalseAnswers(List<AnswerTrueFalse> answerTrueFalses, AnswerBook answerBook);

    void addSubjectiveAnswers(List<AnswerSubjective> answerSubjectives, AnswerBook answerBook);

    AnswerBook addAnswers(AddAnswerBookAnswerRequest addAnswerBookAnswerRequest, Long id);

    void deleteAnswerBook(Long id);

}