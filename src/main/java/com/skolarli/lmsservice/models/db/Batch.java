package com.skolarli.lmsservice.models.db;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name="batches")
@AllArgsConstructor
@NoArgsConstructor
public class Batch extends  Tenantable{
    public static final Logger logger = LoggerFactory.getLogger(Batch.class);

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "course_id")
    @JsonIgnoreProperties("courseBatches") 
    private Course course;

    @ManyToOne
    @JoinColumn(name="instructor_id")
    @JsonIgnoreProperties("batches")
    private LmsUser instructor;

    @OneToMany(mappedBy = "batch")
    @JsonIgnoreProperties("batch")
    private List<BatchSchedule> batchSchedules;

    private int batchEnrollmentCapacity;

    private String batchAdditionalInfo;

    private long batchFees;

    private DisountType batchDiscountType;

    private int batchDiscountAmount;

    private Date batchStartDate;

    private Date batchEndDate;

    private String batchCustomJs;

    private Boolean batchSeoAllowComments;

    private Boolean batchSeoAllowRatings;

    private String batchSeoTitleTag;

    private String batchSeoMetaDescription;

    private String batchSeoMetaKeywords;

    private Boolean batchIsDeleted;

    public void update(Batch newBatch) {
        if (newBatch.getId() != 0) {
            logger.error("Cannot update batch id");
        }
        if (newBatch.getCourse() != null) {
            this.course = newBatch.getCourse();
        }
        if (newBatch.getInstructor() != null) {
            this.instructor = newBatch.getInstructor();
        }
        if (newBatch.getBatchSchedules() != null && !newBatch.getBatchSchedules().isEmpty()) {
            newBatch.getBatchSchedules().forEach(currentSchedule -> {
                if (!this.getBatchSchedules().contains(currentSchedule)) {
                    this.getBatchSchedules().add(currentSchedule);
                }
            });
        }
        if (newBatch.getBatchEnrollmentCapacity() != 0) {
            this.batchEnrollmentCapacity = newBatch.getBatchEnrollmentCapacity();
        }
        if (newBatch.getBatchAdditionalInfo() != null) {
            this.batchAdditionalInfo = newBatch.getBatchAdditionalInfo();
        }
        if (newBatch.getBatchFees() != 0) {
            this.batchFees = newBatch.getBatchFees();
        }
        if (newBatch.getBatchDiscountType() != null) {
            this.batchDiscountType = newBatch.getBatchDiscountType();
        }
        if (newBatch.getBatchDiscountAmount() != 0) {
            this.batchDiscountAmount = newBatch.getBatchDiscountAmount();
        }
        if (newBatch.getBatchStartDate() != null) {
            this.batchStartDate = newBatch.getBatchStartDate();
        }
        if (newBatch.getBatchEndDate() != null) {
            this.batchEndDate = newBatch.getBatchEndDate();
        }
        if (newBatch.getBatchCustomJs() != null) {
            this.batchCustomJs = newBatch.getBatchCustomJs();
        }
        if (newBatch.getBatchSeoAllowComments() != null) {
            this.batchSeoAllowComments = newBatch.getBatchSeoAllowComments();
        }
        if (newBatch.getBatchSeoAllowRatings() != null) {
            this.batchSeoAllowRatings = newBatch.getBatchSeoAllowRatings();
        }
        if (newBatch.getBatchSeoTitleTag() != null) {
            this.batchSeoTitleTag = newBatch.getBatchSeoTitleTag();
        }
        if (newBatch.getBatchSeoMetaDescription() != null) {
            this.batchSeoMetaDescription = newBatch.getBatchSeoMetaDescription();
        }
        if (newBatch.getBatchSeoMetaKeywords() != null) {
            this.batchSeoMetaKeywords = newBatch.getBatchSeoMetaKeywords();
        }
        if (newBatch.getBatchIsDeleted() != null) {
            this.batchIsDeleted = newBatch.getBatchIsDeleted();
        }
    }

    

}
