package com.skolarli.lmsservice.models.db;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@EqualsAndHashCode(callSuper = true)
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
    @JsonIgnoreProperties("courseChapters")
    private Course course;
    
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name="chapter_id")
    @JsonIgnoreProperties("chapter")
    private List<Lesson> chapterLessons = new ArrayList<>();

    
    public void update(Chapter chapter) {
        if (chapter.getChapterName() != null && !chapter.getChapterName().isEmpty()) {
            this.setChapterName(chapter.getChapterName());
        }
        if (chapter.getChapterDescription() != null && !chapter.getChapterDescription().isEmpty()) {
            this.setChapterDescription(chapter.getChapterDescription());
        }

        if (chapter.getCourse()!=null &&  !chapter.getCourse().equals(this.getCourse())) {
            this.setCourse(chapter.getCourse());
        }
        
        if (!chapter.getChapterLessons().isEmpty()) {
            chapter.getChapterLessons().forEach(
                    (currentLesson) -> {
                        if (!this.getChapterLessons().contains(currentLesson)) {
                            this.getChapterLessons().add(currentLesson);
                        }
                    });
        }
    }
}