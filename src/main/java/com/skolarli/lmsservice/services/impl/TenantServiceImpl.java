package com.skolarli.lmsservice.services.impl;

import com.skolarli.lmsservice.contexts.TenantContext;
import com.skolarli.lmsservice.exception.ResourceNotFoundException;
import com.skolarli.lmsservice.models.db.Tenant;
import com.skolarli.lmsservice.repository.TenantRepository;
import com.skolarli.lmsservice.services.TenantService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TenantServiceImpl implements TenantService {
    Logger logger = LoggerFactory.getLogger(TenantServiceImpl.class);
    private final TenantRepository tenantRepository;

    private TenantContext tenantContext;

    public TenantServiceImpl(TenantRepository tenantRepository, TenantContext tenantContext) {
        super();
        this.tenantRepository = tenantRepository;
        this.tenantContext = tenantContext;
    }

    @Override
    public Tenant saveTenant(Tenant tenant) {
        tenantRepository.save(tenant);
        return tenant;
    }

    @Override
    public List<Tenant> getAllTenants() {
        return tenantRepository.findAll();
    }

    @Override
    public Tenant getTenantById(long id) {
        Optional<Tenant> tenant = tenantRepository.findById(id);
        if (tenant.isPresent()) {
            return tenant.get();
        } else {
            throw new ResourceNotFoundException("Tenant", "Id", id);
        }
    }

    @Override
    public Tenant getTenantByDomainName(String domainName) {
        Tenant tenant = tenantRepository.findByDomainName(domainName);
        if (tenant != null) {
            return tenant;
        } else {
            throw new ResourceNotFoundException("Tenant", "Domain Name", domainName);
        }
    }

    @Override
    public Tenant updateTenant(Tenant tenant) {
        long id = tenantContext.getTenantId();
        Tenant existingTenant = tenantRepository.findById(id).orElseThrow(
                () ->  new ResourceNotFoundException("Tenant", "Id", id)
        );
        existingTenant.update(tenant);
        tenantRepository.save(existingTenant);
        return existingTenant;
    }

    @Override
    public void deleteTenant(long id) {
        Tenant existingTenant = tenantRepository.findById(id).orElseThrow(
                () ->  new ResourceNotFoundException("Tenant", "Id", id)
        );
        tenantRepository.delete(existingTenant);
    }

    @Override
    public List<String> getAllDomainNames() {
        List<String> domainNames = tenantRepository.getAllDomainNames();
        return domainNames;
    }

    @Override
    public Boolean isUniqueDomainName(String domainName) {
        List<String> domainNames = getAllDomainNames();
        if (domainNames.contains(domainName)) {
            return false;
        } else {
            return true;
        }
    }


}
