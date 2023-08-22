package com.skolarli.lmsservice.models.dto.exam.exam;

import com.skolarli.lmsservice.models.db.exam.ExamQuestionMcq;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class NewExamQuestionMcqRequest extends NewExamQuestionRequest {
    List<Integer> correctAnswer;
    @NotNull
    private Integer numberOfOptions;
    private String option1;
    private String option2;
    private String option3;
    private String option4;
    private String option5;
    private String option6;
    private int numberOfCorrectAnswers;

    public ExamQuestionMcq toExamQuestionMcq() {
        ExamQuestionMcq examQuestionMcq = new ExamQuestionMcq();
        super.toExamQuestion(examQuestionMcq);

        if (correctAnswer != null) {
            examQuestionMcq.setCorrectAnswer(correctAnswer.stream().map(
                    String::valueOf).collect(Collectors.joining(",")));
        } else {
            examQuestionMcq.setCorrectAnswer("");
        }
        examQuestionMcq.setNumberOfOptions(numberOfOptions);
        examQuestionMcq.setOption1(option1);
        examQuestionMcq.setOption2(option2);
        examQuestionMcq.setOption3(option3);
        examQuestionMcq.setOption4(option4);
        examQuestionMcq.setOption5(option5);
        examQuestionMcq.setOption6(option6);
        examQuestionMcq.setNumberOfCorrectAnswers(numberOfCorrectAnswers);
        return examQuestionMcq;
    }
}