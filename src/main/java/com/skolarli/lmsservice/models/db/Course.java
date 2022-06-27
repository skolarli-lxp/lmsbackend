package com.skolarli.lmsservice.models.db;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "courses")
@AllArgsConstructor
public class Course extends Tenantable {

    public enum DiscountType {
        STUDENT,
        PROMOTION,
    }

    public enum CourseStatus {
        PLANNED,
        RUNNING,
        DEPRECATED
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull
    @Column(name = "course_name", nullable = false)
    private String name;

    private int courseFees;

    private DiscountType discountType;

    private int discountPercentage;

    private int discountAmount;

    //TODO: should not accept this in input json -- don't try to translate it
    @ManyToOne
    @JoinColumn(name = "owner_user_id")
    @JsonIgnoreProperties("courses") // TO avoid infinite recursion during serialization
    private LmsUser owner;

    private CourseStatus courseStatus;

    private String courseCategory;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "course_id")
    @JsonIgnoreProperties("course")
    private List<CourseTag> courseTagList = new ArrayList<>();

    public void update(Course course) {
        if (course.getName() != null && !course.getName().isEmpty()) {
            this.setName(course.getName());
        }
        if (course.getCourseFees() != 0) {
            this.setCourseFees(course.getCourseFees());
        }
        if (course.getDiscountType() != null) {
            this.setDiscountType(course.getDiscountType());
        }
        if (course.getDiscountPercentage() != 0) {
            this.setDiscountPercentage(course.getDiscountPercentage());
        }
        if (course.getDiscountAmount() != 0) {
            this.setDiscountAmount(course.getDiscountAmount());
        }
        if (course.getCourseStatus() != null) {
            this.setCourseStatus(course.getCourseStatus());
        }
        if (course.getCourseCategory() != null && !course.getCourseCategory().isEmpty()) {
            this.setCourseCategory(course.getCourseCategory());
        }
        if (!course.getCourseTagList().isEmpty()) {
            course.getCourseTagList().forEach(
                    (currentTag) -> {
                        if (!this.getCourseTagList().contains(currentTag)) {
                            this.getCourseTagList().add(currentTag);
                        }
                    });
        }
    }

}
