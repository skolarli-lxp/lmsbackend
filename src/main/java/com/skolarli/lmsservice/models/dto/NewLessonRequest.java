package com.skolarli.lmsservice.models.dto;

import lombok.*;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class NewLessonRequest {
    @NotNull(message = "lessonName is required")
    private String lessonName;

    private String lessonDescription;

    @NotNull(message = "chapterId is required")
    private long chapterId;

    private int lessonSortOrder;

    // Lesson Video Related info
    private String videoId;
    private String videoTitle;
    private String videoDescription;
    private String videoUrl;
    private String videoThumbnailUrl;
    private long videoSize;
    private Boolean allowDownload;
    private String videoIsActive;

    // Lesson Text Related info
    private String textContent;
    private String textTitle;
    private String textDescription;
    private String textUrl;
    private Boolean textIsActive;

    // Lesson PDF Related info
    private String pdfTitle;
    private String pdfDescription;
    private String pdfUrl;
    private long pdfSize;
    private Boolean pdfIsActive;

    // Lesson Audio Related info
    private String audioTitle;
    private String audioDescription;
    private String audioUrl;
    private long audioSize;
    private Boolean audioIsActive;

    // Lesson Presentation Related info
    private String presentationTitle;
    private String presentationDescription;
    private String presentationUrl;
    private long presentationSize;
    private Boolean presentationIsActive;

    // Lesson Downloadables Related info
    private String downloadablesTitle;
    private String downloadablesDescription;
    private String downloadablesUrl;
    private long downloadablesSize;
    private Boolean downloadablesIsActive;

}