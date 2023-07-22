package com.skolarli.lmsservice.models.db.course;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.skolarli.lmsservice.models.db.core.Tenantable;
import com.skolarli.lmsservice.models.dto.course.LessonSortOrderResponse;
import lombok.*;
import org.hibernate.annotations.Where;

import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.*;
import javax.validation.constraints.NotNull;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "lessons")
@Where(clause = "lesson_is_deleted is null or lesson_is_deleted = false")
public class Lesson extends Tenantable {

    // TODO Validate lesson sort order as positive

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String lessonName;

    @Column(columnDefinition = "TEXT")
    private String lessonDescription;

    @NotNull
    private int lessonSortOrder;

    @ManyToOne
    @JoinColumn(name = "chapter_id")
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    @JsonIdentityReference(alwaysAsId = true)
    private Chapter chapter;

    // Lesson Video Related info
    private String videoId;
    private String videoTitle;

    @Column(columnDefinition = "TEXT")
    private String videoDescription;
    @Column(columnDefinition = "VARCHAR(1024)")
    private String videoUrl;
    @Column(columnDefinition = "VARCHAR(1024)")
    private String videoThumbnailUrl;
    private long videoSize;
    private Boolean allowDownload;
    private String videoIsActive;

    // Lesson Text Related info

    @Column(columnDefinition = "TEXT")
    private String textContent;
    private String textTitle;
    @Column(columnDefinition = "TEXT")
    private String textDescription;
    @Column(columnDefinition = "VARCHAR(1024)")
    private String textUrl;
    private Boolean textIsActive;

    // Lesson PDF Related info
    private String pdfTitle;
    @Column(columnDefinition = "TEXT")
    private String pdfDescription;
    @Column(columnDefinition = "VARCHAR(1024)")
    private String pdfUrl;
    private long pdfSize;
    private Boolean pdfIsActive;

    // Lesson Audio Related info
    private String audioTitle;
    @Column(columnDefinition = "TEXT")
    private String audioDescription;
    @Column(columnDefinition = "VARCHAR(1024)")
    private String audioUrl;
    private long audioSize;
    private Boolean audioIsActive;

    // Lesson Presentation Related info
    private String presentationTitle;
    @Column(columnDefinition = "TEXT")
    private String presentationDescription;
    @Column(columnDefinition = "VARCHAR(1024)")
    private String presentationUrl;
    private long presentationSize;
    private Boolean presentationIsActive;

    // Lesson Downloadables Related info
    private String downloadablesTitle;
    @Column(columnDefinition = "TEXT")
    private String downloadablesDescription;
    @Column(columnDefinition = "VARCHAR(1024)")
    private String downloadablesUrl;
    private long downloadablesSize;
    private Boolean downloadablesIsActive;

    private Boolean lessonIsDeleted;

    public static List<LessonSortOrderResponse> toLessonSortOrderResponseList(
            List<Lesson> lessons) {
        return lessons.stream().map(lesson -> {
            return lesson.toLessonSortOrderResponse();
        }).collect(Collectors.toList());
    }

    public void update(Lesson lesson) {
        // Copy all non-null and non-empty values from the given object to this object

        if (lesson.getLessonName() != null && !lesson.getLessonName().isEmpty()) {
            this.setLessonName(lesson.getLessonName());
        }
        if (lesson.getLessonDescription() != null && !lesson.getLessonDescription().isEmpty()) {
            this.setLessonDescription(lesson.getLessonDescription());
        }
        if (lesson.getLessonSortOrder() != 0) {
            this.setLessonSortOrder(lesson.getLessonSortOrder());
        }
        //Video details update
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

        // Text details update
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

        // Audio details update

        if (lesson.getAudioTitle() != null && !lesson.getAudioTitle().isEmpty()) {
            this.setAudioTitle(lesson.getAudioTitle());
        }
        if (lesson.getAudioDescription() != null && !lesson.getAudioDescription().isEmpty()) {
            this.setAudioDescription(lesson.getAudioDescription());
        }
        if (lesson.getAudioUrl() != null && !lesson.getAudioUrl().isEmpty()) {
            this.setAudioUrl(lesson.getAudioUrl());
        }
        if (lesson.getAudioSize() != 0) {
            this.setAudioSize(lesson.getAudioSize());
        }
        if (lesson.getAudioIsActive() != null) {
            this.setAudioIsActive(lesson.getAudioIsActive());
        }

        // Presentation details update
        if (lesson.getPresentationTitle() != null && !lesson.getPresentationTitle().isEmpty()) {
            this.setPresentationTitle(lesson.getPresentationTitle());
        }
        if (lesson.getPresentationDescription() != null
                && !lesson.getPresentationDescription().isEmpty()) {
            this.setPresentationDescription(lesson.getPresentationDescription());
        }
        if (lesson.getPresentationUrl() != null && !lesson.getPresentationUrl().isEmpty()) {
            this.setPresentationUrl(lesson.getPresentationUrl());
        }
        if (lesson.getPresentationSize() != 0) {
            this.setPresentationSize(lesson.getPresentationSize());
        }
        if (lesson.getPresentationIsActive() != null) {
            this.setPresentationIsActive(lesson.getPresentationIsActive());
        }

        // Downloadables details update
        if (lesson.getDownloadablesTitle() != null && !lesson.getDownloadablesTitle().isEmpty()) {
            this.setDownloadablesTitle(lesson.getDownloadablesTitle());
        }
        if (lesson.getDownloadablesDescription() != null
                && !lesson.getDownloadablesDescription().isEmpty()) {
            this.setDownloadablesDescription(lesson.getDownloadablesDescription());
        }
        if (lesson.getDownloadablesUrl() != null && !lesson.getDownloadablesUrl().isEmpty()) {
            this.setDownloadablesUrl(lesson.getDownloadablesUrl());
        }
        if (lesson.getDownloadablesSize() != 0) {
            this.setDownloadablesSize(lesson.getDownloadablesSize());
        }
        if (lesson.getDownloadablesIsActive() != null) {
            this.setDownloadablesIsActive(lesson.getDownloadablesIsActive());
        }

        // PDF details update
        if (lesson.getPdfTitle() != null && !lesson.getPdfTitle().isEmpty()) {
            this.setPdfTitle(lesson.getPdfTitle());
        }
        if (lesson.getPdfDescription() != null && !lesson.getPdfDescription().isEmpty()) {
            this.setPdfDescription(lesson.getPdfDescription());
        }
        if (lesson.getPdfUrl() != null && !lesson.getPdfUrl().isEmpty()) {
            this.setPdfUrl(lesson.getPdfUrl());
        }
        if (lesson.getPdfSize() != 0) {
            this.setPdfSize(lesson.getPdfSize());
        }
        if (lesson.getPdfIsActive() != null) {
            this.setPdfIsActive(lesson.getPdfIsActive());
        }


        if (lesson.getLessonIsDeleted() != null) {
            this.setLessonIsDeleted(lesson.getLessonIsDeleted());
        }
    }

    public LessonSortOrderResponse toLessonSortOrderResponse() {
        LessonSortOrderResponse lessonSortOrderResponse = new LessonSortOrderResponse();
        lessonSortOrderResponse.setLessonId(this.getId());
        lessonSortOrderResponse.setLessonSortOrder(this.getLessonSortOrder());
        lessonSortOrderResponse.setLessonName(this.getLessonName());
        return lessonSortOrderResponse;
    }


}