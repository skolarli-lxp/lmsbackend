package com.skolarli.lmsservice.models.dto.exam;

import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AddExamQuestionToQbRequest {
    List<Long> mcqQuestionsList;
    List<Long> trueOrFalseQuestionsList;
    List<Long> subjectiveQuestionsList;
}