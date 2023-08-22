package com.skolarli.lmsservice.models.dto.exam.answerbook;

import com.skolarli.lmsservice.models.db.exam.AnswerSubjective;
import lombok.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UpdateAnswerSubjectiveRequest {
    private static final Logger logger = LoggerFactory.getLogger(UpdateAnswerSubjectiveRequest.class);

    private String answer;

    private String studentRemarks;

    public AnswerSubjective toAnswerSubjective() {
        AnswerSubjective answerSubjective = new AnswerSubjective();
        answerSubjective.setAnswer(this.getAnswer());
        answerSubjective.setStudentRemarks(this.getStudentRemarks());
        return answerSubjective;
    }
}
