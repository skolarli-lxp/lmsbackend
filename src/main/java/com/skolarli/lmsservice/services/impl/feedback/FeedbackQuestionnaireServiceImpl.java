package com.skolarli.lmsservice.services.impl.feedback;

import com.skolarli.lmsservice.exception.ResourceNotFoundException;
import com.skolarli.lmsservice.models.FeedbackType;
import com.skolarli.lmsservice.models.db.feedback.FeedbackQuestion;
import com.skolarli.lmsservice.models.db.feedback.FeedbackQuestionnaire;
import com.skolarli.lmsservice.models.dto.feedback.NewFeedbackQuestionRequest;
import com.skolarli.lmsservice.models.dto.feedback.NewFeedbackQuestionnaireRequest;
import com.skolarli.lmsservice.repository.feedback.FeedbackQuestionRepository;
import com.skolarli.lmsservice.repository.feedback.FeedbackQuestionnaireRepository;
import com.skolarli.lmsservice.services.core.LmsUserService;
import com.skolarli.lmsservice.services.course.BatchScheduleService;
import com.skolarli.lmsservice.services.course.BatchService;
import com.skolarli.lmsservice.services.course.CourseService;
import com.skolarli.lmsservice.services.feedback.FeedbackQuestionnaireService;
import com.skolarli.lmsservice.utils.UserUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FeedbackQuestionnaireServiceImpl implements FeedbackQuestionnaireService {

    FeedbackQuestionnaireRepository feedbackQuestionnaireRepository;

    FeedbackQuestionRepository  feedbackQuestionRepository;

    CourseService courseService;

    BatchService batchService;

    BatchScheduleService batchScheduleService;

    LmsUserService lmsUserService;
    UserUtils userUtils;

    public FeedbackQuestionnaireServiceImpl(FeedbackQuestionnaireRepository feedbackQuestionnaireRepository,
                                            CourseService courseService,
                                            UserUtils userUtils, BatchService batchService,
                                            BatchScheduleService batchScheduleService,
                                            FeedbackQuestionRepository feedbackQuestionRepository,
                                            LmsUserService lmsUserService) {
        this.feedbackQuestionnaireRepository = feedbackQuestionnaireRepository;
        this.feedbackQuestionRepository = feedbackQuestionRepository;
        this.userUtils = userUtils;
        this.courseService = courseService;
        this.batchService = batchService;
        this.batchScheduleService = batchScheduleService;
        this.lmsUserService = lmsUserService;
    }

    @Override
    public FeedbackQuestionnaire toFeedbackQuestionnaire(NewFeedbackQuestionnaireRequest feedbackQuestionnaireRequest) {
        FeedbackQuestionnaire feedbackQuestionnaire = new FeedbackQuestionnaire();
        feedbackQuestionnaire.setFeedbackType(feedbackQuestionnaireRequest.getFeedbackType());
        feedbackQuestionnaire.setFeedbackQuestionnaireName(feedbackQuestionnaireRequest.getFeedbackQuestionnaireName());
        feedbackQuestionnaire.setFeedbackFrom(feedbackQuestionnaireRequest.getFeedbackFrom());
        if (feedbackQuestionnaireRequest.getCourseId() != null) {
            feedbackQuestionnaire.setCourse(courseService.getCourseById(feedbackQuestionnaireRequest.getCourseId()));
        }
        if (feedbackQuestionnaireRequest.getBatchId() != null) {
            feedbackQuestionnaire.setBatch(batchService.getBatch(feedbackQuestionnaireRequest.getBatchId()));
        }
        if (feedbackQuestionnaireRequest.getBatchScheduleId() != null) {
            feedbackQuestionnaire.setBatchSchedule(batchScheduleService.getBatchSchedule(
                feedbackQuestionnaireRequest.getBatchScheduleId()));
        }
        if (feedbackQuestionnaireRequest.getStudentId() != null) {
            feedbackQuestionnaire.setStudent(lmsUserService.getLmsUserById(
                feedbackQuestionnaireRequest.getStudentId()));
        }
        if (feedbackQuestionnaireRequest.getTrainerId() != null) {
            feedbackQuestionnaire.setTrainer(lmsUserService.getLmsUserById(feedbackQuestionnaireRequest
                .getTrainerId()));
        }
        if (feedbackQuestionnaireRequest.getQuestions() != null && feedbackQuestionnaireRequest
            .getQuestions().size() > 0) {
            feedbackQuestionnaire.setQuestions(
                feedbackQuestionnaireRequest.getQuestions().stream().map(question ->
                        toFeedbackQuestion(question, feedbackQuestionnaire)).collect(Collectors.toList())
            );
        }
        return feedbackQuestionnaire;
    }

    @Override
    public FeedbackQuestion toFeedbackQuestion(NewFeedbackQuestionRequest question,
                                               FeedbackQuestionnaire feedbackQuestionnaire) {
        FeedbackQuestion feedbackQuestion = toFeedbackQuestion(question);
        feedbackQuestion.setFeedbackQuestionnaire(feedbackQuestionnaire);
        return feedbackQuestion;
    }

    @Override
    public FeedbackQuestion toFeedbackQuestion(NewFeedbackQuestionRequest question) {
        FeedbackQuestion feedbackQuestion = new FeedbackQuestion();
        feedbackQuestion.setQuestionText(question.getQuestionText());
        return feedbackQuestion;
    }

    @Override
    public FeedbackQuestionnaire getFeedbackQuestionnaireById(Long id) {
        List<FeedbackQuestionnaire> feedbackQuestionnaires =
            feedbackQuestionnaireRepository.findAllById(new ArrayList<>(List.of(id)));
        if (feedbackQuestionnaires.size() == 0) {
            throw new ResourceNotFoundException("Feedback with Id " + id + " not found");
        }
        return feedbackQuestionnaires.get(0);
    }

    @Override
    public FeedbackQuestion getFeedbackQuestionById(Long id) {
        List<FeedbackQuestion> feedbackQuestions =
            feedbackQuestionRepository.findAllById(new ArrayList<>(List.of(id)));
        if (feedbackQuestions.size() == 0) {
            throw new ResourceNotFoundException("Feedback with Id " + id + " not found");
        }
        return feedbackQuestions.get(0);
    }

    @Override
    public List<FeedbackQuestionnaire> getAllFeedbackQuestionnaires() {
        return feedbackQuestionnaireRepository.findAll();
    }

    @Override
    public List<FeedbackQuestionnaire> getAllFeedbackQuestionnairesByFeedbackType(FeedbackType feedbackType) {
        return feedbackQuestionnaireRepository.findAllByFeedbackType(feedbackType);
    }

    @Override
    public List<FeedbackQuestionnaire> getAllFeedbackQuestionnaireByFeedbackTypeAndTargetId(FeedbackType feedbackType,
                                                                                            Long targetId) {
        if (feedbackType == FeedbackType.STUDENT) {
            return feedbackQuestionnaireRepository.findAllByStudent_IdAndFeedbackType(targetId, feedbackType);
        } else if (feedbackType == FeedbackType.TRAINER) {
            return feedbackQuestionnaireRepository.findAllByTrainer_IdAndFeedbackType(targetId, feedbackType);
        } else if (feedbackType == FeedbackType.BATCH) {
            return feedbackQuestionnaireRepository.findAllByBatch_IdAndFeedbackType(targetId, feedbackType);
        } else if (feedbackType == FeedbackType.BATCH_SESSION) {
            return feedbackQuestionnaireRepository.findAllByBatchSchedule_IdAndFeedbackType(targetId, feedbackType);
        } else {
            throw new ResourceNotFoundException("Feedback with Id " + targetId + " not found");
        }
    }

    @Override
    public FeedbackQuestionnaire createFeedbackQuestionnaire(FeedbackQuestionnaire feedbackQuestionnaire) {
        feedbackQuestionnaire.setCreatedBy(userUtils.getCurrentUser());
        return feedbackQuestionnaireRepository.save(feedbackQuestionnaire);
    }

    @Override
    public FeedbackQuestionnaire updateFeedbackQuestionnaire(FeedbackQuestionnaire feedbackQuestionnaire, Long id) {
        FeedbackQuestionnaire existingFeedbackQuestionnaire = getFeedbackQuestionnaireById(id);
        existingFeedbackQuestionnaire.update(feedbackQuestionnaire);
        existingFeedbackQuestionnaire.setUpdatedBy(userUtils.getCurrentUser());
        return feedbackQuestionnaireRepository.save(existingFeedbackQuestionnaire);
    }

    @Override
    public FeedbackQuestionnaire addQuestionToFeedbackQuestionnaire(Long feedbackId, FeedbackQuestion question) {
        FeedbackQuestionnaire existingFeedbackQuestionnaire = getFeedbackQuestionnaireById(feedbackId);
        question.setFeedbackQuestionnaire(existingFeedbackQuestionnaire);
        if (existingFeedbackQuestionnaire.getQuestions() == null) {
            existingFeedbackQuestionnaire.setQuestions(new ArrayList<>());
        }
        existingFeedbackQuestionnaire.getQuestions().add(question);
        existingFeedbackQuestionnaire.setUpdatedBy(userUtils.getCurrentUser());
        return feedbackQuestionnaireRepository.save(existingFeedbackQuestionnaire);
    }

    @Override
    public FeedbackQuestionnaire addQuestionsToFeedbackQuestionnaire(Long feedbackId,
                                                                     List<FeedbackQuestion> questions) {
        FeedbackQuestionnaire existingFeedbackQuestionnaire = getFeedbackQuestionnaireById(feedbackId);
        if (questions == null || questions.size() == 0) {
            return existingFeedbackQuestionnaire;
        }
        questions.forEach(question -> question.setFeedbackQuestionnaire(existingFeedbackQuestionnaire));
        if (existingFeedbackQuestionnaire.getQuestions() == null) {
            existingFeedbackQuestionnaire.setQuestions(new ArrayList<>());
        }
        existingFeedbackQuestionnaire.getQuestions().addAll(questions);
        existingFeedbackQuestionnaire.setUpdatedBy(userUtils.getCurrentUser());
        return feedbackQuestionnaireRepository.save(existingFeedbackQuestionnaire);
    }

    @Override
    public FeedbackQuestionnaire updateQuestionInFeedbackQuestionnaire(Long feedbackId, FeedbackQuestion question) {
        FeedbackQuestionnaire existingFeedbackQuestionnaire = getFeedbackQuestionnaireById(feedbackId);
        if (existingFeedbackQuestionnaire.getQuestions() == null) {
            throw new ResourceNotFoundException("Question with Id " + question.getId() + " not found");
        }
        FeedbackQuestion existingQuestion = existingFeedbackQuestionnaire.getQuestions().stream()
            .filter(q -> q.getId() == question.getId()).findFirst().orElse(null);
        if (existingQuestion == null) {
            throw new ResourceNotFoundException("Question with Id " + question.getId() + " not found");
        }
        existingQuestion.update(question);
        existingFeedbackQuestionnaire.setUpdatedBy(userUtils.getCurrentUser());
        return feedbackQuestionnaireRepository.save(existingFeedbackQuestionnaire);
    }

    @Override
    public FeedbackQuestionnaire removeQuestionFromFeedbackQuestionnaire(Long feedbackId, Long questionId) {
        FeedbackQuestionnaire existingFeedbackQuestionnaire = getFeedbackQuestionnaireById(feedbackId);
        if (existingFeedbackQuestionnaire.getQuestions() == null) {
            throw new ResourceNotFoundException("Question with Id " + questionId + " not found");
        }
        FeedbackQuestion existingQuestion = existingFeedbackQuestionnaire.getQuestions().stream()
            .filter(q -> q.getId() == questionId).findFirst().orElse(null);
        if (existingQuestion == null) {
            throw new ResourceNotFoundException("Question with Id " + questionId + " not found");
        }
        existingFeedbackQuestionnaire.getQuestions().remove(existingQuestion);
        existingFeedbackQuestionnaire.setUpdatedBy(userUtils.getCurrentUser());
        return feedbackQuestionnaireRepository.save(existingFeedbackQuestionnaire);
    }

    @Override
    public void deleteFeedbackQuestionnaire(Long id) {
        FeedbackQuestionnaire existingFeedbackQuestionnaire = getFeedbackQuestionnaireById(id);
        feedbackQuestionnaireRepository.delete(existingFeedbackQuestionnaire);
    }
}