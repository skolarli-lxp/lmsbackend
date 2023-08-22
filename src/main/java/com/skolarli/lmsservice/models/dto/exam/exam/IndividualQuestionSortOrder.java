package com.skolarli.lmsservice.models.dto.exam.exam;

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
public class IndividualQuestionSortOrder {
    @NotNull
    private Long questionId;
    @NotNull
    private Integer questionSortOrder;
}