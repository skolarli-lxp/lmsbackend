package com.skolarli.lmsservice.models.dto.exam.answerbook;

import com.skolarli.lmsservice.models.AnswerBookStatus;
import lombok.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.ZonedDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class GetScoresResponse {
    private static final Logger logger = LoggerFactory.getLogger(GetScoresResponse.class);

    private Long id;

    private Long examId;

    private Long studentId;

    AnswerBookStatus status;

    private int totalMarks;

    private double obtainedMarks;

    private int additionalMarks;

    private int totalQuestions;

    private int attemptedQuestions;

    private int correctAnswers;

    private int incorrectAnswers;

    private int partialCorrectAnswers;

    private int totalDuration;

    private int timeTaken;

    private ZonedDateTime sessionStartTime;

    private ZonedDateTime sessionEndTime;

    private String remarks;
}
