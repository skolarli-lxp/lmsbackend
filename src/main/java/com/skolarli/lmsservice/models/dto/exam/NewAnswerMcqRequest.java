package com.skolarli.lmsservice.models.dto.exam;

import lombok.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class NewAnswerMcqRequest {
    private static final Logger logger = LoggerFactory.getLogger(NewAnswerMcqRequest.class);

    private Long questionId;

    private Integer answer;

    private String studentRemarks;
}