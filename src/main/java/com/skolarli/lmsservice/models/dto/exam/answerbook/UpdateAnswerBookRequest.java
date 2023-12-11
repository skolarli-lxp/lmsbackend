package com.skolarli.lmsservice.models.dto.exam.answerbook;

import com.skolarli.lmsservice.models.AnswerBookStatus;
import com.skolarli.lmsservice.models.db.exam.AnswerBook;
import lombok.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.ZonedDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UpdateAnswerBookRequest {
    private static final Logger logger = LoggerFactory.getLogger(UpdateAnswerBookRequest.class);

    AnswerBookStatus status;

    private int totalMarks;

    private double obtainedMarks;

    private int additionalMarks;

    private int totalQuestions;

    private int attemptedQuestions;

    private int correctAnswers;

    private int incorrectAnswers;

    private int partiallyCorrectAnswers;

    private int totalDuration;

    private int timeTaken;

    private ZonedDateTime sessionStartTime;

    private ZonedDateTime sessionEndTime;

    private String remarks;

    public AnswerBook toAnswerBook() {
        AnswerBook answerBook = new AnswerBook();
        answerBook.setStatus(this.status);
        answerBook.setTotalMarks(this.totalMarks);
        answerBook.setObtainedMarks(this.obtainedMarks);
        answerBook.setAdditionalMarks(this.additionalMarks);
        answerBook.setTotalQuestions(this.totalQuestions);
        answerBook.setAttemptedQuestions(this.attemptedQuestions);
        answerBook.setCorrectAnswers(this.correctAnswers);
        answerBook.setIncorrectAnswers(this.incorrectAnswers);
        answerBook.setPartiallyCorrectAnswers(this.partiallyCorrectAnswers);
        answerBook.setTotalDuration(this.totalDuration);
        answerBook.setTimeTaken(this.timeTaken);
        answerBook.setSessionStartTime(this.sessionStartTime);
        answerBook.setSessionEndTime(this.sessionEndTime);
        answerBook.setRemarks(this.remarks);
        return answerBook;
    }

}
