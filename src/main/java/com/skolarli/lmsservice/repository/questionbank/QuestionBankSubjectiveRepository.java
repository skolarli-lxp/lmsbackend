package com.skolarli.lmsservice.repository.questionbank;

import com.skolarli.lmsservice.models.db.questionbank.BankQuestionSubjective;
import com.skolarli.lmsservice.repository.core.TenantableRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface QuestionBankSubjectiveRepository extends
    TenantableRepository<BankQuestionSubjective> {

    @Query("SELECT q FROM BankQuestionSubjective q "
        + "WHERE (:courseId IS NULL OR :courseId = 0.0 OR q.course.id = :courseId) "
        + "AND (:batchId IS NULL OR :batchId = 0.0 OR q.batch.id = :batchId) "
        + "AND (:lessonId IS NULL OR :lessonId = 0.0 OR q.lesson.id = :lessonId) "
        + "AND (:chapterId IS NULL OR :chapterId = 0.0 OR q.chapter.id = :chapterId)"
        + "AND (:studentId IS NULL OR :studentId = 0.0 OR q.student.id = :studentId)")
    List<BankQuestionSubjective> findQuestionsByParameters(Double courseId, Double batchId,
                                                           Double lessonId, Double chapterId,
                                                           Double studentId);
}
