package com.skolarli.lmsservice.models;

import lombok.*;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UpdateLessonDescriptionRequest {

    @NotNull
    private String lessonDescription;
}