package com.skolarli.lmsservice.models.db;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name="batches")
@AllArgsConstructor
@NoArgsConstructor
public class Batch extends  Tenantable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "course_id")
    @JsonIgnoreProperties("courseBatches") // TO avoid infinite recursion during serialization
    private Course course;

    @ManyToOne
    @JoinColumn(name="instructor_id")
    @JsonIgnoreProperties("batches")
    private LmsUser instructor;

    @OneToMany(mappedBy = "batch")
    @JsonIgnoreProperties("batch")
    private List<BatchSchedule> batchSchedules;

    private int enrollmentCapacity;

    private String additionalInfo;

    private long batchFees;





    public void updateBatch(Batch newBatch) {
    }
}
