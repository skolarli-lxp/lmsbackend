package com.skolarli.lmsservice.models.db;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Data
@Entity
@Table(name="batches")
public class Batch {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "course_id")
    @JsonIgnoreProperties("batches") // TO avoid infinite recursion during serialization
    private Course course;

    @ManyToOne
    @JoinColumn(name="instructor_id")
    @JsonIgnoreProperties("batches")
    private LmsUser instructor;

    @OneToMany
    @JsonIgnoreProperties("batch")
    private List<BatchSchedule> batchSchedules;
}
