package com.skolarli.lmsservice.models.db.resource;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.skolarli.lmsservice.models.db.core.LmsUser;
import com.skolarli.lmsservice.models.db.core.Tenantable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import javax.persistence.*;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "resources")
public class LmsResource extends Tenantable {

    private static final Logger logger = LoggerFactory.getLogger(LmsResource.class);

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String resourceName;
    private ResourceFormat resourceFormat;
    private ResourceFor resourceFor;
    private String resourceDescription;
    private String resourceUrl;
    private String resourceSizeKb;
    private String resourceMediaDuration;
    private String resourceMediaResolution;
    private String resourceThumbnail;
    private String resourceTags;
    private String resourceCategory;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    @JsonIdentityReference(alwaysAsId = true)
    LmsUser createdBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "last_updated_by")
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    @JsonIdentityReference(alwaysAsId = true)
    LmsUser lastUpdatedBy;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_time", nullable = false, updatable = false)
    @CreationTimestamp
    private Date createdTime;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "last_updated_time", nullable = false)
    @UpdateTimestamp
    private Date lastUpdatedTime;

    public void update(LmsResource lmsResource) {
        if (lmsResource.getResourceName() != null) {
            this.resourceName = lmsResource.getResourceName();
        }
        if (lmsResource.getResourceFormat() != null) {
            this.resourceFormat = lmsResource.getResourceFormat();
        }
        if (lmsResource.getResourceFor() != null) {
            this.resourceFor = lmsResource.getResourceFor();
        }
        if (lmsResource.getResourceDescription() != null) {
            this.resourceDescription = lmsResource.getResourceDescription();
        }
        if (lmsResource.getResourceUrl() != null) {
            this.resourceUrl = lmsResource.getResourceUrl();
        }
        if (lmsResource.getResourceSizeKb() != null) {
            this.resourceSizeKb = lmsResource.getResourceSizeKb();
        }
        if (lmsResource.getResourceMediaDuration() != null) {
            this.resourceMediaDuration = lmsResource.getResourceMediaDuration();
        }
        if (lmsResource.getResourceMediaResolution() != null) {
            this.resourceMediaResolution = lmsResource.getResourceMediaResolution();
        }
        if (lmsResource.getResourceThumbnail() != null) {
            this.resourceThumbnail = lmsResource.getResourceThumbnail();
        }
        if (lmsResource.getResourceTags() != null) {
            this.resourceTags = lmsResource.getResourceTags();
        }
        if (lmsResource.getResourceCategory() != null) {
            this.resourceCategory = lmsResource.getResourceCategory();
        }
    }
}
