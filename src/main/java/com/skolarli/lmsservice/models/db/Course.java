package com.skolarli.lmsservice.models.db;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "courses")
@AllArgsConstructor
@NoArgsConstructor
public class Course extends Tenantable {
    // Has to be static to avoid persisting the logger
    private static final Logger logger = LoggerFactory.getLogger(Course.class);

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
    private String courseName;

    @NotNull
    private String courseDescription;

    private int courseFees;

    private DiscountType courseDiscountType;
    private int courseDiscountAmount;

    //TODO: should not accept this in input json -- don't try to translate it
    @ManyToOne
    @JoinColumn(name = "owner_user_id")
    @JsonIgnoreProperties("courses") // TO avoid infinite recursion during serialization
    private LmsUser courseOwner;

    private CourseStatus courseStatus;

    private String courseCategory;

    private DeliveryFormat courseDeliveryFormat;

    private Boolean courseIsPrivate;

    private String courseCustomJs;

    private Boolean courseSeoAllowComments;

    private Boolean courseSeoAllowRatings;

    private String courseSeoTitleTag;

    private  String courseSeoDescription;

    private String courseMetaTagKeywords;

    private String courseCoverImage;

    private String courseThumbImage;


    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "course_id")
    @JsonIgnoreProperties("course")
    private List<CourseTag> courseTagList = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name="course_id")
    @JsonIgnoreProperties("course")
    private List<Batch> courseBatches = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name="course_id")
    @JsonIgnoreProperties("course")
    private List<Chapter> courseChapters = new ArrayList<>();

    /**
     * Updates the course with the values in the passed object
     * All fields except for Id will be updated with the passed value if it is not null
     * This method does not care about permissions. It is assumed that the caller has already checked for permissions.
     * Permission should be handled in the service layer.
     * List of items like batches, lesson etc are only updated with additional values. Existing values are not deleted.
     * 
     * @param course
     */
    public void update(Course course) {
        if (course.getId() != 0) {
            logger.error("Course ID is auto generated. Cannot update.");
        }
        if (course.getCourseName() != null && !course.getCourseName().isEmpty()) {
            this.setCourseName(course.getCourseName());
        }
        if (course.getCourseDescription() != null && !course.getCourseDescription().isEmpty()) {
            this.setCourseDescription(course.getCourseDescription());
        }
        if (course.getCourseFees() != 0) {
            this.setCourseFees(course.getCourseFees());
        }
        if (course.getCourseDiscountType() != null) {
            this.setCourseDiscountType(course.getCourseDiscountType());
        }
        if (course.getCourseDiscountAmount() != 0) {
            this.setCourseDiscountAmount(course.getCourseDiscountAmount());
        }
        if (course.getCourseStatus() != null) {
            this.setCourseStatus(course.getCourseStatus());
        }
        if (course.getCourseCategory() != null && !course.getCourseCategory().isEmpty()) {
            this.setCourseCategory(course.getCourseCategory());
        }
        if (course.getCourseDeliveryFormat() != null) {
            this.setCourseDeliveryFormat(course.getCourseDeliveryFormat());
        }
        if (course.getCourseIsPrivate() != null) {
            this.setCourseIsPrivate(course.getCourseIsPrivate());
        }
        if (course.getCourseCustomJs() != null && !course.getCourseCustomJs().isEmpty()) {
            this.setCourseCustomJs(course.getCourseCustomJs());
        }
        if (course.getCourseSeoAllowComments() != null) {
            this.setCourseSeoAllowComments(course.getCourseSeoAllowComments());
        }
        if (course.getCourseSeoAllowRatings() != null) {
            this.setCourseSeoAllowRatings(course.getCourseSeoAllowRatings());
        }
        if (course.getCourseSeoTitleTag() != null && !course.getCourseSeoTitleTag().isEmpty()) {
            this.setCourseSeoTitleTag(course.getCourseSeoTitleTag());
        }
        if (course.getCourseSeoDescription() != null && !course.getCourseSeoDescription().isEmpty()) {
            this.setCourseSeoDescription(course.getCourseSeoDescription());
        }
        if (course.getCourseMetaTagKeywords() != null && !course.getCourseMetaTagKeywords().isEmpty()) {
            this.setCourseMetaTagKeywords(course.getCourseMetaTagKeywords());
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

        if (!course.getCourseBatches().isEmpty()) {
            course.getCourseBatches().forEach(
                    (currentBatch) -> {
                        if (!this.getCourseBatches().contains(currentBatch)) {
                            this.getCourseBatches().add(currentBatch);
                        }
                    });
        }

        if (!course.getCourseChapters().isEmpty()) {
            course.getCourseChapters().forEach(
                    (currentChapter) -> {
                        if (!this.getCourseChapters().contains(currentChapter)) {
                            this.getCourseChapters().add(currentChapter);
                        }
                    });
        }
    }

}
