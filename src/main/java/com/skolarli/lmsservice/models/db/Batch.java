package com.skolarli.lmsservice.models.db;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.List;
import javax.persistence.*;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "batches")
@AllArgsConstructor
@NoArgsConstructor
@Where(clause = "batch_is_deleted is null or batch_is_deleted = false")
public class Batch extends Tenantable {
    public static final Logger logger = LoggerFactory.getLogger(Batch.class);

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "course_id")
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    @JsonIdentityReference(alwaysAsId = true)
    private Course course;

    @ManyToOne
    @JoinColumn(name = "instructor_id")
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    @JsonIdentityReference(alwaysAsId = true)
    private LmsUser instructor;

    @OneToMany(mappedBy = "batch")
    @JsonIgnoreProperties("batch")
    private List<BatchSchedule> batchSchedules;

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

    @Column(columnDefinition = "TEXT")
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
        if (newBatch.getBatchName() != null) {
            this.batchName = newBatch.getBatchName();
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
        if (newBatch.getBatchStatus() != null) {
            this.batchStatus = newBatch.getBatchStatus();
        }
        if (newBatch.getBatchDeliveryFormat() != null) {
            this.batchDeliveryFormat = newBatch.getBatchDeliveryFormat();
        }
        if (newBatch.getBatchDurationHours() != 0) {
            this.batchDurationHours = newBatch.getBatchDurationHours();
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
