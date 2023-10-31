package com.skolarli.lmsservice.models.dto.assignment;

import com.skolarli.lmsservice.models.db.assignment.AssignmentStatus;
import com.skolarli.lmsservice.models.dto.resource.NewLmsResourceRequest;
import lombok.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.ZonedDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class NewAssignmentRequest {
    private static final Logger logger = LoggerFactory.getLogger(NewAssignmentRequest.class);

    private String assignmentName;

    private String assignmentObjective;

    private String assignmentDescription;

    private NewLmsResourceRequest assignmentResourceFile;

    private ZonedDateTime assignmentPublishedDate;

    private ZonedDateTime assignmentDueDate;

    private AssignmentStatus assignmentStatus;

    private String assignmentInstructionsForStudents;

    private String assignmentInstructionsForTeachers;

    private int assignmentTotalMarks;

    private List<NewAssignmentQuestionRequest> assignmentQuestions;

    Long studentId;

    Long courseId;

    Long batchId;

    Long batchScheduleId;
}
