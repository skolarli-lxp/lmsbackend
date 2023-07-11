package com.skolarli.lmsservice.models.db;

import lombok.*;

import javax.persistence.*;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "questionbank_subjective")
public class BankQuestionSubjective extends BankQuestion {

    private int wordCount;

    @Column(columnDefinition = "TEXT")
    private String correctAnswer;


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