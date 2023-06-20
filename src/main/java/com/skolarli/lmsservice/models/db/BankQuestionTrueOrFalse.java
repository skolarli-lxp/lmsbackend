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
@Table(name = "questionbank_tf")
public class BankQuestionTrueOrFalse extends Question {

    private String option1 = "True";
    private String option2 = "False";

    @Check(constraints = "correct_answer == 0 OR correct_answer == 1")
    private Integer correctAnswer;


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