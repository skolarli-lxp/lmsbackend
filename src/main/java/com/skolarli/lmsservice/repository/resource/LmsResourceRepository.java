package com.skolarli.lmsservice.repository.resource;

import com.skolarli.lmsservice.models.db.resource.LmsResource;
import com.skolarli.lmsservice.models.db.resource.ResourceFor;
import com.skolarli.lmsservice.repository.core.TenantableRepository;

import java.util.List;

public interface LmsResourceRepository extends TenantableRepository<LmsResource> {
    List<LmsResource> findAllByResourceFor(ResourceFor resourceFor);

    List<LmsResource> findAllByResourceFormat(String resourceFormat);
}
