package com.skolarli.lmsservice.utils;

import com.skolarli.lmsservice.models.db.core.LmsUser;
import com.skolarli.lmsservice.services.LmsUserService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class UserUtils {
    private final LmsUserService lmsUserService;

    public UserUtils(LmsUserService lmsUserService) {
        this.lmsUserService = lmsUserService;
    }

    public LmsUser getCurrentUser() {
        String userName = (String) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        return lmsUserService.getLmsUserByEmail(userName);
    }

    public Boolean currentUserIsAdmin() {
        return getCurrentUser().getIsAdmin();
    }

    public Boolean currentUserIsStudent() {
        return getCurrentUser().getIsStudent();
    }

    public Boolean currentUserIsInstructor() {
        return getCurrentUser().getIsInstructor();
    }

    public Boolean currentUserIsSuperAdmin() {
        return getCurrentUser().getIsSuperAdmin();
    }
}
