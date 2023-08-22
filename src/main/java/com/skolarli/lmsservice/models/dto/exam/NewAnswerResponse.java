package com.skolarli.lmsservice.models.dto.exam;

import lombok.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class NewAnswerResponse {
    private static final Logger logger = LoggerFactory.getLogger(NewAnswerResponse.class);

    private Long answerBookId;

    private Long questionId;

    private Long answerId;

    private String answer;
}
