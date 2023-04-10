package com.skolarli.lmsservice.unittests.services.impl;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.skolarli.lmsservice.exception.OperationNotSupportedException;
import com.skolarli.lmsservice.exception.ResourceNotFoundException;
import com.skolarli.lmsservice.models.db.LmsUser;
import com.skolarli.lmsservice.utils.UserUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.skolarli.lmsservice.models.db.Course;
import com.skolarli.lmsservice.models.db.DeliveryFormat;
import com.skolarli.lmsservice.models.db.DisountType;
import com.skolarli.lmsservice.repository.CourseRepository;
import com.skolarli.lmsservice.services.impl.CourseServiceImpl;

@ExtendWith(MockitoExtension.class)
public class CourseServiceImplTests {
    @Mock
    private CourseRepository courseRepository;

    @InjectMocks
    private CourseServiceImpl courseService;

    @Mock
    private UserUtils userUtils;

    Course newCourse;

    @BeforeEach
    void setup(){
        newCourse = new Course();
        newCourse.setId(1L);
        newCourse.setCourseName("Test Course");
        newCourse.setCourseDescription("Test Course Description");
        newCourse.setCourseFees(1000);
        newCourse.setCourseDeliveryFormat(DeliveryFormat.PHYSICAL_CLASSROOM);
        newCourse.setCourseDiscountAmount(0);
        newCourse.setCourseDiscountType(DisountType.NONE);
    }

    @Test
    public void testSaveCourseSuccess(){
        when(courseRepository.save(newCourse)).thenReturn(newCourse);
        Course savedCourse = courseService.saveCourse(newCourse);

        verify(courseRepository).save(newCourse);
        assertEquals(savedCourse.getId(), 1L);
    }

    @Test
    public void testSaveCourseFailure() throws Exception {
        when(courseRepository.save(any())).thenThrow(new IllegalArgumentException());
        assertThrows(IllegalArgumentException.class, () -> {
            courseService.saveCourse(newCourse);
        });

        verify(courseRepository).save(newCourse);
    }

    @Test
    public void testUpdateCourseSuccess() {
        LmsUser currentUser = new LmsUser();
        currentUser.setId(1);
        currentUser.setIsAdmin(true);
        Course updatedCourse = new Course();
        updatedCourse.setId(100L);
        updatedCourse.setCourseName("Updated Course Name");
        updatedCourse.setCourseDescription("Updated Course Description");
        updatedCourse.setCourseDeliveryFormat(DeliveryFormat.PHYSICAL_CLASSROOM);
        updatedCourse.setCourseDiscountType(DisountType.NONE);

        when(courseRepository.findById(1L)).thenReturn(Optional.of(newCourse));
        when(courseRepository.save(newCourse)).thenReturn(newCourse);
        when(userUtils.getCurrentUser()).thenReturn(currentUser);

        Course savedCourse = courseService.updateCourse(updatedCourse, 1);

        verify(courseRepository).save(updatedCourse);
        assertEquals(savedCourse.getId(), 1L);
        assertEquals(savedCourse.getCourseName(), "Updated Course Name");
        assertEquals(savedCourse.getCourseFees(), 1000);
    }

    @Test
    public void testUpdateCourseFailure() throws Exception {
        LmsUser currentUser = new LmsUser();
        currentUser.setId(1);
        currentUser.setIsAdmin(false);

        Course updatedCourse = new Course();
        updatedCourse.setId(100L);
        updatedCourse.setCourseName("Updated Course Name");
        updatedCourse.setCourseDescription("Updated Course Description");
        updatedCourse.setCourseDeliveryFormat(DeliveryFormat.PHYSICAL_CLASSROOM);
        updatedCourse.setCourseDiscountType(DisountType.NONE);

        when(userUtils.getCurrentUser()).thenReturn(currentUser);
        when(courseRepository.findById(1L)).thenReturn(Optional.of(newCourse));

        assertThrows(OperationNotSupportedException.class, () -> {
            courseService.updateCourse(updatedCourse, 1);
        });
    }

    @Test
    public void testDeleteCourseSuccess() {
        LmsUser currentUser = new LmsUser();
        currentUser.setId(1);
        currentUser.setIsAdmin(true);


        when(courseRepository.findById(1L)).thenReturn(Optional.of(newCourse));
        when(userUtils.getCurrentUser()).thenReturn(currentUser);

        courseService.deleteCourse(1);
    }

    @Test
    public void testDeleteCourseFailure() throws Exception {
        LmsUser currentUser = new LmsUser();
        currentUser.setId(1);
        currentUser.setIsAdmin(false);

        when(userUtils.getCurrentUser()).thenReturn(currentUser);
        when(courseRepository.findById(1L)).thenReturn(Optional.of(newCourse));

        assertThrows(OperationNotSupportedException.class, () -> {
            courseService.deleteCourse(1);
        });
    }

    @Test
    public void testGetCourseByIdSuccess() {
        when(courseRepository.findAllById(List.of(1L))).thenReturn(List.of(newCourse));

        Course course = courseService.getCourseById(1);

        verify(courseRepository).findAllById(List.of(1L));
        assertEquals(course.getId(), 1L);
    }

    @Test
    public void testGetCourseByIdFailure() throws Exception {
        when(courseRepository.findAllById(List.of(1L))).thenReturn(new ArrayList<>());

        assertThrows(ResourceNotFoundException.class, () -> {
            courseService.getCourseById(1);
        });
    }

    @Test
    public void testGetAllCoursesSuccess() {
        List<Course> courseList = new ArrayList<>();
        courseList.add(newCourse);
        when(courseRepository.findAll()).thenReturn(courseList);

        courseService.getAllCourses();
        verify(courseRepository).findAll();
        assertEquals(courseList.get(0).getId(), 1L);
    }

    @Test
    public void testGetAllCoursesFailure() throws Exception {
        when(courseRepository.findAll()).thenThrow(new IllegalArgumentException());

        assertThrows(IllegalArgumentException.class, () -> {
            courseService.getAllCourses();
        });
    }
    
}

