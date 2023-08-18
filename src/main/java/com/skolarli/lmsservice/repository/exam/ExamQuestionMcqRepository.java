package com.skolarli.lmsservice.repository.exam;

import com.skolarli.lmsservice.models.db.exam.ExamQuestionMcq;
import com.skolarli.lmsservice.repository.core.TenantableRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ExamQuestionMcqRepository extends TenantableRepository<ExamQuestionMcq> {
    @Query(value = "SELECT COALESCE(MAX(question_sort_order),0) "
            + "FROM lms.examquestions_mcq where exam_id = ?1", nativeQuery = true)
    int findMaxQuestionSortOrder(Long examId);

    List<ExamQuestionMcq> findByExamIdOrderByQuestionSortOrderAsc(Long examId);
}
