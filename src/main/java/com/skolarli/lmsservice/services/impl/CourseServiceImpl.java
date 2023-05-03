package com.skolarli.lmsservice.services.impl;

import com.skolarli.lmsservice.exception.OperationNotSupportedException;
import com.skolarli.lmsservice.exception.ResourceNotFoundException;
import com.skolarli.lmsservice.models.db.Course;
import com.skolarli.lmsservice.models.db.LmsUser;
import com.skolarli.lmsservice.repository.CourseRepository;
import com.skolarli.lmsservice.services.CourseService;
import com.skolarli.lmsservice.utils.UserUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CourseServiceImpl implements CourseService {

    Logger logger = LoggerFactory.getLogger(CourseServiceImpl.class);
    private final CourseRepository courseRepository;

    private final UserUtils userUtils;

    public CourseServiceImpl(CourseRepository courseRepository, UserUtils userUtils) {
        super();
        this.courseRepository = courseRepository;
        this.userUtils = userUtils;
    }

    @Override
    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    @Override
    public Course getCourseById(long id) {
        List<Course> course = courseRepository.findAllById(new ArrayList<>(List.of(id)));

        if (course.isEmpty()) {
            throw new ResourceNotFoundException("Course", "Id", id);
        }
        return course.get(0);
    }

    @Override
    public Course saveCourse(Course course) {
        if (course.getCourseOwner() == null) {
            course.setCourseOwner(userUtils.getCurrentUser());
        }
        return courseRepository.save(course);
    }

    @Override
    public Course updateCourse(Course newCourse, long id) {
        LmsUser currentUser = userUtils.getCurrentUser();
        Course existingCourse = courseRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Course", "Id", id));

        if (!currentUser.getIsAdmin() && currentUser != existingCourse.getCourseOwner()) {
            throw new OperationNotSupportedException("User does not have permission to perform "
                    + "Update operation");
        }
        if (!currentUser.getIsAdmin() && newCourse.getCourseOwner() != null) {
            logger.error("Only admin can change course owner");
            newCourse.setCourseOwner(null);
        }

        // Update existing Course
        existingCourse.update(newCourse);
        return courseRepository.save(existingCourse);

    }

    @Override
    public void deleteCourse(long id) {
        LmsUser currentUser = userUtils.getCurrentUser();
        Course existingCourse = courseRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Course", "Id", id));
        if (!currentUser.getIsAdmin() && currentUser != existingCourse.getCourseOwner()) {
            throw new OperationNotSupportedException("User does not have permission to perform "
                    + "Delete operation");
        }
        existingCourse.setCourseDeleted(true);

        courseRepository.save(existingCourse);
    }

    @Override
    public void hardDeleteCourse(long id) {
        LmsUser currentUser = userUtils.getCurrentUser();
        Course existingCourse = courseRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Course", "Id", id));
        if (!currentUser.getIsAdmin() && currentUser != existingCourse.getCourseOwner()) {
            throw new OperationNotSupportedException("User does not have permission to perform "
                    + "Delete operation");
        }
        courseRepository.delete(existingCourse);
    }
}
