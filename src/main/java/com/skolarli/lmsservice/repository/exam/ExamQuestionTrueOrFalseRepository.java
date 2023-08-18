package com.skolarli.lmsservice.repository.exam;

import com.skolarli.lmsservice.models.db.exam.ExamQuestionTrueOrFalse;
import com.skolarli.lmsservice.repository.core.TenantableRepository;
import org.springframework.data.jpa.repository.Query;

public interface ExamQuestionTrueOrFalseRepository extends TenantableRepository<ExamQuestionTrueOrFalse> {
    @Query(value = "SELECT COALESCE(MAX(question_sort_order),0) "
            + "FROM lms.examquestions_tf where exam_id = ?1", nativeQuery = true)
    int findMaxQuestionSortOrder(Long examId);
}
