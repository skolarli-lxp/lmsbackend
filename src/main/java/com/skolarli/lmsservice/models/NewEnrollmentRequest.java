package com.skolarli.lmsservice.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.skolarli.lmsservice.models.db.Batch;
import com.skolarli.lmsservice.models.db.Enrollment;
import com.skolarli.lmsservice.models.db.LmsUser;
import com.skolarli.lmsservice.services.BatchService;
import com.skolarli.lmsservice.services.LmsUserService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NewEnrollmentRequest {
    @NotNull(message = "batchId cannot be empty")
    private long batchId;
    @NotNull(message = "studentId cannot be empty")
    private long studentId;

    @JsonIgnore
    @Autowired
    BatchService batchService;
    @JsonIgnore
    @Autowired
    LmsUserService lmsUserService;

    public Enrollment toEnrollment() {
        Batch batch = batchService.getBatch(batchId);
        LmsUser student = lmsUserService.getLmsUserById(studentId);
        return new Enrollment(batch, student);
    }
}
