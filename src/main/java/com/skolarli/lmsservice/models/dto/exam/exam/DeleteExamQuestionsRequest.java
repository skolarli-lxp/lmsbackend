package com.skolarli.lmsservice.models.dto.exam.exam;

import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DeleteExamQuestionsRequest {
    private List<Long> mcqQuestionsIds;

    private List<Long> trueOrFalseQuestionsIds;

    private List<Long> subjectiveQuestionsIds;
}