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
@Table(name = "truefalse_answers", uniqueConstraints = {
    @UniqueConstraint(name = "uniqquestion", columnNames = {"answer_book_id", "question_id"})
})
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
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    @JsonIdentityReference(alwaysAsId = true)
    ExamQuestionTrueOrFalse question;

    private int answer;

    private double marksGiven;

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
        if (answerTrueFalse.getAnswer() != 0) {
            this.answer = answerTrueFalse.getAnswer();
        }
        if (answerTrueFalse.getMarksGiven() != 0) {
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
        if (this.answer == 0) {
            this.evaluationResult = EvaluationResult.INCORRECT;
            this.evaluatorRemarks = "No answer provided";
            this.marksGiven = 0.0;
            return;
        }
        if (this.answer == this.question.getCorrectAnswer()) {
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
        newAnswerResponse.setAnswerBookId(this.answerBook.getId());
        newAnswerResponse.setQuestionId(this.question.getId());
        newAnswerResponse.setAnswerId(this.id);
        newAnswerResponse.setAnswer(String.valueOf(this.answer));
        return newAnswerResponse;
    }

    public GetAnswerResponse toGetAnswerResponse() {
        GetAnswerResponse getAnswerResponse = new GetAnswerResponse();
        getAnswerResponse.setQuestionId(this.question.getId());
        getAnswerResponse.setAnswerId(this.id);
        getAnswerResponse.setAnswer(String.valueOf(this.answer));
        return getAnswerResponse;
    }
}
