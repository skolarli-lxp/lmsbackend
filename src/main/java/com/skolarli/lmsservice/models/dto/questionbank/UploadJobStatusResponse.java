package com.skolarli.lmsservice.models.dto.questionbank;

import lombok.*;
import org.springframework.batch.core.JobExecution;

import java.sql.Timestamp;
import java.util.List;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UploadJobStatusResponse {
    @NotNull
    private Long jobId;
    @NotNull
    private String status;
    private List errors;
    private Timestamp createTime;
    private Timestamp startTime;
    private Timestamp endTime;

    public UploadJobStatusResponse(JobExecution jobExecution) {
        this.jobId = jobExecution.getJobId();
        this.status = jobExecution.getStatus().toString();
        this.errors = jobExecution.getAllFailureExceptions();
        if (jobExecution.getCreateTime() != null) {
            this.createTime = new Timestamp(jobExecution.getCreateTime().getTime());
        }
        if (jobExecution.getStartTime() != null) {
            this.startTime = new Timestamp(jobExecution.getStartTime().getTime());
        }
        if (jobExecution.getEndTime() != null) {
            this.endTime = new Timestamp(jobExecution.getEndTime().getTime());
        }
    }
}