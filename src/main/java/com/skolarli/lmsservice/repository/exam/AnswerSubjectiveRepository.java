package com.skolarli.lmsservice.repository.exam;

import com.skolarli.lmsservice.models.db.exam.AnswerSubjective;
import com.skolarli.lmsservice.repository.core.TenantableRepository;

import java.util.List;

public interface AnswerSubjectiveRepository extends TenantableRepository<AnswerSubjective> {
    List<AnswerSubjective> findAllByAnswerBook_IdAndQuestion_Id(Long answerBookId, Long questionId);
}
