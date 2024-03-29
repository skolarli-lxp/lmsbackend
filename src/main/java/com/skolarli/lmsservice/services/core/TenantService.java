package com.skolarli.lmsservice.services.core;

import com.skolarli.lmsservice.models.db.core.Tenant;

import java.util.List;

public interface TenantService {
    List<Tenant> getAllTenants();

    Tenant getTenantById(long id);

    Tenant getCurrentTenant();

    Tenant getTenantByDomainName(String domainName);

    List<String> getAllDomainNames();

    Tenant saveTenant(Tenant tenant);

    Tenant updateTenant(Tenant tenant);

    void deleteTenant(long id);

    void hardDeleteTenant(long id);

    Boolean isUniqueDomainName(String domainName);
}
