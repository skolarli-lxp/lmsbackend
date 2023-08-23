package com.skolarli.lmsservice.repository.exam;

import com.skolarli.lmsservice.models.db.exam.AnswerBook;
import com.skolarli.lmsservice.models.db.exam.AnswerMcq;
import com.skolarli.lmsservice.repository.core.TenantableRepository;

import java.util.List;

public interface AnswerMcqRepository extends TenantableRepository<AnswerMcq> {
    List<AnswerMcq> findAllByAnswerBook_IdAndQuestion_Id(Long answerBookId, Long questionId);
}
