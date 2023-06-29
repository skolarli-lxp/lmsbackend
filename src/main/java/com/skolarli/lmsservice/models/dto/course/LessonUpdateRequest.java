package com.skolarli.lmsservice.models.dto.course;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Optional;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
public class LessonUpdateRequest {

    private Optional<String> lessonName;

    private Optional<String> lessonDescription;

    private Optional<Integer> lessonSortOrder;

    private Optional<Long> chapterId;

    // Lesson Video Related info
    private Optional<String> videoId;
    private Optional<String> videoTitle;

    private Optional<String> videoDescription;
    private Optional<String> videoUrl;
    private Optional<String> videoThumbnailUrl;
    private Optional<Long> videoSize;
    private Optional<Boolean> allowDownload;
    private Optional<String> videoIsActive;

    private Optional<String> textContent;
    private Optional<String> textTitle;
    private Optional<String> textDescription;
    private Optional<String> textUrl;

    private Optional<Boolean> textIsActive;

    // Lesson PDF Related info
    private Optional<String> pdfTitle;

    private Optional<String> pdfDescription;

    private Optional<String> pdfUrl;
    private Optional<Long> pdfSize;

    private Optional<Boolean> pdfIsActive;

    // Lesson Audio Related info

    private Optional<String> audioTitle;
    private Optional<String> audioDescription;
    private Optional<String> audioUrl;
    private Optional<Long> audioSize;

    private Optional<Boolean> audioIsActive;

    // Lesson Presentation Related info

    private Optional<String> presentationTitle;

    private Optional<String> presentationDescription;

    private Optional<String> presentationUrl;

    private Optional<Long> presentationSize;

    private Optional<Boolean> presentationIsActive;


    // Lesson Downloadables Related info

    private Optional<String> downloadablesTitle;

    private Optional<String> downloadablesDescription;

    private Optional<String> downloadablesUrl;

    private Optional<Long> downloadablesSize;

    private Optional<Boolean> downloadablesIsActive;

}
