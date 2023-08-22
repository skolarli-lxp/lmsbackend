package com.skolarli.lmsservice.models.dto.exam.exam;

import lombok.*;

import java.util.List;

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