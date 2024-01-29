package com.skolarli.lmsservice.models.dto.course;

import com.skolarli.lmsservice.models.db.core.Tenantable;
import com.skolarli.lmsservice.models.db.course.Chapter;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ChapterResponse extends Tenantable {
    private long id;

    private String chapterName;

    private String chapterDescription;

    private Long courseId;

    private Boolean chapterIsDeleted;

    private int chapterSortOrder;

    public ChapterResponse(Chapter chapter) {
        this.id = chapter.getId();
        this.chapterName = chapter.getChapterName();
        this.chapterDescription = chapter.getChapterDescription();
        if (chapter.getCourse() != null) {
            this.courseId = chapter.getCourse().getId();
        }
        this.chapterIsDeleted = chapter.getChapterIsDeleted();
        this.chapterSortOrder = chapter.getChapterSortOrder();
    }
}