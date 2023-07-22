package com.skolarli.lmsservice.repository;

import com.skolarli.lmsservice.models.db.core.LmsUser;

public interface CustomLmsUserRepository {
    LmsUser findLmsUserByEmail(String email);
}
