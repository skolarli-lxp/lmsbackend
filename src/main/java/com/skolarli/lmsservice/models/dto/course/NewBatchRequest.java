package com.skolarli.lmsservice.models.dto.course;

import com.skolarli.lmsservice.models.BatchStatus;
import com.skolarli.lmsservice.models.DeliveryFormat;
import com.skolarli.lmsservice.models.DiscountType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.ZonedDateTime;
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

    private ZonedDateTime batchStartDate;

    private ZonedDateTime batchEndDate;

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
