package com.skolarli.lmsservice.services.assignment;

import com.skolarli.lmsservice.models.db.assignment.AssignmentAnswerBook;
import com.skolarli.lmsservice.models.db.assignment.AssignmentAnswerBookStatus;
import com.skolarli.lmsservice.models.dto.assignment.AssignmentAnswerEvaulationRequest;
import com.skolarli.lmsservice.models.dto.exam.answerbook.NewAnswerBookRequest;

import java.util.List;

public interface AssignmentAnswerBookService {

    AssignmentAnswerBook toAnswerBook(NewAnswerBookRequest newAnswerBookRequest);

    AssignmentAnswerBook getAnswerBookById(Long id);

    List<AssignmentAnswerBook> getAllByAssignmentId(Long assignmentId);

    List<AssignmentAnswerBook> getAllByStudentId(Long studentId);

    List<AssignmentAnswerBook> getAllByAssignmentIdAndStudentId(Long assignmentId, Long studentId);

    List<AssignmentAnswerBook> queryAnswerBooks(Long assignmentId, Long studentId,
                                                Long courseId, Long batchId, Long scheduleId);


    AssignmentAnswerBook saveAnswerBook(AssignmentAnswerBook answerBook);

    AssignmentAnswerBook updateAnswerBook(AssignmentAnswerBook answerBook, Long answerBookId);

    AssignmentAnswerBook updateStatus(AssignmentAnswerBookStatus status, Long answerBookId);

    AssignmentAnswerBook addAnswers(NewAnswerBookRequest newAnswerBookRequest, Long answerBookId);

    AssignmentAnswerBook updateAnswers(NewAnswerBookRequest newAnswerBookRequest, Long answerBookId);

    AssignmentAnswerBook evaluateAnswers(List<AssignmentAnswerEvaulationRequest> request, Long answerBookId);

    AssignmentAnswerBook calculateFinalScores(Long answerBookId);

    AssignmentAnswerBook nullifyFields(List<String> fieldNames, Long answerBookId);

    void hardDeleteAnswerBook(Long id);

    void deleteAnswersFromAnswerBook(Long answerBookId, List<Long> answerIds);

}
