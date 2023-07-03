package com.skolarli.lmsservice.models.dto.exam;

import com.skolarli.lmsservice.models.db.ExamQuestionMcq;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;
import javax.validation.constraints.NotNull;

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