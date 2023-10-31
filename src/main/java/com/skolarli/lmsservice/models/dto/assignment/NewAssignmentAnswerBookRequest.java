package com.skolarli.lmsservice.models.dto.assignment;

import com.skolarli.lmsservice.models.db.assignment.AssignmentAnswerBookStatus;
import com.skolarli.lmsservice.models.dto.resource.NewLmsResourceRequest;
import lombok.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class NewAssignmentAnswerBookRequest {
    private static final Logger logger = LoggerFactory.getLogger(NewAssignmentAnswerBookRequest.class);

    private Long assignmentId;

    private AssignmentAnswerBookStatus assignmentAnswerBookStatus;

    private String answer;

    private NewLmsResourceRequest answerResourceFile;

    private String studentRemarks;

    private String evaluatorRemarks;

    private List<NewAssignmentAnswerRequest> answers;

    private Long studentId;
}
