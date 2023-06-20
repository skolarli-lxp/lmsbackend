package com.skolarli.lmsservice.models.db;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.skolarli.lmsservice.models.AnswerFormat;
import com.skolarli.lmsservice.models.QuestionFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;
import javax.persistence.*;
import javax.validation.constraints.NotNull;

@MappedSuperclass
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Question extends Tenantable {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id")
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    @JsonIdentityReference(alwaysAsId = true)
    Course course;
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
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(columnDefinition = "VARCHAR(1024)")
    @NotNull
    private String question;

    private String questionType;
    private QuestionFormat questionFormat;
    private AnswerFormat answerFormat;
    private String sampleAnswerText;

    private String sampleAnswerUrl;

    private int testAdditionCount;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_time", nullable = false, updatable = false)
    @CreationTimestamp
    private Date createdTime;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "last_updated_time", nullable = false)
    @UpdateTimestamp
    private Date lastUpdatedTime;

    public void update(Question question) {
        if (question.getQuestion() != null) {
            this.question = question.getQuestion();
        }
        if (question.getQuestionType() != null) {
            this.questionType = question.getQuestionType();
        }
        if (question.getQuestionFormat() != null) {
            this.questionFormat = question.getQuestionFormat();
        }
        if (question.getAnswerFormat() != null) {
            this.answerFormat = question.getAnswerFormat();
        }
        if (question.getSampleAnswerText() != null) {
            this.sampleAnswerText = question.getSampleAnswerText();
        }
        if (question.getSampleAnswerUrl() != null) {
            this.sampleAnswerUrl = question.getSampleAnswerUrl();
        }
        if (question.getCourse() != null) {
            this.course = question.getCourse();
        }
    }
}
