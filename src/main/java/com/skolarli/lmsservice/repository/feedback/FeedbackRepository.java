package com.skolarli.lmsservice.repository.feedback;

import com.skolarli.lmsservice.models.FeedbackType;
import com.skolarli.lmsservice.models.db.feedback.Feedback;
import com.skolarli.lmsservice.repository.core.TenantableRepository;

import java.util.List;

public interface FeedbackRepository extends TenantableRepository<Feedback> {

    List<Feedback> findAllByBatch_IdAndFeedbackType(Long batchId, FeedbackType feedbackType);

    List<Feedback> findAllByBatchSchedule_IdAndFeedbackType(Long batchScheduleId, FeedbackType feedbackType);

    List<Feedback> findAllByStudent_IdAndFeedbackType(Long studentId, FeedbackType feedbackType);

    List<Feedback> findAllByTrainer_IdAndFeedbackType(Long trainerId, FeedbackType feedbackType);

    List<Feedback> findAllByGivenBy_Id(Long givenByUserId);
}
