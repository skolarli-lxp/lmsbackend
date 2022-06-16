package com.skolarli.lmsservice.repository;

import com.skolarli.lmsservice.models.db.LmsUser;

public interface CustomLmsUserRepository {
    LmsUser findLmsUserByEmail(String email);
}
