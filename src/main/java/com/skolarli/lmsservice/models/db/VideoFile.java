package com.skolarli.lmsservice.models.db;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "video_files")
public class VideoFile extends Tenantable {
    private static final Logger logger = LoggerFactory.getLogger(VideoFile.class);
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String videoName;
    private String videoDescription;

    private String videoUrl;

    private String videoType;

    private String videoSize;

    private String videoDuration;

    private String videoResolution;

    private String videoThumbnail;

    private String videoTags;

    private String videoCategory;
}
