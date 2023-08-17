package com.skolarli.lmsservice.models.db.exam;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.skolarli.lmsservice.exception.OperationNotSupportedException;
import com.skolarli.lmsservice.models.EvaluationResult;
import com.skolarli.lmsservice.models.db.core.LmsUser;
import com.skolarli.lmsservice.models.db.core.Tenantable;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;
import javax.persistence.*;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "truefalse_answers")
public class AnswerTrueFalse extends Tenantable {
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
    ExamQuestionTrueOrFalse question;

    private Integer answer;

    private Double marksGiven;

    private String evaluatorRemarks;

    private EvaluationResult evaluationResult = EvaluationResult.NOT_EVALUATED;

    private String studentRemarks;

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

    public void update(AnswerTrueFalse answerTrueFalse) {
        if (answerTrueFalse.getAnswer() != null) {
            this.answer = answerTrueFalse.getAnswer();
        }
        if (answerTrueFalse.getMarksGiven() != null) {
            this.marksGiven = answerTrueFalse.getMarksGiven();
        }
        if (answerTrueFalse.getEvaluatorRemarks() != null) {
            this.evaluatorRemarks = answerTrueFalse.getEvaluatorRemarks();
        }
        if (answerTrueFalse.getStudentRemarks() != null) {
            this.studentRemarks = answerTrueFalse.getStudentRemarks();
        }
        if (answerTrueFalse.getUpdatedBy() != null) {
            this.updatedBy = answerTrueFalse.getUpdatedBy();
        }
    }

    public void autoEvaluate() {
        if (this.answer == null) {
            this.evaluationResult = EvaluationResult.INCORRECT;
            this.evaluatorRemarks = "No answer provided";
            this.marksGiven = 0.0;
            return;
        }
        if (this.answer.equals(this.question.getCorrectAnswer())) {
            this.evaluationResult = EvaluationResult.CORRECT;
            this.marksGiven = this.question.getMarks().doubleValue();
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
}
