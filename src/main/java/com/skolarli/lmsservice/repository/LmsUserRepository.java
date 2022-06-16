package com.skolarli.lmsservice.repository;

import com.skolarli.lmsservice.models.db.LmsUser;

import java.util.List;

public interface LmsUserRepository extends TenantableRepository<LmsUser>, CustomLmsUserRepository {
    List<LmsUser> findByEmail(String email);
    List<LmsUser> findOneByEmail(String email);
    LmsUser findByEmailAndTenantId(String email, long tenantId);
}

