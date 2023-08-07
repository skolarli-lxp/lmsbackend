package com.skolarli.lmsservice.models.db.questionbank;

import com.skolarli.lmsservice.models.db.exam.ExamQuestionTrueOrFalse;
import lombok.*;
import org.hibernate.annotations.Check;

import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "questionbank_tf")
public class BankQuestionTrueOrFalse extends BankQuestion {

    private String option1 = "True";
    private String option2 = "False";

    @Check(constraints = "correct_answer == 0 OR correct_answer == 1")
    private Integer correctAnswer;

    public BankQuestionTrueOrFalse(ExamQuestionTrueOrFalse examQuestionTrueOrFalse) {
        super(examQuestionTrueOrFalse);
        this.setOption1(examQuestionTrueOrFalse.getOption1());
        this.setOption2(examQuestionTrueOrFalse.getOption2());
        this.setCorrectAnswer(examQuestionTrueOrFalse.getCorrectAnswer());
    }

    public void update(BankQuestionTrueOrFalse bankQuestionTrueOrFalse) {
        super.update(bankQuestionTrueOrFalse);

        if (bankQuestionTrueOrFalse.getOption1() != null) {
            this.setOption1(bankQuestionTrueOrFalse.getOption1());
        }
        if (bankQuestionTrueOrFalse.getOption2() != null) {
            this.setOption2(bankQuestionTrueOrFalse.getOption2());
        }
        if (bankQuestionTrueOrFalse.getCorrectAnswer() != null) {
            this.setCorrectAnswer(bankQuestionTrueOrFalse.getCorrectAnswer());
        }
    }
}