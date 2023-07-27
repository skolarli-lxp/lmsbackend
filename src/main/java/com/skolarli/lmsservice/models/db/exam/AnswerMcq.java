package com.skolarli.lmsservice.models.db.exam;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.skolarli.lmsservice.models.db.core.LmsUser;
import com.skolarli.lmsservice.models.db.core.Tenantable;
import lombok.*;
import org.hibernate.annotations.Check;
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
@Table(name = "mcq_answers")
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
    ExamQuestionMcq question;

    @Check(constraints = "answers >= 0 AND answer <=6")
    private Integer answer;

    private Double marksGiven;

    private String evaluatorRemarks;

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

    public void update(AnswerMcq answerMcq) {
        if (answerMcq.getQuestion() != null) {
            this.question = answerMcq.getQuestion();
        }
        if (answerMcq.getAnswer() != null) {
            this.answer = answerMcq.getAnswer();
        }
        if (answerMcq.getMarksGiven() != null) {
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
}