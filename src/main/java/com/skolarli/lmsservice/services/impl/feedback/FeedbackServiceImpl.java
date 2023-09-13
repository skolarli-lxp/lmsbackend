package com.skolarli.lmsservice.services.impl.feedback;

import com.skolarli.lmsservice.exception.ResourceNotFoundException;
import com.skolarli.lmsservice.models.FeedbackType;
import com.skolarli.lmsservice.models.db.feedback.Feedback;
import com.skolarli.lmsservice.models.db.feedback.FeedbackQuestion;
import com.skolarli.lmsservice.models.dto.feedback.NewFeedbackQuestionRequest;
import com.skolarli.lmsservice.models.dto.feedback.NewFeedbackRequest;
import com.skolarli.lmsservice.repository.feedback.FeedbackRepository;
import com.skolarli.lmsservice.services.core.LmsUserService;
import com.skolarli.lmsservice.services.course.BatchScheduleService;
import com.skolarli.lmsservice.services.course.BatchService;
import com.skolarli.lmsservice.services.feedback.FeedbackService;
import com.skolarli.lmsservice.utils.UserUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FeedbackServiceImpl implements FeedbackService {

    FeedbackRepository feedbackRepository;

    BatchService batchService;

    BatchScheduleService batchScheduleService;

    LmsUserService lmsUserService;
    UserUtils userUtils;

    public FeedbackServiceImpl(FeedbackRepository feedbackRepository, UserUtils userUtils,
                               BatchService batchService, BatchScheduleService batchScheduleService,
                               LmsUserService lmsUserService) {
        this.feedbackRepository = feedbackRepository;
        this.userUtils = userUtils;
        this.batchService = batchService;
        this.batchScheduleService = batchScheduleService;
        this.lmsUserService = lmsUserService;
    }

    @Override
    public Feedback toFeedback(NewFeedbackRequest feedbackRequest) {
        Feedback feedback = new Feedback();
        feedback.setFeedbackType(feedbackRequest.getFeedbackType());
        if (feedbackRequest.getBatchId() == null) {
            feedback.setBatch(batchService.getBatch(feedbackRequest.getBatchId()));
        }
        if (feedbackRequest.getBatchScheduleId() == null) {
            feedback.setBatchSchedule(batchScheduleService.getBatchSchedule(feedbackRequest.getBatchScheduleId()));
        }
        if (feedbackRequest.getStudentId() == null) {
            feedback.setStudent(lmsUserService.getLmsUserById(feedbackRequest.getStudentId()));
        }
        if (feedbackRequest.getTrainerId() == null) {
            feedback.setTrainer(lmsUserService.getLmsUserById(feedbackRequest.getTrainerId()));
        }
        feedback.setGivenBy(lmsUserService.getLmsUserById(feedbackRequest.getGivenByUserId()));
        feedback.setQuestions(
            feedbackRequest.getQuestions().stream().map(question -> toFeedbackQuestion(question, feedback))
                .collect(Collectors.toList())
        );
        return feedback;
    }

    @Override
    public FeedbackQuestion toFeedbackQuestion(NewFeedbackQuestionRequest question, Feedback feedback) {
        FeedbackQuestion feedbackQuestion = toFeedbackQuestion(question);
        feedbackQuestion.setFeedback(feedback);
        return feedbackQuestion;
    }

    @Override
    public FeedbackQuestion toFeedbackQuestion(NewFeedbackQuestionRequest question) {
        FeedbackQuestion feedbackQuestion = new FeedbackQuestion();
        feedbackQuestion.setQuestionText(question.getQuestionText());
        feedbackQuestion.setStarRating(question.getStarRating());
        feedbackQuestion.setTextRemark(question.getTextRemark());
        return feedbackQuestion;
    }

    @Override
    public Feedback getFeedbackById(Long id) {
        List<Feedback> feedbacks =
            feedbackRepository.findAllById(new ArrayList<>(List.of(id)));
        if (feedbacks.size() == 0) {
            throw new ResourceNotFoundException("Feedback with Id " + id + " not found");
        }
        return feedbacks.get(0);
    }

    @Override
    public List<Feedback> getAllFeedbacks() {
        return feedbackRepository.findAll();
    }

    @Override
    public List<Feedback> getFeedbacksByRelatedIdAndType(Long relatedId, FeedbackType type) {
        if (type == FeedbackType.BATCH) {
            return feedbackRepository.findAllByBatch_IdAndFeedbackType(relatedId, type);
        } else if (type == FeedbackType.BATCH_SESSION) {
            return feedbackRepository.findAllByBatchSchedule_IdAndFeedbackType(relatedId, type);
        } else if (type == FeedbackType.STUDENT) {
            return feedbackRepository.findAllByStudent_IdAndFeedbackType(relatedId, type);
        } else if (type == FeedbackType.TRAINER) {
            return feedbackRepository.findAllByTrainer_IdAndFeedbackType(relatedId, type);
        } else {
            throw new ResourceNotFoundException("Type " + type + " not supported");
        }
    }

    @Override
    public List<Feedback> getAllFeedbacksGivenByUser(Long userId) {
        return feedbackRepository.findAllByGivenBy_Id(userId);
    }

    @Override
    public Feedback createFeedback(Feedback feedback) {
        feedback.setCreatedBy(userUtils.getCurrentUser());
        return feedbackRepository.save(feedback);
    }

    @Override
    public Feedback updateFeedback(Feedback feedback) {
        Feedback existingFeedback = getFeedbackById(feedback.getId());
        existingFeedback.update(feedback);
        existingFeedback.setUpdatedBy(userUtils.getCurrentUser());
        return feedbackRepository.save(existingFeedback);
    }

    @Override
    public Feedback addQuestionToFeedback(Long feedbackId, FeedbackQuestion question) {
        Feedback existingFeedback = getFeedbackById(feedbackId);
        if (existingFeedback == null) {
            throw new ResourceNotFoundException("Feedback with Id " + feedbackId + " not found");
        }
        question.setFeedback(existingFeedback);
        existingFeedback.getQuestions().add(question);
        existingFeedback.setUpdatedBy(userUtils.getCurrentUser());
        return feedbackRepository.save(existingFeedback);
    }

    @Override
    public Feedback addQuestionsToFeedback(Long feedbackId, List<FeedbackQuestion> questions) {
        Feedback existingFeedback = getFeedbackById(feedbackId);
        if (existingFeedback == null) {
            throw new ResourceNotFoundException("Feedback with Id " + feedbackId + " not found");
        }
        questions.forEach(question -> question.setFeedback(existingFeedback));
        existingFeedback.getQuestions().addAll(questions);
        existingFeedback.setUpdatedBy(userUtils.getCurrentUser());
        return feedbackRepository.save(existingFeedback);
    }

    @Override
    public Feedback updateQuestionInFeedback(Long feedbackId, FeedbackQuestion question) {
        Feedback existingFeedback = getFeedbackById(feedbackId);
        if (existingFeedback == null) {
            throw new ResourceNotFoundException("Feedback with Id " + feedbackId + " not found");
        }
        FeedbackQuestion existingQuestion = existingFeedback.getQuestions().stream()
            .filter(q -> q.getId() == question.getId()).findFirst().orElse(null);
        if (existingQuestion == null) {
            throw new ResourceNotFoundException("Question with Id " + question.getId() + " not found");
        }
        existingQuestion.update(question);
        existingFeedback.setUpdatedBy(userUtils.getCurrentUser());
        return feedbackRepository.save(existingFeedback);
    }

    @Override
    public Feedback removeQuestionFromFeedback(Long feedbackId, Long questionId) {
        Feedback existingFeedback = getFeedbackById(feedbackId);
        if (existingFeedback == null) {
            throw new ResourceNotFoundException("Feedback with Id " + feedbackId + " not found");
        }
        FeedbackQuestion existingQuestion = existingFeedback.getQuestions().stream()
            .filter(q -> q.getId() == questionId).findFirst().orElse(null);
        if (existingQuestion == null) {
            throw new ResourceNotFoundException("Question with Id " + questionId + " not found");
        }
        existingFeedback.getQuestions().remove(existingQuestion);
        existingFeedback.setUpdatedBy(userUtils.getCurrentUser());
        return feedbackRepository.save(existingFeedback);
    }

    @Override
    public void deleteFeedback(Long id) {
        Feedback existingFeedback = getFeedbackById(id);
        if (existingFeedback == null) {
            throw new ResourceNotFoundException("Feedback with Id " + id + " not found");
        }
        feedbackRepository.delete(existingFeedback);
    }
}