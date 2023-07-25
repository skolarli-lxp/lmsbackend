package com.skolarli.lmsservice.models.dto.exam;

import lombok.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class NewAnswerSubjectiveRequest {
    private static final Logger logger = LoggerFactory.getLogger(NewAnswerSubjectiveRequest.class);

    private Long questionId;

    private String answer;

    private String studentRemarks;
}
