package com.skolarli.lmsservice.models.db;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name="courses")
public class Course extends Tenantable {
    enum DiscountType {
        STUDENT,
        PROMOTION,
    }

    enum CourseStatus {
        PLANNED,
        RUNNING,
        DEPRECATED
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull
    @Column(name="course_name", nullable = false)
    private String name;

    private int courseFees;

    private DiscountType discountType;

    private int discountPercentage;

    private int discountAmount;

    @ManyToOne
    @JoinColumn(name = "owner_user_id")
    @JsonIgnoreProperties("courses") // TO avoid infinite recursion during serialization
    private LmsUser owner;

    private CourseStatus courseStatus;

    private String courseCategory;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name="course_id")
    @JsonIgnoreProperties("course")
    private List<CourseTag> courseTagList;

}
