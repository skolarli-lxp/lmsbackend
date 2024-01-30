package com.skolarli.lmsservice.models.dto.course;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CountResponse {
    private Long totalCourses;

    private Long publishedCourses;

    private Long totalBatches;

    private Long totalBatchSchedules;

    private Long totalStudents;

    private Long totalInstructors;
}