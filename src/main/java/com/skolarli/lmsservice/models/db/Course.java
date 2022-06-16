package com.skolarli.lmsservice.models.db;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name="courses")
public class Course extends Tenantable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name="course_name", nullable = false)
    private String name;

    private String instructor;

}
