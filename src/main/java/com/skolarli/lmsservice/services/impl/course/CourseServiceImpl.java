package com.skolarli.lmsservice.services.impl.course;

import com.skolarli.lmsservice.exception.OperationNotSupportedException;
import com.skolarli.lmsservice.exception.ResourceNotFoundException;
import com.skolarli.lmsservice.models.db.core.LmsUser;
import com.skolarli.lmsservice.models.db.course.Course;
import com.skolarli.lmsservice.models.db.course.CourseTag;
import com.skolarli.lmsservice.repository.course.CourseRepository;
import com.skolarli.lmsservice.repository.course.CourseTagRepository;
import com.skolarli.lmsservice.services.course.CourseService;
import com.skolarli.lmsservice.utils.UserUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CourseServiceImpl implements CourseService {

    final Logger logger = LoggerFactory.getLogger(CourseServiceImpl.class);
    private final CourseRepository courseRepository;
    private final CourseTagRepository courseTagRepository;

    private final UserUtils userUtils;

    public CourseServiceImpl(CourseRepository courseRepository, UserUtils userUtils,
                             CourseTagRepository courseTagRepository) {
        super();
        this.courseRepository = courseRepository;
        this.userUtils = userUtils;
        this.courseTagRepository = courseTagRepository;
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

    private void canDelete(Course existingCourse) {
        LmsUser currentUser = userUtils.getCurrentUser();
        if (!currentUser.getIsAdmin() && currentUser != existingCourse.getCourseOwner()) {
            throw new OperationNotSupportedException("User does not have permission to perform "
                    + "Delete operation");
        }
        if (existingCourse.getCourseBatches() != null
                && !existingCourse.getCourseBatches().isEmpty()) {
            throw new OperationNotSupportedException("Course cannot be deleted as it has batches "
                    + "associated with it");
        }
        if (existingCourse.getCourseChapters() != null
                && !existingCourse.getCourseChapters().isEmpty()) {
            throw new OperationNotSupportedException("Course cannot be deleted as it has chapters "
                    + "associated with it");
        }
    }

    private void deleteCourseTags(Course existingCourse) {
        if (existingCourse.getCourseTagList() != null
                && !existingCourse.getCourseTagList().isEmpty()) {

            List<CourseTag> courseTagList = existingCourse.getCourseTagList();
            List<CourseTag> courseTagListCopy = new ArrayList<>(courseTagList);

            // @Preremove will not work for deleteAll
            while (!courseTagList.isEmpty()) {
                courseTagList.remove(0);
            }
            courseTagRepository.deleteAll(courseTagListCopy);
        }
    }

    @Override
    public void softDeleteCourse(long id) {
        Course existingCourse = courseRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Course", "Id", id));
        try {
            canDelete(existingCourse);
        } catch (OperationNotSupportedException e) {
            logger.error("Error while deleting course", e);
            throw e;
        }

        deleteCourseTags(existingCourse);
        existingCourse.setCourseDeleted(true);
        courseRepository.save(existingCourse);
    }

    @Override
    public void hardDeleteCourse(long id) {
        Course existingCourse = courseRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Course", "Id", id));
        try {
            canDelete(existingCourse);
        } catch (OperationNotSupportedException e) {
            logger.error("Error while deleting course", e);
            throw e;
        }
        deleteCourseTags(existingCourse);
        courseRepository.delete(existingCourse);
    }
}
