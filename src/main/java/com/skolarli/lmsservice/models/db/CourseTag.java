package com.skolarli.lmsservice.models.db;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Objects;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name="course_tags")
public class CourseTag extends Tenantable {

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

    /* 
    We consider two CourseTags as equal if the tag field is equal irrespective of other values
    Used to make sure that duplicate tags are not added while updating a course
    */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CourseTag courseTag = (CourseTag) o;
        return  Objects.equals(tag, courseTag.tag);
    }

}
