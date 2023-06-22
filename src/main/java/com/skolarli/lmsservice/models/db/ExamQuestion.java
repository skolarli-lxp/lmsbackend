package com.skolarli.lmsservice.models.db;

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

    private String questionType;

    private DifficultyLevel difficultyLevel;
    private QuestionFormat questionFormat;
    private AnswerFormat answerFormat;

    @Column(columnDefinition = "VARCHAR(8192)")
    private String sampleAnswerText;

    private String sampleAnswerUrl;

    private int weightage;


    public void update(ExamQuestion question) {
        if (question.getQuestion() != null) {
            this.question = question.getQuestion();
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
        if (question.getWeightage() != 0) {
            this.weightage = question.getWeightage();
        }

    }
}
