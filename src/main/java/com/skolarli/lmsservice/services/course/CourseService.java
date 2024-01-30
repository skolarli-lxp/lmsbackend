package com.skolarli.lmsservice.services.course;

import com.skolarli.lmsservice.models.CourseStatus;
import com.skolarli.lmsservice.models.db.course.Course;

import java.util.List;

public interface CourseService {

    List<Course> getAllCourses();

    Course getCourseById(long id);

    Long getCourseCount(CourseStatus courseStatus);

    Course saveCourse(Course course);

    Course updateCourse(Course course, long id);

    void softDeleteCourse(long id);

    void hardDeleteCourse(long id);
}
