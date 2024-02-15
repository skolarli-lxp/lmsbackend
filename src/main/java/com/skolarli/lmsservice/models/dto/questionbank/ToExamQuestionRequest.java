package com.skolarli.lmsservice.models.dto.questionbank;

import lombok.*;

import java.util.List;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ToExamQuestionRequest {
    @NotNull
    List<Long> bankQuestionIds;
    List<Integer> marks;
}


