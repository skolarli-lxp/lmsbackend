package com.skolarli.lmsservice.services;

import com.skolarli.lmsservice.models.Role;
import com.skolarli.lmsservice.models.db.LmsUser;
import com.skolarli.lmsservice.models.db.VerificationCode;

import java.util.List;

public interface LmsUserService {
    List<LmsUser> getAllLmsUsers ();
    LmsUser getLmsUserById(long id);
    LmsUser getLmsUserByEmailAndTenantId(String email, long tenantId);
    LmsUser getLmsUserByEmail(String email);
    List<LmsUser> getLmsUsersByRole(Role role);

    LmsUser saveLmsUser (LmsUser lmsUser);
    
    LmsUser updateLmsUser(LmsUser lmsUser, long id);

    void deleteLmsUser(long id);
    void hardDeleteLmsUser(long id);
}
