package com.skolarli.lmsservice.repository.exam;

import com.skolarli.lmsservice.models.db.exam.AnswerTrueFalse;
import com.skolarli.lmsservice.repository.core.TenantableRepository;

import java.util.List;

public interface AnswerTrueFalseRepository extends TenantableRepository<AnswerTrueFalse> {
    List<AnswerTrueFalse> findAllByAnswerBook_IdAndQuestion_Id(Long answerBookId, Long questionId);
}
