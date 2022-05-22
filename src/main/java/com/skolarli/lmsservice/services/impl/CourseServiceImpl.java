package com.skolarli.lmsservice.services.impl;

import com.skolarli.lmsservice.exception.ResourceNotFoundException;
import com.skolarli.lmsservice.models.db.Course;
import com.skolarli.lmsservice.repository.CourseRepository;
import com.skolarli.lmsservice.services.CourseService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CourseServiceImpl implements CourseService {

    private CourseRepository courseRepository;

    public CourseServiceImpl(CourseRepository courseRepository) {
        super();
        this.courseRepository = courseRepository;
    }

    @Override
    public Course saveCourse(Course course) {
        return courseRepository.save(course);
    }

    @Override
    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    @Override
    public Course getCourseById(long id) {
        Optional<Course> course = courseRepository.findById(id);
        if (course.isPresent()) {
            return course.get();
        }
        else {
            throw new ResourceNotFoundException("Course","Id", id);
        }
    }

    @Override
    public Course updateCourse(Course course, long id) {
        // Check if course exists, else throw exception
        Course existingCourse = courseRepository.findById(id).orElseThrow(
                                          () -> new ResourceNotFoundException("Course", "Id", id));
        // Update existing Course
        existingCourse.setName(course.getName());
        existingCourse.setInstructor(course.getInstructor());
        courseRepository.save(existingCourse);
        return existingCourse;

    }

    @Override
    public void deleteCourse(long id) {
        Course existingCourse = courseRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Course", "Id", id));
        courseRepository.delete(existingCourse);
    }


}
