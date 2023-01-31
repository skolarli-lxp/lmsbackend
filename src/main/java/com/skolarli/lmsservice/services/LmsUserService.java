package com.skolarli.lmsservice.services;

import com.skolarli.lmsservice.models.db.LmsUser;

import java.util.List;

public interface LmsUserService {
    List<LmsUser> getAllLmsUsers ();
    LmsUser getLmsUserById(long id);
    LmsUser getLmsUserByEmailAndTenantId(String email, long tenantId);
    LmsUser getLmsUserByEmail(String email);

    LmsUser saveLmsUser (LmsUser lmsUser);
    
    LmsUser updateLmsUser(LmsUser lmsUser, long id);
    LmsUser verifyLmsUser(long id);

    void deleteLmsUser(long id);
    void hardDeleteLmsUser(long id);
}
