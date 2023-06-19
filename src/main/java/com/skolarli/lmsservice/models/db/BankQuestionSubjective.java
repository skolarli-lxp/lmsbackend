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
@Table(name = "questionbank_subjective")
public class BankQuestionSubjective extends Tenantable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(columnDefinition = "VARCHAR(1024)")
    @NotNull
    private String question;


    private QuestionFormat questionType;

    private AnswerFormat answerType;

    private int wordCount;

    private String sampleAnswerText;

    private String sampleAnswerUrl;

    private String correctAnswer;

    private int testAdditionCount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    @JsonIdentityReference(alwaysAsId = true)
    LmsUser createdBy;

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

    public void update(BankQuestionSubjective bankQuestionSubjective) {

        if (bankQuestionSubjective.getQuestion() != null) {
            this.setQuestion(bankQuestionSubjective.getQuestion());
        }
        if (bankQuestionSubjective.getQuestionType() != null) {
            this.setQuestionType(bankQuestionSubjective.getQuestionType());
        }
        if (bankQuestionSubjective.getAnswerType() != null) {
            this.setAnswerType(bankQuestionSubjective.getAnswerType());
        }
        if (bankQuestionSubjective.getWordCount() != 0) {
            this.setWordCount(bankQuestionSubjective.getWordCount());
        }
        if (bankQuestionSubjective.getSampleAnswerText() != null) {
            this.setSampleAnswerText(bankQuestionSubjective.getSampleAnswerText());
        }
        if (bankQuestionSubjective.getSampleAnswerUrl() != null) {
            this.setSampleAnswerUrl(bankQuestionSubjective.getSampleAnswerUrl());
        }
        if (bankQuestionSubjective.getCorrectAnswer() != null) {
            this.setCorrectAnswer(bankQuestionSubjective.getCorrectAnswer());
        }
        if (bankQuestionSubjective.getTestAdditionCount() != 0) {
            this.setTestAdditionCount(bankQuestionSubjective.getTestAdditionCount());
        }
        if (bankQuestionSubjective.getCourse() != null) {
            this.setCourse(bankQuestionSubjective.getCourse());
        }
    }
}