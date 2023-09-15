package com.skolarli.lmsservice.repository.feedback;

import com.skolarli.lmsservice.models.FeedbackType;
import com.skolarli.lmsservice.models.db.feedback.FeedbackQuestionnaire;
import com.skolarli.lmsservice.repository.core.TenantableRepository;

import java.util.List;

public interface FeedbackQuestionnaireRepository extends TenantableRepository<FeedbackQuestionnaire> {

    List<FeedbackQuestionnaire> findAllByBatch_IdAndFeedbackType(Long batchId, FeedbackType feedbackType);

    List<FeedbackQuestionnaire> findAllByBatchSchedule_IdAndFeedbackType(Long batchScheduleId,
                                                                         FeedbackType feedbackType);

    List<FeedbackQuestionnaire> findAllByStudent_IdAndFeedbackType(Long studentId, FeedbackType feedbackType);

    List<FeedbackQuestionnaire> findAllByTrainer_IdAndFeedbackType(Long trainerId, FeedbackType feedbackType);

    List<FeedbackQuestionnaire> findAllByFeedbackType(FeedbackType feedbackType);
}
