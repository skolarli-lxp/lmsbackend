package com.skolarli.lmsservice.models.dto;

import lombok.*;

import java.util.List;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class NewExamQuestionsAllTypesRequest {

    List<NewExamQuestionMcqRequest> mcqQuestions;
    List<NewExamQuestionSubjectiveRequest> subjectiveQuestions;
    List<NewExamQuestionTrueOrFalseRequest> trueOrFalseQuestions;
}