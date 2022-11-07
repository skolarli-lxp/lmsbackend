package com.skolarli.lmsservice.services;

import com.skolarli.lmsservice.models.db.LmsUser;

import java.util.List;

public interface LmsUserService {
    LmsUser saveLmsUser (LmsUser lmsUser);
    List<LmsUser> getAllLmsUsers ();
    LmsUser getLmsUserById(long id);
    LmsUser updateLmsUser(LmsUser lmsUser, long id);
    void deleteLmsUser(long id);
    LmsUser getLmsUserByEmailAndTenantId(String email, long tenantId);
    LmsUser getLmsUserByEmail(String email);
}
