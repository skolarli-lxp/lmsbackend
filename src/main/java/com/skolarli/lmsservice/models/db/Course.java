package com.skolarli.lmsservice.models.db;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
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
@NoArgsConstructor
public class Course extends Tenantable {

    public enum DiscountType {
        NONE,
        PERCENTAGE,
        FIXED
    }

    public enum DeliveryFormat {
        PHYSICAL_CLASSROOM,
        VIRTUAL_CLASSROOM,
        ONLINE_INSTRUCTOR_LED,
        SELF_PACED,
        MIXED_DELIVERY
    }

    public enum CourseStatus {
        PLANNED,
        SCHEDULED,
        RUNNING,
        DEPRECATED
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull
    @Column(name = "course_name", nullable = false)
    private String name;

    @NotNull
    private String courseDescription;

    private int courseFees;

    private DiscountType discountType;
    private int discountAmount;

    //TODO: should not accept this in input json -- don't try to translate it
    @ManyToOne
    @JoinColumn(name = "owner_user_id")
    @JsonIgnoreProperties("courses") // TO avoid infinite recursion during serialization
    private LmsUser owner;

    private CourseStatus courseStatus;

    private String courseCategory;

    private DeliveryFormat deliveryFormat;

    private Boolean isPrivateCourse;

    private String customJs;

    private Boolean seoAllowComments;

    private Boolean seoAllowRatings;

    private String seoTitleTag;

    private  String seoDescription;

    private String metaTagKeywords;

    private String courseCoverImage;

    private String courseThumbImage;


    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "course_id")
    @JsonIgnoreProperties("course")
    private List<CourseTag> courseTagList = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name="course_id")
    @JsonIgnoreProperties("course")
    private List<Batch> batches = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name="course_id")
    @JsonIgnoreProperties("course")
    private List<Chapter> chapters = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name="course_id")
    @JsonIgnoreProperties("course")
    private List<Lesson> lessons = new ArrayList<>();

    public void update(Course course) {
        if (course.getName() != null && !course.getName().isEmpty()) {
            this.setName(course.getName());
        }
        if (course.getCourseDescription() != null && !course.getCourseDescription().isEmpty()) {
            this.setCourseDescription(course.getCourseDescription());
        }
        if (course.getCourseFees() != 0) {
            this.setCourseFees(course.getCourseFees());
        }
        if (course.getDiscountType() != null) {
            this.setDiscountType(course.getDiscountType());
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
        if (course.getDeliveryFormat() != null) {
            this.setDeliveryFormat(course.getDeliveryFormat());
        }
        if (course.getIsPrivateCourse() != null) {
            this.setIsPrivateCourse(course.getIsPrivateCourse());
        }
        if (course.getCustomJs() != null && !course.getCustomJs().isEmpty()) {
            this.setCustomJs(course.getCustomJs());
        }
        if (course.getSeoAllowComments() != null) {
            this.setSeoAllowComments(course.getSeoAllowComments());
        }
        if (course.getSeoAllowRatings() != null) {
            this.setSeoAllowRatings(course.getSeoAllowRatings());
        }
        if (course.getSeoTitleTag() != null && !course.getSeoTitleTag().isEmpty()) {
            this.setSeoTitleTag(course.getSeoTitleTag());
        }
        if (course.getSeoDescription() != null && !course.getSeoDescription().isEmpty()) {
            this.setSeoDescription(course.getSeoDescription());
        }
        if (course.getMetaTagKeywords() != null && !course.getMetaTagKeywords().isEmpty()) {
            this.setMetaTagKeywords(course.getMetaTagKeywords());
        }
        if (course.getCourseCoverImage() != null && !course.getCourseCoverImage().isEmpty()) {
            this.setCourseCoverImage(course.getCourseCoverImage());
        }
        if (course.getCourseThumbImage() != null && !course.getCourseThumbImage().isEmpty()) {
            this.setCourseThumbImage(course.getCourseThumbImage());
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
