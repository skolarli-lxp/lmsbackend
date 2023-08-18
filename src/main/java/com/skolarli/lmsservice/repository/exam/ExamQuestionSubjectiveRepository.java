package com.skolarli.lmsservice.repository.exam;

import com.skolarli.lmsservice.models.db.exam.ExamQuestionSubjective;
import com.skolarli.lmsservice.repository.core.TenantableRepository;
import org.springframework.data.jpa.repository.Query;

public interface ExamQuestionSubjectiveRepository extends TenantableRepository<ExamQuestionSubjective> {
    @Query(value = "SELECT COALESCE(MAX(question_sort_order),0) "
            + "FROM lms.examquestions_subjective where exam_id = ?1", nativeQuery = true)
    int findMaxQuestionSortOrder(Long examId);
}
