package com.skolarli.lmsservice.services.core;

import com.skolarli.lmsservice.contexts.TenantContext;
import com.skolarli.lmsservice.models.db.core.LmsUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class LmsUserDetailsService implements UserDetailsService {
    Logger logger = LoggerFactory.getLogger(LmsUserDetailsService.class);
    final LmsUserService lmsUserService;

    @Autowired
    private TenantContext tenantContext;

    public LmsUserDetailsService(LmsUserService lmsUserService) {
        this.lmsUserService = lmsUserService;
    }

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        LmsUser lmsUser = lmsUserService.getLmsUserByEmailAndTenantId(
                userName, tenantContext.getTenantId());
        if (lmsUser != null) {
            return new User(userName, lmsUser.getPassword(), new ArrayList<>());
        } else {
            throw new UsernameNotFoundException("Cannot find user " + userName);
        }
    }
}
