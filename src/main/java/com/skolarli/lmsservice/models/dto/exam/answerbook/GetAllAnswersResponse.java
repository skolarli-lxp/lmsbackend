package com.skolarli.lmsservice.models.dto.exam.answerbook;

import lombok.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class GetAllAnswersResponse {
    private static final Logger logger = LoggerFactory.getLogger(GetAllAnswersResponse.class);

    private Long answerBookId;

    List<GetAnswerResponse> mcqAnswers;

    List<GetAnswerResponse> trueFalseAnswers;

    List<GetAnswerResponse> subjectiveAnswers;

}
