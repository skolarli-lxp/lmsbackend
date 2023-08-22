package com.skolarli.lmsservice.models.dto.exam.answerbook;

import com.skolarli.lmsservice.models.db.exam.AnswerTrueFalse;
import lombok.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UpdateAnswerTrueFalseRequest {
    private static final Logger logger = LoggerFactory.getLogger(UpdateAnswerTrueFalseRequest.class);

    private Integer answer;

    private String studentRemarks;

    public AnswerTrueFalse toAnswerTrueOrFalse() {
        AnswerTrueFalse answerTrueFalse = new AnswerTrueFalse();
        answerTrueFalse.setAnswer(this.answer);
        answerTrueFalse.setStudentRemarks(this.studentRemarks);
        return answerTrueFalse;
    }
}
