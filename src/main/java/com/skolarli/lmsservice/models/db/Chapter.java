package com.skolarli.lmsservice.models.db;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.skolarli.lmsservice.models.dto.course.ChapterSortOrderResponse;
import com.skolarli.lmsservice.models.dto.course.NewChapterRequest;
import lombok.*;
import org.hibernate.annotations.Where;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.*;
import javax.validation.constraints.NotNull;

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
        if (chapter.getChapterDescription() != null
                && !chapter.getChapterDescription().isEmpty()) {
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

    public void update(NewChapterRequest newChapterRequest) {
        if (newChapterRequest.getChapterName() != null
                && !newChapterRequest.getChapterName().isEmpty()) {
            this.setChapterName(newChapterRequest.getChapterName());
        }
        if (newChapterRequest.getChapterDescription() != null
                && !newChapterRequest.getChapterDescription().isEmpty()) {
            this.setChapterDescription(newChapterRequest.getChapterDescription());
        }

        if (newChapterRequest.getChapterSortOrder() != 0) {
            this.setChapterSortOrder(newChapterRequest.getChapterSortOrder());
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
        return chapters.stream().map(Chapter::toChapterSortOrderResponse)
                .collect(Collectors.toList());
    }
}