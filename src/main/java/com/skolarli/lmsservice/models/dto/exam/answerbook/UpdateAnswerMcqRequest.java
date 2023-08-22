package com.skolarli.lmsservice.models.dto.exam.answerbook;

import com.skolarli.lmsservice.models.db.exam.AnswerMcq;
import lombok.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UpdateAnswerMcqRequest {
    private static final Logger logger = LoggerFactory.getLogger(UpdateAnswerMcqRequest.class);

    private List<Integer> answer;

    private String studentRemarks;

    public AnswerMcq toAnswerMcq() {
        AnswerMcq answerMcq = new AnswerMcq();
        answerMcq.setAnswer(
                this.getAnswer().stream().map(String::valueOf).collect(Collectors.joining(","))
        );
        answerMcq.setStudentRemarks(this.getStudentRemarks());
        return answerMcq;
    }
}
