package com.skolarli.lmsservice.models.db;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Where;

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
@Table(name="lessons")
@Where(clause = "lesson_is_deleted is null or lesson_is_deleted = false")
public class Lesson extends Tenantable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String lessonName;

    private String lessonDescription;

    @ManyToOne
    @JoinColumn(name="chapter_id")
    @JsonIgnoreProperties("chapterLessons")
    private Chapter chapter;

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

    private Boolean lessonIsDeleted;


    public void update(Lesson lesson) {
        if (lesson.getLessonName() != null && !lesson.getLessonName().isEmpty()) {
            this.setLessonName(lesson.getLessonName());
        }
        if (lesson.getLessonDescription() != null && !lesson.getLessonDescription().isEmpty()) {
            this.setLessonDescription(lesson.getLessonDescription());
        }
        if (lesson.getVideoId() != null && !lesson.getVideoId().isEmpty()) {
            this.setVideoId(lesson.getVideoId());
        }
        if (lesson.getVideoTitle() != null && !lesson.getVideoTitle().isEmpty()) {
            this.setVideoTitle(lesson.getVideoTitle());
        }
        if (lesson.getVideoDescription() != null && !lesson.getVideoDescription().isEmpty()) {
            this.setVideoDescription(lesson.getVideoDescription());
        }
        if (lesson.getVideoUrl() != null && !lesson.getVideoUrl().isEmpty()) {
            this.setVideoUrl(lesson.getVideoUrl());
        }
        if (lesson.getVideoThumbnailUrl() != null && !lesson.getVideoThumbnailUrl().isEmpty()) {
            this.setVideoThumbnailUrl(lesson.getVideoThumbnailUrl());
        }
        if (lesson.getVideoSize() != 0) {
            this.setVideoSize(lesson.getVideoSize());
        }
        if (lesson.getAllowDownload() != null) {
            this.setAllowDownload(lesson.getAllowDownload());
        }
        if (lesson.getVideoIsActive() != null && !lesson.getVideoIsActive().isEmpty()) {
            this.setVideoIsActive(lesson.getVideoIsActive());
        }
        if (lesson.getTextContent() != null && !lesson.getTextContent().isEmpty()) {
            this.setTextContent(lesson.getTextContent());
        }
        if (lesson.getTextTitle() != null && !lesson.getTextTitle().isEmpty()) {
            this.setTextTitle(lesson.getTextTitle());
        }
        if (lesson.getTextDescription() != null && !lesson.getTextDescription().isEmpty()) {
            this.setTextDescription(lesson.getTextDescription());
        }
        if (lesson.getTextUrl() != null && !lesson.getTextUrl().isEmpty()) {
            this.setTextUrl(lesson.getTextUrl());
        }
        if (lesson.getTextIsActive() != null) {
            this.setTextIsActive(lesson.getTextIsActive());
        }
        if (lesson.getPdfTitle() != null && !lesson.getPdfTitle().isEmpty()) {
            this.setPdfTitle(lesson.getPdfTitle());
        }
        if (lesson.getLessonIsDeleted() != null) {
            this.setLessonIsDeleted(lesson.getLessonIsDeleted());
        }
    }
}