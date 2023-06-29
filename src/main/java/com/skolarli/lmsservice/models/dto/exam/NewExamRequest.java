package com.skolarli.lmsservice.models.dto.exam;


import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class NewExamRequest {

    Long courseId;

    Long batchId;

    private String examName;

    private String examType;

    private String durationMins;

    private List<NewExamQuestionMcqRequest> mcqQuestions;

    private List<NewExamQuestionSubjectiveRequest> subjectiveQuestions;

    private List<NewExamQuestionTrueOrFalseRequest> trueOrFalseQuestions;

}
