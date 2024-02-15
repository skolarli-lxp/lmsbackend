package com.skolarli.lmsservice.models.db.exam;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.skolarli.lmsservice.models.AnswerFormat;
import com.skolarli.lmsservice.models.DifficultyLevel;
import com.skolarli.lmsservice.models.QuestionFormat;
import com.skolarli.lmsservice.models.db.core.Tenantable;
import com.skolarli.lmsservice.models.db.questionbank.BankQuestion;
import com.skolarli.lmsservice.models.db.questionbank.ResourceFile;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@MappedSuperclass
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ExamQuestion extends Tenantable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(columnDefinition = "VARCHAR(1024)")
    @NotNull
    private String question;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "resource_id", referencedColumnName = "id")
    private ResourceFile questionResourceFile;

    private String questionType;

    private DifficultyLevel difficultyLevel;
    private QuestionFormat questionFormat;
    private AnswerFormat answerFormat;

    @Column(columnDefinition = "VARCHAR(8192)")
    private String sampleAnswerText;

    private String sampleAnswerUrl;

    private Integer marks;

    private Integer questionSortOrder;

    @ManyToOne
    @JoinColumn(name = "exam_id")
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    @JsonIdentityReference(alwaysAsId = true)
    private Exam exam;

    public ExamQuestion(BankQuestion bankQuestion, Integer marks, Exam exam) {
        this.question = bankQuestion.getQuestion();
        this.questionResourceFile = bankQuestion.getQuestionResourceFile();
        this.questionType = bankQuestion.getQuestionType();
        this.difficultyLevel = bankQuestion.getDifficultyLevel();
        this.questionFormat = bankQuestion.getQuestionFormat();
        this.answerFormat = bankQuestion.getAnswerFormat();
        this.sampleAnswerText = bankQuestion.getSampleAnswerText();
        this.sampleAnswerUrl = bankQuestion.getSampleAnswerUrl();
        if (marks != null) {
            this.marks = marks;
        } else {
            this.marks = bankQuestion.getMarks();
        }
        this.exam = exam;
    }


    public void update(ExamQuestion question) {
        if (question.getQuestion() != null) {
            this.question = question.getQuestion();
        }
        if (question.getQuestionResourceFile() != null) {
            if (this.questionResourceFile != null) {
                this.questionResourceFile.update(question.getQuestionResourceFile());
            } else {
                this.questionResourceFile = question.getQuestionResourceFile();
            }
        }
        if (question.getQuestionType() != null) {
            this.questionType = question.getQuestionType();
        }
        if (question.getDifficultyLevel() != null) {
            this.difficultyLevel = question.getDifficultyLevel();
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
        if (question.getMarks() != null) {
            this.marks = question.getMarks();
        }
        if (question.getQuestionSortOrder() != null) {
            this.questionSortOrder = question.getQuestionSortOrder();
        }
        if (question.getExam() != null) {
            this.exam = question.getExam();
        }
    }
}
