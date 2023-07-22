package com.skolarli.lmsservice.services;

import com.skolarli.lmsservice.models.db.course.Course;

import java.util.List;

public interface CourseService {

    List<Course> getAllCourses();

    Course getCourseById(long id);

    Course saveCourse(Course course);

    Course updateCourse(Course course, long id);

    void softDeleteCourse(long id);

    void hardDeleteCourse(long id);
}
