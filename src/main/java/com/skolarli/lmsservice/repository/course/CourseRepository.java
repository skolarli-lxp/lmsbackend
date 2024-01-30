package com.skolarli.lmsservice.repository.course;

import com.skolarli.lmsservice.models.CourseStatus;
import com.skolarli.lmsservice.models.db.course.Course;
import com.skolarli.lmsservice.repository.core.TenantableRepository;
import org.springframework.data.jpa.repository.Query;


public interface CourseRepository extends TenantableRepository<Course> {

    //Do not use count() method, since it will not filter for tenants
    @Query(value = "SELECT count(*) FROM Course")
    long findCourseCount();

    @Query(value = "SELECT count(*) FROM Course WHERE courseStatus = ?1")
    long findCourseCountByCourseStatus(CourseStatus courseStatus);
}
