package com.skolarli.lmsservice.authentications;

import org.springframework.security.authentication.AbstractAuthenticationToken;

public class TenantAuthenticationToken extends AbstractAuthenticationToken {

    private final Object principal;
    private final long tenantId;

    public TenantAuthenticationToken(Object principal, long tenantId) {
        super(null);
        this.principal = principal;
        this.tenantId = tenantId;
        setAuthenticated(true);
    }

    public long getTenantId() {
        return tenantId;
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return principal;
    }
}
