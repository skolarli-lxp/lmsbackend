package com.skolarli.lmsservice.repository;

import com.skolarli.lmsservice.models.db.Tenant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TenantRepository extends JpaRepository<Tenant, Long> {
    Tenant findByDomainName(String domainName);
}
