package com.skolarli.lmsservice.repository.feedback;

import com.skolarli.lmsservice.models.FeedbackType;
import com.skolarli.lmsservice.models.db.feedback.Feedback;
import com.skolarli.lmsservice.models.db.feedback.FeedbackQuestionnaire;
import com.skolarli.lmsservice.repository.core.TenantableRepository;

import java.util.List;

public interface FeedbackRepository extends TenantableRepository<Feedback> {

    List<Feedback> findAllByFeedbackQuestionnaire_Id(Long feedbackQuestionnaireId);

    List<Feedback> findAllByFeedbackQuestionnaire_IdAndCreatedBy_Id(Long feedbackQuestionnaireId, Long createdByUserId);

    List<Feedback> findAllByFeedbackQuestion_Id(Long feedbackQuestionId);

    List<Feedback> findAllByFeedbackQuestion_IdAndCreatedBy_Id(Long feedbackQuestionId, Long createdByUserId);

    List<Feedback> findAllByCreatedBy_Id(Long createdByUserId);
}
