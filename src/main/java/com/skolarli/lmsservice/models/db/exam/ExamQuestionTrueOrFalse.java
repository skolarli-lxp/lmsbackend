package com.skolarli.lmsservice.models.db.exam;

import com.skolarli.lmsservice.models.db.questionbank.BankQuestionTrueOrFalse;
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
@Table(name = "examquestions_tf")
public class ExamQuestionTrueOrFalse extends ExamQuestion {

    private String option1 = "True";
    private String option2 = "False";

    @Check(constraints = "correct_answer == 0 OR correct_answer == 1")
    private Integer correctAnswer;

    public ExamQuestionTrueOrFalse(BankQuestionTrueOrFalse bankQuestionTrueOrFalse,
                                   Integer marks, Exam exam) {
        super(bankQuestionTrueOrFalse, marks, exam);
        this.correctAnswer = bankQuestionTrueOrFalse.getCorrectAnswer();
    }

    public void update(ExamQuestionTrueOrFalse bankQuestionTrueOrFalse) {
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