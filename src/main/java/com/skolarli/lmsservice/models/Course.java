package com.skolarli.lmsservice.models;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name="course")
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name="course_name", nullable = false)
    private String name;

    private String instructor;
}
