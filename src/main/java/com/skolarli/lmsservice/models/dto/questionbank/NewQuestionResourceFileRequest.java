package com.skolarli.lmsservice.models.dto.questionbank;

import com.skolarli.lmsservice.models.db.questionbank.ResourceFile;
import lombok.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class NewQuestionResourceFileRequest {
    private static final Logger logger = LoggerFactory
            .getLogger(NewQuestionResourceFileRequest.class);

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

    public ResourceFile toResourceFile() {
        ResourceFile resourceFile = new ResourceFile();
        resourceFile.setResourceName(resourceName);
        resourceFile.setResourceType(resourceType);
        resourceFile.setResourceDescription(resourceDescription);
        resourceFile.setResourceUrl(resourceUrl);
        resourceFile.setResourceSizeKb(resourceSizeKb);
        resourceFile.setResourceMediaDuration(resourceMediaDuration);
        resourceFile.setResourceMediaResolution(resourceMediaResolution);
        resourceFile.setResourceThumbnail(resourceThumbnail);
        resourceFile.setResourceTags(resourceTags);
        resourceFile.setResourceCategory(resourceCategory);
        return resourceFile;
    }
}
