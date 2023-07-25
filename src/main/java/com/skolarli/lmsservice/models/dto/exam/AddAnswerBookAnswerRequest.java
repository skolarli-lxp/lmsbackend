package com.skolarli.lmsservice.models.dto.exam;

import lombok.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AddAnswerBookAnswerRequest {
    private static final Logger logger = LoggerFactory.getLogger(AddAnswerBookAnswerRequest.class);

    private List<NewAnswerMcqRequest> mcqAnswers;

    private List<NewAnswerSubjectiveRequest> subjectiveAnswers;

    private List<NewAnswerTrueFalseRequest> trueFalseAnswers;
}
