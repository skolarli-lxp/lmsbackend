package com.skolarli.lmsservice.models.db;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name="enrollment")
public class Enrollment extends Tenantable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "batch_id")
    @JsonIgnoreProperties("enrollments")
    private Batch batch;

    @ManyToOne
    @JoinColumn(name = "student_id")
    @JsonIgnoreProperties("enrollments")
    private LmsUser student;

    public Enrollment(Batch batch, LmsUser student) {
        this.batch = batch;
        this.student = student;
    }

    public void update(Enrollment enrollment) {
        if (enrollment.getBatch() != null) {
            this.batch = enrollment.getBatch();
        }
        if (enrollment.getStudent() != null) {
            this.student = enrollment.getStudent();
        }
    }
}

