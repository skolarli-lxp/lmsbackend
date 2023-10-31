package com.skolarli.lmsservice.services.impl.resource;

import com.skolarli.lmsservice.exception.ResourceNotFoundException;
import com.skolarli.lmsservice.models.db.questionbank.BankQuestionTrueOrFalse;
import com.skolarli.lmsservice.models.db.resource.LmsResource;
import com.skolarli.lmsservice.models.db.resource.ResourceFor;
import com.skolarli.lmsservice.repository.resource.LmsResourceRepository;
import com.skolarli.lmsservice.services.resource.LmsResourceService;
import com.skolarli.lmsservice.utils.UserUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class LmsResourceServiceImpl implements LmsResourceService {

    LmsResourceRepository lmsResourceRepository;

    UserUtils userUtils;

    public LmsResourceServiceImpl(LmsResourceRepository lmsResourceRepository,
                                  UserUtils userUtils) {
        this.lmsResourceRepository = lmsResourceRepository;
        this.userUtils = userUtils;
    }

    @Override
    public LmsResource createLmsResource(LmsResource lmsResource) {
        lmsResource.setCreatedBy(userUtils.getCurrentUser());
        LmsResource newResource = lmsResourceRepository.save(lmsResource);
        return newResource;
    }

    @Override
    public LmsResource getLmsResourceById(Long id) {
        List<LmsResource> lmsResources = lmsResourceRepository.findAllById(new ArrayList<>(List.of(id)));
        if (lmsResources.size() == 0) {
            throw new ResourceNotFoundException("Resource with Id " + id + " not found");
        }
        return lmsResources.get(0);
    }

    @Override
    public List<LmsResource> getLmsResourceFor(ResourceFor type) {
        List<LmsResource> lmsResources = lmsResourceRepository.findAllByResourceFor(type);
        return lmsResources;
    }

    @Override
    public List<LmsResource> getLmsResourceByFormat(String format) {
        List<LmsResource> lmsResources = lmsResourceRepository.findAllByResourceFormat(format);
        return lmsResources;
    }

    @Override
    public List<LmsResource> getAllLmsResources() {
        List<LmsResource> lmsResources = lmsResourceRepository.findAll();
        return lmsResources;
    }

    @Override
    public LmsResource updateLmsResource(Long id, LmsResource lmsResource) {
        LmsResource existingResource = getLmsResourceById(id);
        existingResource.update(lmsResource);
        existingResource.setLastUpdatedBy(userUtils.getCurrentUser());
        return lmsResourceRepository.save(existingResource);
    }

    @Override
    public void deleteLmsResource(Long id) {
        LmsResource existingResource = getLmsResourceById(id);
        lmsResourceRepository.delete(existingResource);
    }
}
