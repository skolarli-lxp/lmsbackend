package com.skolarli.lmsservice.repository.feedback;

import com.skolarli.lmsservice.models.db.feedback.Feedback;
import com.skolarli.lmsservice.models.db.feedback.FeedbackQuestion;
import com.skolarli.lmsservice.repository.core.TenantableRepository;

import java.util.List;

public interface FeedbackQuestionRepository extends TenantableRepository<FeedbackQuestion> {

}
