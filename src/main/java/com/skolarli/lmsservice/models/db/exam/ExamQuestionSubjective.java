package com.skolarli.lmsservice.models.db.exam;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "examquestions_subjective")
public class ExamQuestionSubjective extends ExamQuestion {

    private Integer wordCount;

    @Column(columnDefinition = "TEXT")
    private String correctAnswer;


    public void update(ExamQuestionSubjective bankQuestionSubjective) {
        super.update(bankQuestionSubjective);

        if (bankQuestionSubjective.getWordCount() != null) {
            this.setWordCount(bankQuestionSubjective.getWordCount());
        }
        if (bankQuestionSubjective.getCorrectAnswer() != null) {
            this.setCorrectAnswer(bankQuestionSubjective.getCorrectAnswer());
        }
    }
}