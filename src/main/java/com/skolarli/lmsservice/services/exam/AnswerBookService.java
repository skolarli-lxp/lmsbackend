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

    List<AnswerBook> findAllByExam_Id(Long examId);

    List<AnswerBook> findAllByExam_IdAndStudent_Id(Long examId, Long studentId);

    AnswerBook getAnswerBookById(Long id);

    AnswerBook saveAnswerBook(AnswerBook answerBook);

    AnswerBook saveAnswerBook(NewAnswerBookRequest newAnswerBookRequest);

    AnswerBook updateAnswerBook(AnswerBook answerBook, Long id);

    AnswerBook addMcqAnswers(AnswerBook answerBook, List<AnswerMcq> answerMcqs);

    AnswerBook addTrueFalseAnswers(AnswerBook answerBook, List<AnswerTrueFalse> answerTrueFalses);

    AnswerBook addSubjectiveAnswers(AnswerBook answerBook,
                                    List<AnswerSubjective> answerSubjectives);

    AnswerBook addAnswers(AddAnswerBookAnswerRequest addAnswerBookAnswerRequest, Long id);

    void deleteAnswerBook(Long id);

}