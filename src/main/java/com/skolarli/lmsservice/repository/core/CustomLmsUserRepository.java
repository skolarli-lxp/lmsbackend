package com.skolarli.lmsservice.repository.core;

import com.skolarli.lmsservice.models.db.core.LmsUser;

public interface CustomLmsUserRepository {
    LmsUser findLmsUserByEmail(String email);
}
