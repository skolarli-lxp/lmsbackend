package com.skolarli.lmsservice.repository;

import com.skolarli.lmsservice.models.db.Course;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course, Long> {
}
