package com.skolarli.lmsservice.models.db;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.skolarli.lmsservice.models.AnswerFormat;
import com.skolarli.lmsservice.models.DifficultyLevel;
import com.skolarli.lmsservice.models.QuestionFormat;
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

    @ManyToOne
    @JoinColumn(name = "exam_id")
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    @JsonIdentityReference(alwaysAsId = true)
    private Exam exam;


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
        if (question.getExam() != null) {
            this.exam = question.getExam();
        }

    }
}
