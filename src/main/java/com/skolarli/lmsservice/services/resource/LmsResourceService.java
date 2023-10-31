package com.skolarli.lmsservice.services.resource;

import com.skolarli.lmsservice.models.db.resource.LmsResource;
import com.skolarli.lmsservice.models.db.resource.ResourceFor;

import java.util.List;

public interface LmsResourceService {
    LmsResource createLmsResource(LmsResource lmsResource);

    LmsResource getLmsResourceById(Long id);

    List<LmsResource> getLmsResourceFor(ResourceFor type);

    List<LmsResource> getLmsResourceByFormat(String format);

    List<LmsResource> getAllLmsResources();

    LmsResource updateLmsResource(Long id, LmsResource lmsResource);

    void deleteLmsResource(Long id);
}
