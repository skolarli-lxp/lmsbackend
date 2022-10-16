package com.skolarli.lmsservice.models.db;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.ManyToAny;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name="chapters")
public class Chapter extends Tenantable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String chapterName;

    private String chapterDescription;

    @ManyToOne
    @JoinColumn(name="course_id")
    @JsonIgnoreProperties("chapters")
    private Course course;

    public void update(Chapter chapter) {
        if (chapter.getChapterName() != null && !chapter.getChapterName().isEmpty()) {
            this.setChapterName(chapter.getChapterName());
        }
        if (chapter.getChapterDescription() != null && !chapter.getChapterDescription().isEmpty()) {
            this.setChapterDescription(chapter.getChapterDescription());
        }
    }
}