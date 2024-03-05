package com.skolarli.lmsservice.models.db.questionbank;

import com.skolarli.lmsservice.models.db.exam.ExamQuestionSubjective;
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
@Table(name = "questionbank_subjective")
public class BankQuestionSubjective extends BankQuestion {

    private Integer wordCount;

    @Column(columnDefinition = "TEXT")
    private String correctAnswer;

    public BankQuestionSubjective(ExamQuestionSubjective examQuestionSubjective) {
        super(examQuestionSubjective);
        this.setWordCount(examQuestionSubjective.getWordCount());
        this.setCorrectAnswer(examQuestionSubjective.getCorrectAnswer());
    }


    public void update(BankQuestionSubjective bankQuestionSubjective) {
        super.update(bankQuestionSubjective);

        if (bankQuestionSubjective.getWordCount() != 0) {
            this.setWordCount(bankQuestionSubjective.getWordCount());
        }
        if (bankQuestionSubjective.getCorrectAnswer() != null) {
            this.setCorrectAnswer(bankQuestionSubjective.getCorrectAnswer());
        }
    }
}