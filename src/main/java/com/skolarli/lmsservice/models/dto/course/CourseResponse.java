package com.skolarli.lmsservice.models.dto.course;

import com.skolarli.lmsservice.models.CourseStatus;
import com.skolarli.lmsservice.models.DeliveryFormat;
import com.skolarli.lmsservice.models.DiscountType;
import com.skolarli.lmsservice.models.db.course.Course;
import com.skolarli.lmsservice.models.db.course.CourseTag;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CourseResponse {
    private long id;

    private String courseName;

    private String courseDescription;

    private int courseFees;

    private DiscountType courseDiscountType;

    private int courseDiscountAmount;

    private Long courseOwnerId;

    private CourseStatus courseStatus;

    private String courseCategory;

    private DeliveryFormat courseDeliveryFormat;

    private Boolean courseIsPrivate;

    private String courseCustomJs;

    private Boolean courseSeoAllowComments;

    private Boolean courseSeoAllowRatings;

    private String courseSeoTitleTag;

    private String courseSeoDescription;

    private String courseMetaTagKeywords;

    private String courseCoverImage;

    private String courseThumbImage;

    private Boolean courseDeleted;

    private List<CourseTag> courseTagList = new ArrayList<>();

    public CourseResponse(Course course) {
        this.id = course.getId();
        this.courseName = course.getCourseName();
        this.courseDescription = course.getCourseDescription();
        this.courseFees = course.getCourseFees();
        this.courseDiscountType = course.getCourseDiscountType();
        this.courseDiscountAmount = course.getCourseDiscountAmount();
        if (course.getCourseOwner() != null) {
            this.courseOwnerId = course.getCourseOwner().getId();
        }
        this.courseStatus = course.getCourseStatus();
        this.courseCategory = course.getCourseCategory();
        this.courseDeliveryFormat = course.getCourseDeliveryFormat();
        this.courseIsPrivate = course.getCourseIsPrivate();
        this.courseCustomJs = course.getCourseCustomJs();
        this.courseSeoAllowComments = course.getCourseSeoAllowComments();
        this.courseSeoAllowRatings = course.getCourseSeoAllowRatings();
        this.courseSeoTitleTag = course.getCourseSeoTitleTag();
        this.courseSeoDescription = course.getCourseSeoDescription();
        this.courseMetaTagKeywords = course.getCourseMetaTagKeywords();
        this.courseCoverImage = course.getCourseCoverImage();
        this.courseThumbImage = course.getCourseThumbImage();
        this.courseDeleted = course.getCourseDeleted();
        this.courseTagList = course.getCourseTagList();
    }
}