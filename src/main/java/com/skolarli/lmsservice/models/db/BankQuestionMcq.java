package com.skolarli.lmsservice.models.db;

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
@Table(name = "questionbank_mcq")
public class BankQuestionMcq extends BankQuestion {

    // String of comma separated correct answers Ex: (1,2,3,4,5,6)
    String correctAnswer;
    @Check(constraints = "number_of_answers >= 0 AND number_of_answers <=6")
    private int numberOfOptions;
    private String option1;
    private String option2;
    private String option3;
    private String option4;
    private String option5;
    private String option6;
    @Check(constraints = "number_of_correct_answers >= 0 AND number_of_correct_answers <=6")
    private int numberOfCorrectAnswers;

    public void update(BankQuestionMcq bankQuestionMcq) {
        super.update(bankQuestionMcq);


        if (bankQuestionMcq.getNumberOfOptions() != 0) {
            this.setNumberOfOptions(bankQuestionMcq.getNumberOfOptions());
        }
        if (bankQuestionMcq.getOption1() != null) {
            this.setOption1(bankQuestionMcq.getOption1());
        }
        if (bankQuestionMcq.getOption2() != null) {
            this.setOption2(bankQuestionMcq.getOption2());
        }
        if (bankQuestionMcq.getOption3() != null) {
            this.setOption3(bankQuestionMcq.getOption3());
        }
        if (bankQuestionMcq.getOption4() != null) {
            this.setOption4(bankQuestionMcq.getOption4());
        }
        if (bankQuestionMcq.getOption5() != null) {
            this.setOption5(bankQuestionMcq.getOption5());
        }
        if (bankQuestionMcq.getOption6() != null) {
            this.setOption6(bankQuestionMcq.getOption6());
        }
        if (bankQuestionMcq.getNumberOfCorrectAnswers() != 0) {
            this.setNumberOfCorrectAnswers(bankQuestionMcq.getNumberOfCorrectAnswers());
        }
        if (bankQuestionMcq.getCorrectAnswer() != null) {
            this.setCorrectAnswer(bankQuestionMcq.getCorrectAnswer());
        }
    }

    private String[] splitCorrectAnswers(String correctAnswer) {
        if (correctAnswer == null || correctAnswer.isEmpty()) {
            return new String[0];
        }
        return correctAnswer.split(",");
    }

    public Boolean validateFields() {
        /*
        Validate the value of fields
        1. numberOfCorrectAnswers" is equal to the number of comma separated
           values in  "correctAnswer"
        3. Number  of options is  >=0 or <= 6 , atleast that many options should have non null
            values
         */

        if (this.getNumberOfCorrectAnswers() != splitCorrectAnswers(
                this.getCorrectAnswer()).length) {
            return false;
        }

        if (this.getNumberOfOptions() < 0 || this.getNumberOfOptions() > 6) {
            return false;
        }
        switch (this.getNumberOfOptions()) {
            case 6:
                if (this.getOption6() == null || this.getOption6().isEmpty()) {
                    return false;
                }
            case 5:
                if (this.getOption5() == null || this.getOption5().isEmpty()) {
                    return false;
                }
            case 4:
                if (this.getOption4() == null || this.getOption4().isEmpty()) {
                    return false;
                }
            case 3:
                if (this.getOption3() == null || this.getOption3().isEmpty()) {
                    return false;
                }
            case 2:
                if (this.getOption2() == null || this.getOption2().isEmpty()) {
                    return false;
                }
            case 1:
                if (this.getOption1() == null || this.getOption1().isEmpty()) {
                    return false;
                }
            case 0:
                break;
            default:
                return false;
        }
        return true;
    }
}