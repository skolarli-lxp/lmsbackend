package com.skolarli.lmsservice.models.db;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.skolarli.lmsservice.models.AnswerFormat;
import com.skolarli.lmsservice.models.QuestionFormat;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;
import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "questionbank_tf")
public class BankQuestionTrueOrFalse extends Tenantable {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    @JsonIdentityReference(alwaysAsId = true)
    LmsUser createdBy;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(columnDefinition = "VARCHAR(1024)")
    @NotNull
    private String question;
    private QuestionFormat questionType;
    private AnswerFormat answerType;
    private String option1 = "True";
    private String option2 = "False";
    private String sampleAnswerText;
    private String sampleAnswerUrl;
    private String correctAnswer;
    private int testAdditionCount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id")
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    @JsonIdentityReference(alwaysAsId = true)
    Course course;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_time", nullable = false, updatable = false)
    @CreationTimestamp
    private Date createdTime;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "last_updated_time", nullable = false)
    @UpdateTimestamp
    private Date lastUpdatedTime;

    public void update(BankQuestionTrueOrFalse bankQuestionTrueOrFalse) {
        if (bankQuestionTrueOrFalse.getQuestion() != null) {
            this.setQuestion(bankQuestionTrueOrFalse.getQuestion());
        }
        if (bankQuestionTrueOrFalse.getQuestionType() != null) {
            this.setQuestionType(bankQuestionTrueOrFalse.getQuestionType());
        }
        if (bankQuestionTrueOrFalse.getAnswerType() != null) {
            this.setAnswerType(bankQuestionTrueOrFalse.getAnswerType());
        }
        if (bankQuestionTrueOrFalse.getOption1() != null) {
            this.setOption1(bankQuestionTrueOrFalse.getOption1());
        }
        if (bankQuestionTrueOrFalse.getOption2() != null) {
            this.setOption2(bankQuestionTrueOrFalse.getOption2());
        }
        if (bankQuestionTrueOrFalse.getSampleAnswerText() != null) {
            this.setSampleAnswerText(bankQuestionTrueOrFalse.getSampleAnswerText());
        }
        if (bankQuestionTrueOrFalse.getSampleAnswerUrl() != null) {
            this.setSampleAnswerUrl(bankQuestionTrueOrFalse.getSampleAnswerUrl());
        }
        if (bankQuestionTrueOrFalse.getCorrectAnswer() != null) {
            this.setCorrectAnswer(bankQuestionTrueOrFalse.getCorrectAnswer());
        }
        if (bankQuestionTrueOrFalse.getTestAdditionCount() != 0) {
            this.setTestAdditionCount(bankQuestionTrueOrFalse.getTestAdditionCount());
        }
        if (bankQuestionTrueOrFalse.getCourse() != null) {
            this.setCourse(bankQuestionTrueOrFalse.getCourse());
        }
    }
}