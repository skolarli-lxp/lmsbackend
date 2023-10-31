package com.skolarli.lmsservice.models.dto.assignment;

import com.skolarli.lmsservice.models.db.resource.LmsResource;
import com.skolarli.lmsservice.models.dto.resource.NewLmsResourceRequest;
import lombok.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class NewAssignmentQuestionRequest {
    private static final Logger logger = LoggerFactory.getLogger(NewAssignmentQuestionRequest.class);

    private long assignmentId;

    private String question;

    private NewLmsResourceRequest questionResourceFile;

    private String questionType;

    private Integer marks;

    private Integer questionSortOrder;

    private Integer wordCount;

    private String sampleAnswerText;

    private String sampleAnswerUrl;
}
