package com.skolarli.lmsservice.models.db;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@NoArgsConstructor
@Table(name="course_tags")
public class CourseTag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name="course_id", insertable = false, updatable = false)
    @JsonIgnoreProperties("courseTagList")
    private Course course;

    private String tag;

    public CourseTag(String tag) {
        this.tag = tag;
    }


}
