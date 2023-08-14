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
    @NotNull
    List<Integer> marks;

    public Boolean isValid() {
        return bankQuestionIds.size() == marks.size();
    }
}


