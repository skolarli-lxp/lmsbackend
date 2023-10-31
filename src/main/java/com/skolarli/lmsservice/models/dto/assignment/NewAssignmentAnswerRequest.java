package com.skolarli.lmsservice.models.dto.assignment;

import com.skolarli.lmsservice.models.dto.resource.NewLmsResourceRequest;
import lombok.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class NewAssignmentAnswerRequest {
    private static final Logger logger = LoggerFactory.getLogger(NewAssignmentAnswerRequest.class);

    private Long answerBookId;

    private Long questionId;

    private String answer;

    private NewLmsResourceRequest answerResourceFile;
}
