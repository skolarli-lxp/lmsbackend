package com.skolarli.lmsservice.models.dto.exam;

import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class NewExamQuestionsAllTypesResponse {

    List<NewExamQuestionMcqRequest> mcqQuestions;
    List<NewExamQuestionSubjectiveRequest> subjectiveQuestions;
    List<NewExamQuestionTrueOrFalseRequest> trueOrFalseQuestions;
}