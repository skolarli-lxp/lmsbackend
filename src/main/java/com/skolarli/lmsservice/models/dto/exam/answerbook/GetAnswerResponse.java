package com.skolarli.lmsservice.models.dto.exam.answerbook;

import lombok.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class GetAnswerResponse {
    private static final Logger logger = LoggerFactory.getLogger(GetAnswerResponse.class);

    private Long questionId;

    private Long answerId;

    private String answer;
}
