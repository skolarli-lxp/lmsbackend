package com.skolarli.lmsservice.models.dto;

import com.skolarli.lmsservice.models.db.BatchStatus;
import com.skolarli.lmsservice.models.db.DeliveryFormat;
import com.skolarli.lmsservice.models.db.DiscountType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NewBatchRequest {

    @NotNull(message = "courseId cannot be empty")
    private long courseId;
    @NotNull(message = "instructorId cannot be empty")
    private long instructorId;

    @NotNull(message = "batchName cannot be empty")
    private String batchName;

    private int batchEnrollmentCapacity;

    private String batchAdditionalInfo;

    private long batchFees;

    private DiscountType batchDiscountType;

    private int batchDiscountAmount;

    private Date batchStartDate;

    private Date batchEndDate;

    private BatchStatus batchStatus;

    private DeliveryFormat batchDeliveryFormat;

    private int batchDurationHours;

    private String batchCustomJs;

    private Boolean batchSeoAllowComments;

    private Boolean batchSeoAllowRatings;

    private String batchSeoTitleTag;

    private String batchSeoMetaDescription;

    private String batchSeoMetaKeywords;
}
