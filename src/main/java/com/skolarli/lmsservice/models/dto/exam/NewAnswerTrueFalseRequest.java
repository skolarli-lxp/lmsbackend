package com.skolarli.lmsservice.models.dto.exam;

import lombok.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class NewAnswerTrueFalseRequest {
    private static final Logger logger = LoggerFactory.getLogger(NewAnswerTrueFalseRequest.class);

    private Long answerBookId;

    private Long questionId;

    private Integer answer;

    private String studentRemarks;
}
