package com.skolarli.lmsservice.models.dto.assignment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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