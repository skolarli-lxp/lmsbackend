package com.skolarli.lmsservice.contexts;

import com.skolarli.lmsservice.authentications.TenantAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class TenantContext {
    public long getTenantId() {
        return ((TenantAuthenticationToken) SecurityContextHolder.getContext().
                    getAuthentication()).getTenantId();
    }
}
