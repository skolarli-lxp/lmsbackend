package com.skolarli.lmsservice.tests.services.impl;

import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import com.skolarli.lmsservice.models.db.Course;
import com.skolarli.lmsservice.repository.CourseRepository;
import com.skolarli.lmsservice.services.impl.CourseServiceImpl;

@RunWith(MockitoJUnitRunner.class)
public class CourseServiceImplTests {
    @Mock
    CourseRepository courseRepository;

    @InjectMocks
    CourseServiceImpl courseService;

    @Before
    void setup(){
        MockitoAnnotations.openMocks(this);

        Course newCourse = new Course();
        newCourse.setId(1L);
        newCourse.setCourseName("Test Course");

    }

    @Test
    public void testSaveCourseSuccess(){
        
    }
    
}