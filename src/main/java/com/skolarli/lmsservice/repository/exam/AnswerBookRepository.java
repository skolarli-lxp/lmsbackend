package com.skolarli.lmsservice.repository.exam;

import com.skolarli.lmsservice.models.db.exam.AnswerBook;
import com.skolarli.lmsservice.repository.core.TenantableRepository;

import java.util.List;

public interface AnswerBookRepository extends TenantableRepository<AnswerBook> {
    List<AnswerBook> findAllByExam_Id(Long examId);

    List<AnswerBook> findAllByStudent_Id(Long studentId);

    List<AnswerBook> findAllByExam_IdAndStudent_Id(Long examId, Long studentId);
}
