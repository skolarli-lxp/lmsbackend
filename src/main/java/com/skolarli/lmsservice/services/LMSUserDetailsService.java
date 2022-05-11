package com.skolarli.lmsservice.services;

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
        put("meenu","meenu123");
        put("jaya","jaya123");
    }} ;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        if (users.containsKey(userName)) {
            return new User(userName, users.get(userName), new ArrayList<>());
        } else {
            throw new UsernameNotFoundException("Cannot find user " + userName);
        }
    }
}
