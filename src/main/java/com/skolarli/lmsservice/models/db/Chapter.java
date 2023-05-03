package com.skolarli.lmsservice.models.db;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Where;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.skolarli.lmsservice.models.ChapterSortOrderResponse;

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
@Table(name = "chapters")
@Where(clause = "chapter_is_deleted is null or chapter_is_deleted = false")
public class Chapter extends Tenantable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String chapterName;

    @Column(columnDefinition = "TEXT")
    private String chapterDescription;

    @ManyToOne
    @JoinColumn(name = "course_id")
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    @JsonIdentityReference(alwaysAsId = true)
    private Course course;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "chapter_id")
    @JsonIgnoreProperties("chapter")
    private List<Lesson> chapterLessons = new ArrayList<>();

    private Boolean chapterIsDeleted;

    @NotNull
    private int chapterSortOrder;

    public void update(Chapter chapter) {
        if (chapter.getChapterName() != null && !chapter.getChapterName().isEmpty()) {
            this.setChapterName(chapter.getChapterName());
        }
        if (chapter.getChapterDescription() != null &&
                !chapter.getChapterDescription().isEmpty()) {
            this.setChapterDescription(chapter.getChapterDescription());
        }

        if (chapter.getCourse() != null && !chapter.getCourse().equals(this.getCourse())) {
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
        if (chapter.getChapterIsDeleted() != null) {
            this.setChapterIsDeleted(chapter.getChapterIsDeleted());
        }
        if (chapter.getChapterSortOrder() != 0) {
            this.setChapterSortOrder(chapter.getChapterSortOrder());
        }
    }

    public ChapterSortOrderResponse toChapterSortOrderResponse() {
        return new ChapterSortOrderResponse(
                this.getId(),
                this.getChapterName(),
                this.getChapterSortOrder());
    }

    public static List<ChapterSortOrderResponse> toChapterSortOrderResponseList(
            List<Chapter> chapters) {
        return chapters.stream().map((chapter) ->
                chapter.toChapterSortOrderResponse()
        ).collect(Collectors.toList());
    }
}