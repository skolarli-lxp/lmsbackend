package com.skolarli.lmsservice.services;

import com.skolarli.lmsservice.models.db.Course;

import java.util.List;

public interface CourseService {
    Course saveCourse (Course course);
    List<Course> getAllCourses ();
    Course getCourseById(long id);
    Course updateCourse(Course course, long id);
    void deleteCourse(long id);
    void hardDeleteCourse(long id);
}
