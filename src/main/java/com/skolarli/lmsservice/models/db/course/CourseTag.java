package com.skolarli.lmsservice.models.db.course;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.skolarli.lmsservice.models.db.core.Tenantable;
import lombok.*;

import java.util.Objects;
import javax.persistence.*;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "course_tags")
public class CourseTag extends Tenantable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "course_id", insertable = false, updatable = false)
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    @JsonIdentityReference(alwaysAsId = true)
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
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CourseTag courseTag = (CourseTag) o;
        return Objects.equals(tag, courseTag.tag);
    }

    @PreRemove
    private void removeCourseTags() {
        course.getCourseTagList().remove(this);
    }
}
