package com.skolarli.lmsservice.models.dto.course;

import com.skolarli.lmsservice.models.BatchStatus;
import com.skolarli.lmsservice.models.DeliveryFormat;
import com.skolarli.lmsservice.models.DiscountType;
import com.skolarli.lmsservice.models.db.course.Batch;
import lombok.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BatchResponse {
    public static final Logger logger = LoggerFactory.getLogger(BatchResponse.class);

    private long id;

    private Long course;

    private String batchName;

    private int batchEnrollmentCapacity;

    private String batchAdditionalInfo;

    private long batchFees;

    private DiscountType batchDiscountType;

    private int batchDiscountAmount;

    private Instant batchStartDate;

    private Instant batchEndDate;

    private BatchStatus batchStatus;

    private DeliveryFormat batchDeliveryFormat;

    private int batchDurationHours;

    private String batchCustomJs;

    private Boolean batchSeoAllowComments;

    private Boolean batchSeoAllowRatings;

    private String batchSeoTitleTag;

    private String batchSeoMetaDescription;

    private String batchSeoMetaKeywords;

    private Boolean batchIsDeleted;

    public BatchResponse(Batch batch) {
        this.id = batch.getId();
        this.course = batch.getCourse().getId();
        this.batchName = batch.getBatchName();
        this.batchEnrollmentCapacity = batch.getBatchEnrollmentCapacity();
        this.batchAdditionalInfo = batch.getBatchAdditionalInfo();
        this.batchFees = batch.getBatchFees();
        this.batchDiscountType = batch.getBatchDiscountType();
        this.batchDiscountAmount = batch.getBatchDiscountAmount();
        this.batchStartDate = batch.getBatchStartDate();
        this.batchEndDate = batch.getBatchEndDate();
        this.batchStatus = batch.getBatchStatus();
        this.batchDeliveryFormat = batch.getBatchDeliveryFormat();
        this.batchDurationHours = batch.getBatchDurationHours();
        this.batchCustomJs = batch.getBatchCustomJs();
        this.batchSeoAllowComments = batch.getBatchSeoAllowComments();
        this.batchSeoAllowRatings = batch.getBatchSeoAllowRatings();
        this.batchSeoTitleTag = batch.getBatchSeoTitleTag();
        this.batchSeoMetaDescription = batch.getBatchSeoMetaDescription();
        this.batchSeoMetaKeywords = batch.getBatchSeoMetaKeywords();
        this.batchIsDeleted = batch.getBatchIsDeleted();
    }
}