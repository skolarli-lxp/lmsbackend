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

import java.util.List;
import java.util.Optional;

@Service
public class CourseServiceImpl implements CourseService {
    // TODO : remove soft deleted courses from fetch 

    Logger logger = LoggerFactory.getLogger(CourseServiceImpl.class);
    private CourseRepository courseRepository;

    private final UserUtils userUtils;

    public CourseServiceImpl(CourseRepository courseRepository, UserUtils userUtils) {
        super();
        this.courseRepository = courseRepository;
        this.userUtils = userUtils;
    }

    @Override
    public Course saveCourse(Course course) {
        if (course.getCourseOwner() == null) {
            course.setCourseOwner(userUtils.getCurrentUser());
        }
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
    /**
     * Update exsiting course with content of course 
     * Update will proceed only if the current user is an admin user. 
     * Else throws OperationNotSupportedException 
     * 
     * @param course
     * 
     * @return updated course
     * @throws OperationNotSupportedException
     * @throws ResourceNotFoundException
     */
    public Course updateCourse(Course newCourse, long id) {
        LmsUser currentUser = userUtils.getCurrentUser();
        Course existingCourse = courseRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Course", "Id", id));

        if (currentUser.getIsAdmin() != true && currentUser != existingCourse.getCourseOwner()) {
            throw new OperationNotSupportedException("User does not have permission to perform Update operation");
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
            throw new OperationNotSupportedException("User does not have permission to perform Delete operation");
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
            throw new OperationNotSupportedException("User does not have permission to perform Delete operation");
        }
        courseRepository.delete(existingCourse);
    }
}
