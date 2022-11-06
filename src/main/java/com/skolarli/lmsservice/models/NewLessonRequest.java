package com.skolarli.lmsservice.models;

import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.skolarli.lmsservice.models.db.Lesson;
import com.skolarli.lmsservice.services.ChapterService;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class NewLessonRequest {
    @JsonIgnore
    @Autowired
    ChapterService chapterService;

    @NotNull(message = "lessonName is required")
    private String lessonName;

    private String lessonDescription;

    @NotNull(message = "chapterId is required")
    private long chapterId;

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

    public Lesson toLesson(){
        Lesson lesson = new Lesson();

        lesson.setLessonName(this.lessonName);
        lesson.setLessonDescription(this.lessonDescription);
        lesson.setChapter(this.chapterService.getChapterById(this.chapterId));

        // Lesson Video Related info
        lesson.setVideoId(this.videoId);
        lesson.setVideoTitle(this.videoTitle);
        lesson.setVideoDescription(this.videoDescription);
        lesson.setVideoUrl(this.videoUrl);
        lesson.setVideoThumbnailUrl(this.videoThumbnailUrl);
        lesson.setVideoSize(this.videoSize);
        lesson.setAllowDownload(this.allowDownload);
        lesson.setVideoIsActive(this.videoIsActive);
        
        // Lesson Text Related info
        lesson.setTextContent(this.textContent);
        lesson.setTextTitle(this.textTitle);
        lesson.setTextDescription(this.textDescription);
        lesson.setTextUrl(this.textUrl);
        lesson.setTextIsActive(this.textIsActive);

        // Lesson PDF Related info
        lesson.setPdfTitle(this.pdfTitle);
        lesson.setPdfDescription(this.pdfDescription);
        lesson.setPdfUrl(this.pdfUrl);
        lesson.setPdfSize(this.pdfSize);
        lesson.setPdfIsActive(this.pdfIsActive);

        // Lesson Audio Related info
        lesson.setAudioTitle(this.audioTitle);
        lesson.setAudioDescription(this.audioDescription);
        lesson.setAudioUrl(this.audioUrl);
        lesson.setAudioSize(this.audioSize);
        lesson.setAudioIsActive(this.audioIsActive);

        // Lesson Presentation Related info
        lesson.setPresentationTitle(this.presentationTitle);
        lesson.setPresentationDescription(this.presentationDescription);
        lesson.setPresentationUrl(this.presentationUrl);
        lesson.setPresentationSize(this.presentationSize);
        lesson.setPresentationIsActive(this.presentationIsActive);

        // Lesson Downloadables Related info
        lesson.setDownloadablesTitle(this.downloadablesTitle);
        lesson.setDownloadablesDescription(this.downloadablesDescription);
        lesson.setDownloadablesUrl(this.downloadablesUrl);
        lesson.setDownloadablesSize(this.downloadablesSize);
        lesson.setDownloadablesIsActive(this.downloadablesIsActive);

        return lesson;
    }
}