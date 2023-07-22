package com.skolarli.lmsservice.models.db.questionbank;

import com.skolarli.lmsservice.models.db.core.Tenantable;
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
@Table(name = "resource_files")
public class ResourceFile extends Tenantable {
    private static final Logger logger = LoggerFactory.getLogger(ResourceFile.class);
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String resourceName;
    private String resourceType;
    private String resourceDescription;

    private String resourceUrl;


    private String resourceSizeKb;

    private String resourceMediaDuration;

    private String resourceMediaResolution;

    private String resourceThumbnail;

    private String resourceTags;

    private String resourceCategory;

    public void update(ResourceFile resourceFile) {
        if (resourceFile.getResourceName() != null) {
            this.resourceName = resourceFile.getResourceName();
        }
        if (resourceFile.getResourceType() != null) {
            this.resourceType = resourceFile.getResourceType();
        }
        if (resourceFile.getResourceDescription() != null) {
            this.resourceDescription = resourceFile.getResourceDescription();
        }
        if (resourceFile.getResourceUrl() != null) {
            this.resourceUrl = resourceFile.getResourceUrl();
        }
        if (resourceFile.getResourceSizeKb() != null) {
            this.resourceSizeKb = resourceFile.getResourceSizeKb();
        }
        if (resourceFile.getResourceMediaDuration() != null) {
            this.resourceMediaDuration = resourceFile.getResourceMediaDuration();
        }
        if (resourceFile.getResourceMediaResolution() != null) {
            this.resourceMediaResolution = resourceFile.getResourceMediaResolution();
        }
        if (resourceFile.getResourceThumbnail() != null) {
            this.resourceThumbnail = resourceFile.getResourceThumbnail();
        }
        if (resourceFile.getResourceTags() != null) {
            this.resourceTags = resourceFile.getResourceTags();
        }
        if (resourceFile.getResourceCategory() != null) {
            this.resourceCategory = resourceFile.getResourceCategory();
        }
    }
}
