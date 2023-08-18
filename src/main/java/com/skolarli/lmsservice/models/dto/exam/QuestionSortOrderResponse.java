package com.skolarli.lmsservice.models.dto.exam;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class QuestionSortOrderResponse {
    Long examId;
    @NotNull
    List<IndividualQuestionSortOrder> mcqQuestions;
    @NotNull
    List<IndividualQuestionSortOrder> trueOrFalseQuestions;
    @NotNull
    List<IndividualQuestionSortOrder> subjectiveQuestions;
}