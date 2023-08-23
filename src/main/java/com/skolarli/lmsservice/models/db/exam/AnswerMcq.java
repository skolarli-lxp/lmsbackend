package com.skolarli.lmsservice.models.db.exam;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.skolarli.lmsservice.exception.OperationNotSupportedException;
import com.skolarli.lmsservice.models.EvaluationResult;
import com.skolarli.lmsservice.models.db.core.LmsUser;
import com.skolarli.lmsservice.models.db.core.Tenantable;
import com.skolarli.lmsservice.models.dto.exam.answerbook.GetAnswerResponse;
import com.skolarli.lmsservice.models.dto.exam.answerbook.NewAnswerResponse;
import lombok.*;
import org.hibernate.annotations.Check;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.*;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "mcq_answers", uniqueConstraints = {
    @UniqueConstraint(name = "uniqquestion", columnNames = {"answer_book_id", "question_id"})
})
public class AnswerMcq extends Tenantable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "answer_book_id")
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    @JsonIdentityReference(alwaysAsId = true)
    AnswerBook answerBook;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id")
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    @JsonIdentityReference(alwaysAsId = true)
    ExamQuestionMcq question;

    @Check(constraints = "answers >= 0 AND answer <=6")
    private String answer;

    private double marksGiven;

    private String evaluatorRemarks;

    private String studentRemarks;

    private EvaluationResult evaluationResult = EvaluationResult.NOT_EVALUATED;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    @JsonIdentityReference(alwaysAsId = true)
    LmsUser createdBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "updated_by")
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    @JsonIdentityReference(alwaysAsId = true)
    LmsUser updatedBy;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_time", nullable = false, updatable = false)
    @CreationTimestamp
    private Date createdTime;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "last_updated_time", nullable = false)
    @UpdateTimestamp
    private Date lastUpdatedTime;

    public void update(AnswerMcq answerMcq) {
        if (answerMcq.getAnswer() != null) {
            this.answer = answerMcq.getAnswer();
        }
        if (answerMcq.getMarksGiven() != 0) {
            this.marksGiven = answerMcq.getMarksGiven();
        }
        if (answerMcq.getEvaluatorRemarks() != null) {
            this.evaluatorRemarks = answerMcq.getEvaluatorRemarks();
        }
        if (answerMcq.getStudentRemarks() != null) {
            this.studentRemarks = answerMcq.getStudentRemarks();
        }
        if (answerMcq.getLastUpdatedTime() != null) {
            this.lastUpdatedTime = answerMcq.getLastUpdatedTime();
        }
    }

    private Boolean equateAnswers(String correctAnswer, String givenAnswer) {
        if ((correctAnswer == null || correctAnswer.isEmpty()) && (givenAnswer == null || givenAnswer.isEmpty())) {
            return true;
        } else if (correctAnswer == null || correctAnswer.isEmpty() || givenAnswer == null || givenAnswer.isEmpty()) {
            return false;
        }
        List<Integer> correctAnswers = Arrays.stream(correctAnswer.split(","))
                .map(Integer::parseInt).collect(Collectors.toList());
        List<Integer> givenAnswers = Arrays.stream(givenAnswer.split(","))
                .map(Integer::parseInt).collect(Collectors.toList());
        if (correctAnswers.size() != givenAnswers.size()) {
            return false;
        }
        for (Integer answer : correctAnswers) {
            if (!givenAnswers.contains(answer)) {
                return false;
            }
        }
        return true;
    }

    public void autoEvaluate() {
        if (this.answer == null) {
            this.evaluationResult = EvaluationResult.INCORRECT;
            this.evaluatorRemarks = "No answer provided";
            this.marksGiven = 0.0;
            return;
        }
        if (equateAnswers(this.question.getCorrectAnswer(), this.answer)) {
            this.evaluationResult = EvaluationResult.CORRECT;
            this.marksGiven = this.question.getMarks() == null ? 0.0 : this.question.getMarks().doubleValue();
        } else {
            this.evaluationResult = EvaluationResult.INCORRECT;
            this.marksGiven = 0.0;
        }
    }

    public void manualEvaluate(Double marksGiven,
                               String evaluatorRemarks,
                               EvaluationResult evaluationResult) {
        if (marksGiven <= this.question.getMarks().doubleValue()) {
            this.marksGiven = marksGiven;
        } else {
            throw new OperationNotSupportedException(
                    "Marks given cannot be greater than total marks");
        }
        this.evaluatorRemarks = evaluatorRemarks;
        this.evaluationResult = evaluationResult;
    }

    public NewAnswerResponse toNewAnswerResponse() {
        NewAnswerResponse newAnswerResponse = new NewAnswerResponse();
        newAnswerResponse.setAnswer(this.answer);
        newAnswerResponse.setAnswerBookId(this.answerBook.getId());
        newAnswerResponse.setQuestionId(this.question.getId());
        newAnswerResponse.setAnswerId(this.id);
        return newAnswerResponse;
    }

    public GetAnswerResponse toGetAnswerResponse() {
        GetAnswerResponse getAnswerResponse = new GetAnswerResponse();
        getAnswerResponse.setAnswer(this.answer);
        getAnswerResponse.setQuestionId(this.question.getId());
        getAnswerResponse.setAnswerId(this.id);
        return getAnswerResponse;
    }
}
