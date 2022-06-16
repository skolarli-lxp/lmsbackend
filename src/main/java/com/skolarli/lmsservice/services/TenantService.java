package com.skolarli.lmsservice.services;

import com.skolarli.lmsservice.models.db.Tenant;

import java.util.List;

public interface TenantService {
    Tenant saveTenant(Tenant tenant);
    List<Tenant> getAllTenants();
    Tenant getTenantById(long id);
    Tenant updateTenant(Tenant tenant, long id);
    void deleteTenant(long id);

    Tenant getTenantByDomainName(String domainName);
}
