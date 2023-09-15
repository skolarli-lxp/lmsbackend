package com.skolarli.lmsservice.services.feedback;

import com.skolarli.lmsservice.models.FeedbackType;
import com.skolarli.lmsservice.models.db.feedback.FeedbackQuestion;
import com.skolarli.lmsservice.models.db.feedback.FeedbackQuestionnaire;
import com.skolarli.lmsservice.models.dto.feedback.NewFeedbackQuestionRequest;
import com.skolarli.lmsservice.models.dto.feedback.NewFeedbackQuestionnaireRequest;

import java.util.List;

public interface FeedbackQuestionnaireService {

    FeedbackQuestionnaire toFeedbackQuestionnaire(NewFeedbackQuestionnaireRequest feedback);

    FeedbackQuestion toFeedbackQuestion(NewFeedbackQuestionRequest question,
                                        FeedbackQuestionnaire feedbackQuestionnaire);

    FeedbackQuestion toFeedbackQuestion(NewFeedbackQuestionRequest question);

    FeedbackQuestionnaire getFeedbackQuestionnaireById(Long id);

    FeedbackQuestion getFeedbackQuestionById(Long id);


    List<FeedbackQuestionnaire> getAllFeedbackQuestionnaires();

    List<FeedbackQuestionnaire> getAllFeedbackQuestionnairesByFeedbackType(FeedbackType feedbackType);

    List<FeedbackQuestionnaire> getAllFeedbackQuestionnaireByFeedbackTypeAndTargetId(FeedbackType feedbackType,
                                                                                     Long targetId);

    FeedbackQuestionnaire createFeedbackQuestionnaire(FeedbackQuestionnaire feedbackQuestionnaire);

    FeedbackQuestionnaire updateFeedbackQuestionnaire(FeedbackQuestionnaire feedbackQuestionnaire, Long id);

    FeedbackQuestionnaire addQuestionToFeedbackQuestionnaire(Long feedbackId, FeedbackQuestion question);

    FeedbackQuestionnaire addQuestionsToFeedbackQuestionnaire(Long feedbackId, List<FeedbackQuestion> questions);

    FeedbackQuestionnaire updateQuestionInFeedbackQuestionnaire(Long feedbackId, FeedbackQuestion question);

    FeedbackQuestionnaire removeQuestionFromFeedbackQuestionnaire(Long feedbackId, Long questionId);

    void deleteFeedbackQuestionnaire(Long id);
}
