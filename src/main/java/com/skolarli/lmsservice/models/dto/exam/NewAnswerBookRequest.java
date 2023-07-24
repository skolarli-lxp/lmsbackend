package com.skolarli.lmsservice.models.dto.exam;

import lombok.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.ZonedDateTime;
import java.util.List;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class NewAnswerBookRequest {
    private static final Logger logger = LoggerFactory.getLogger(NewAnswerBookRequest.class);

    @NotNull
    private Long examId;

    @NotNull
    private Long studentId;

    private String remarks;

    private List<NewAnswerMcqRequest> mcqAnswers;

    private List<NewAnswerSubjectiveRequest> subjectiveAnswers;

    private List<NewAnswerTrueFalseRequest> trueFalseAnswers;

    private ZonedDateTime sessionStartTime;
    private ZonedDateTime sessionEndTime;

    private Long courseId;
    private Long batchId;
}
