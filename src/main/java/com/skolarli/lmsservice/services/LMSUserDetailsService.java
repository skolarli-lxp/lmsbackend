package com.skolarli.lmsservice.services;

import com.skolarli.lmsservice.contexts.TenantContext;
import com.skolarli.lmsservice.models.db.LmsUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;

@Service
public class LMSUserDetailsService implements UserDetailsService {
    HashMap<String, String> users = new HashMap<>() {{
        put("meenu", "meenu123");
        put("jaya", "jaya123");
    }};
    final
    LmsUserService lmsUserService;

    @Autowired
    private TenantContext tenantContext;

    public LMSUserDetailsService(LmsUserService lmsUserService) {
        this.lmsUserService = lmsUserService;
    }

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        LmsUser lmsUser = lmsUserService.getLmsUserByEmailAndTenantId(userName, tenantContext.getTenantId());
        if (lmsUser != null) {
            return new User(userName, users.get(userName), new ArrayList<>());
        } else {
            throw new UsernameNotFoundException("Cannot find user " + userName);
        }
//        if (users.containsKey(userName)) {
//            return new User(userName, users.get(userName), new ArrayList<>());
//        } else {
//            throw new UsernameNotFoundException("Cannot find user " + userName);
//        }
    }
}
