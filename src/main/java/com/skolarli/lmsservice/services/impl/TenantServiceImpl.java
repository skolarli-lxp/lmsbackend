package com.skolarli.lmsservice.services.impl;

import com.skolarli.lmsservice.exception.ResourceNotFoundException;
import com.skolarli.lmsservice.models.db.Tenant;
import com.skolarli.lmsservice.repository.TenantRepository;
import com.skolarli.lmsservice.services.TenantService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TenantServiceImpl implements TenantService {
    private final TenantRepository tenantRepository;

    public TenantServiceImpl(TenantRepository tenantRepository) {
        super();
        this.tenantRepository = tenantRepository;
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
    public Tenant updateTenant(Tenant tenant, long id) {
        Tenant existingTenant = tenantRepository.findById(id).orElseThrow(
                () ->  new ResourceNotFoundException("Tenant", "Id", id)
        );
        existingTenant.setCompanyName(tenant.getCompanyName());
        existingTenant.setWebsite(tenant.getWebsite());
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
}
