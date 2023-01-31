package com.skolarli.lmsservice.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

import javax.validation.constraints.NotNull;

import com.skolarli.lmsservice.models.db.DisountType;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NewBatchRequest {
    
    @NotNull(message = "courseId cannot be empty")
    private long courseId;
    @NotNull(message = "instructorId cannot be empty")
    private long instructorId;

    private int batchEnrollmentCapacity;

    private String batchAdditionalInfo;

    private long batchFees;

    private DisountType batchDiscountType;

    private int batchDiscountAmount;

    private Date batchStartDate;

    private Date batchEndDate;

    private int batchDurationHours;

    private String batchCustomJs;

    private Boolean batchSeoAllowComments;

    private Boolean batchSeoAllowRatings;

    private String batchSeoTitleTag;

    private String batchSeoMetaDescription;

    private String batchSeoMetaKeywords;
}
