package com.skolarli.lmsservice.models.dto.resource;

import com.skolarli.lmsservice.models.db.resource.LmsResource;
import com.skolarli.lmsservice.models.db.resource.ResourceFor;
import com.skolarli.lmsservice.models.db.resource.ResourceFormat;
import lombok.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class NewLmsResourceRequest {
    private static final Logger logger = LoggerFactory
        .getLogger(NewLmsResourceRequest.class);

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

    private LmsResource toLmsResource(NewLmsResourceRequest newLmsResourceRequest) {
        LmsResource lmsResource = new LmsResource();
        lmsResource.setResourceName(newLmsResourceRequest.getResourceName());
        lmsResource.setResourceFormat(newLmsResourceRequest.getResourceFormat());
        lmsResource.setResourceFor(newLmsResourceRequest.getResourceFor());
        lmsResource.setResourceDescription(newLmsResourceRequest.getResourceDescription());
        lmsResource.setResourceUrl(newLmsResourceRequest.getResourceUrl());
        lmsResource.setResourceSizeKb(newLmsResourceRequest.getResourceSizeKb());
        lmsResource.setResourceMediaDuration(newLmsResourceRequest.getResourceMediaDuration());
        lmsResource.setResourceMediaResolution(newLmsResourceRequest.getResourceMediaResolution());
        lmsResource.setResourceThumbnail(newLmsResourceRequest.getResourceThumbnail());
        lmsResource.setResourceTags(newLmsResourceRequest.getResourceTags());
        lmsResource.setResourceCategory(newLmsResourceRequest.getResourceCategory());
        return lmsResource;
    }
}
