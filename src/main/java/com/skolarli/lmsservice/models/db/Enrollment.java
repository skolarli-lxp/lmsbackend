package com.skolarli.lmsservice.models.db;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import org.hibernate.annotations.Where;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name="enrollment", uniqueConstraints = @UniqueConstraint(name= "dupenrollment", columnNames = {"student_id", "batch_id"}))
@Where(clause = "enrollment_is_deleted is null or enrollment_is_deleted = false")
public class Enrollment extends Tenantable {
    private static final Logger logger = LoggerFactory.getLogger(Enrollment.class);

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "batch_id")
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    @JsonIdentityReference(alwaysAsId = true)
    private Batch batch;

    @ManyToOne
    @JoinColumn(name = "student_id")
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    @JsonIdentityReference(alwaysAsId = true)
    private LmsUser student;

    private Boolean enrollmentIsDeleted;    

    public void update(Enrollment enrollment) {
        if (enrollment.getId() != 0) {
            logger.error("Cannot update id");
        }
        if (enrollment.getBatch() != null) {
            this.batch = enrollment.getBatch();
        }
        if (enrollment.getStudent() != null) {
            this.student = enrollment.getStudent();
        }
        if (enrollment.getEnrollmentIsDeleted() != null) {
            this.enrollmentIsDeleted = enrollment.getEnrollmentIsDeleted();
        }
    }
}

