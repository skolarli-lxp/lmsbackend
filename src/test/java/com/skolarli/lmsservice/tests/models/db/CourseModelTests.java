package com.skolarli.lmsservice.tests.models.db;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import com.skolarli.lmsservice.models.db.Batch;
import com.skolarli.lmsservice.models.db.Chapter;
import com.skolarli.lmsservice.models.db.Course;
import com.skolarli.lmsservice.models.db.DisountType;
import com.skolarli.lmsservice.models.db.DeliveryFormat;

@RunWith(MockitoJUnitRunner.class)
public class CourseModelTests {
    Course course;
    @BeforeEach
    public void setup(){
        course = new Course();
        course.setId(1L);
        course.setCourseName("Test Course");
        course.setCourseDescription("Test Course Description");
        course.setCourseFees(1000);
        course.setCourseDeliveryFormat(DeliveryFormat.PHYSICAL_CLASSROOM);
        course.setCourseDiscountAmount(0);

        Batch batchOne = new Batch();
        batchOne.setId(1L);
        batchOne.setCourse(new Course());
        List<Batch> batches = new ArrayList<>();
        batches.add(batchOne);
        course.setCourseBatches(batches);

    }

    @Test
    public void testUpdateCourseSuccess() {
        Course newCourse = new Course();
        newCourse.setCourseName("Updated Course Name");
        newCourse.setCourseDescription("Updated Course Description");
        newCourse.setCourseFees(5000);
        course.setCourseDeliveryFormat(DeliveryFormat.PHYSICAL_CLASSROOM);
        course.setCourseDiscountType(DisountType.NONE);

        Batch batchTwo = new Batch();
        batchTwo.setId(2L);
        batchTwo.setCourse(new Course());
        List<Batch> batches = new ArrayList<>();
        batches.add(batchTwo);
        newCourse.setCourseBatches(batches);

        Chapter chapterOne = new Chapter();
        chapterOne.setId(1L);
        chapterOne.setCourse(newCourse);
        List<Chapter> chapters = new ArrayList<>();
        chapters.add(chapterOne);
        newCourse.setCourseChapters(chapters);

        course.update(newCourse);


        assert(course.getCourseName().equals("Updated Course Name"));
        assert(course.getCourseDescription().equals("Updated Course Description"));
        assert(course.getCourseFees() == 5000);
        assert(course.getCourseDeliveryFormat().equals(DeliveryFormat.PHYSICAL_CLASSROOM));
        assert(course.getCourseDiscountType().equals(DisountType.NONE));
        assert(course.getCourseDiscountAmount() == 0);
        assert(course.getCourseBatches().size() == 2);
        assert(course.getCourseBatches().get(0).getId() == 1L);
        assert(course.getCourseBatches().get(1).getId() == 2L);
        assert(course.getCourseChapters().size() == 1);
        assert(course.getCourseChapters().get(0).getId() == 1L);
    }

    @Test
    public void testUpdateCourseSuccessWithId() {
        Course newCourse = new Course();
        newCourse.setId(5L);
        newCourse.setCourseName("Updated Course Name");
        newCourse.setCourseDescription("Updated Course Description");
        newCourse.setCourseFees(5000);
        course.setCourseDeliveryFormat(DeliveryFormat.PHYSICAL_CLASSROOM);
        course.setCourseDiscountType(DisountType.NONE);

        course.update(newCourse);
        assert(course.getId() == 1L);
    }

}
