package com.skolarli.lmsservice.repository;

import com.skolarli.lmsservice.models.db.Tenant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TenantRepository extends JpaRepository<Tenant, Long> {
    Tenant findByDomainName(String domainName);

    @Query("SELECT t.domainName FROM Tenant t")
    List<String> getAllDomainNames();
}
