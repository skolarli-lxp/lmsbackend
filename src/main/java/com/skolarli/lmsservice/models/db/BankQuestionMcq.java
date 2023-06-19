package com.skolarli.lmsservice.models.db;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.skolarli.lmsservice.models.AnswerFormat;
import com.skolarli.lmsservice.models.QuestionFormat;
import lombok.*;
import org.hibernate.annotations.Check;
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
@Table(name = "questionbank_mcq")
public class BankQuestionMcq extends Tenantable {
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
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(columnDefinition = "VARCHAR(1024)")
    @NotNull
    private String question;
    @Check(constraints = "number_of_answers >= 0 AND number_of_answers <=6")
    private int numberOfAnswers;
    private QuestionFormat questionType;
    private AnswerFormat answerType;
    private String answer1;
    private String answer2;
    private String answer3;
    private String answer4;
    private String answer5;
    private String answer6;
    private String correctAnswer;
    private int testAdditionCount;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_time", nullable = false, updatable = false)
    @CreationTimestamp
    private Date createdTime;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "last_updated_time", nullable = false)
    @UpdateTimestamp
    private Date lastUpdatedTime;

    public void update(BankQuestionMcq bankQuestionMcq) {
        if (bankQuestionMcq.getQuestion() != null) {
            this.setQuestion(bankQuestionMcq.getQuestion());
        }
        if (bankQuestionMcq.getNumberOfAnswers() != 0) {
            this.setNumberOfAnswers(bankQuestionMcq.getNumberOfAnswers());
        }
        if (bankQuestionMcq.getQuestionType() != null) {
            this.setQuestionType(bankQuestionMcq.getQuestionType());
        }
        if (bankQuestionMcq.getAnswerType() != null) {
            this.setAnswerType(bankQuestionMcq.getAnswerType());
        }
        if (bankQuestionMcq.getAnswer1() != null) {
            this.setAnswer1(bankQuestionMcq.getAnswer1());
        }
        if (bankQuestionMcq.getAnswer2() != null) {
            this.setAnswer2(bankQuestionMcq.getAnswer2());
        }
        if (bankQuestionMcq.getAnswer3() != null) {
            this.setAnswer3(bankQuestionMcq.getAnswer3());
        }
        if (bankQuestionMcq.getAnswer4() != null) {
            this.setAnswer4(bankQuestionMcq.getAnswer4());
        }
        if (bankQuestionMcq.getAnswer5() != null) {
            this.setAnswer5(bankQuestionMcq.getAnswer5());
        }
        if (bankQuestionMcq.getAnswer6() != null) {
            this.setAnswer6(bankQuestionMcq.getAnswer6());
        }
        if (bankQuestionMcq.getCorrectAnswer() != null) {
            this.setCorrectAnswer(bankQuestionMcq.getCorrectAnswer());
        }
        if (bankQuestionMcq.getTestAdditionCount() != 0) {
            this.setTestAdditionCount(bankQuestionMcq.getTestAdditionCount());
        }
        if (bankQuestionMcq.getCourse() != null) {
            this.setCourse(bankQuestionMcq.getCourse());
        }
    }
}