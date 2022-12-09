package com.skolarli.lmsservice.services;

import com.skolarli.lmsservice.models.db.Tenant;

import java.util.List;

public interface TenantService {
    Tenant saveTenant(Tenant tenant);
    List<Tenant> getAllTenants();
    Tenant getTenantById(long id);
    Tenant updateTenant(Tenant tenant);
    void deleteTenant(long id);
    void hardDeleteTenant(long id);

    List<String> getAllDomainNames();

    Boolean isUniqueDomainName(String domainName);

    Tenant getTenantByDomainName(String domainName);
}
