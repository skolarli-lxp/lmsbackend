package com.skolarli.lmsservice.filters;

import com.skolarli.lmsservice.authentications.TenantAuthenticationToken;
import com.skolarli.lmsservice.models.db.Tenant;
import com.skolarli.lmsservice.services.TenantService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class TenantAuthorizationFilter extends OncePerRequestFilter {
    Logger logger = LoggerFactory.getLogger(TenantAuthorizationFilter.class);

    @Autowired
    TenantService tenantService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        logger.info("Tenant Authorization Filter");
        TenantAuthenticationToken tenantAuthenticationToken = getTenantAuthenticationToken(
                request.getHeader("Domain"));
        SecurityContextHolder.getContext().setAuthentication(tenantAuthenticationToken);
        logger.info("Set Authentication");
        filterChain.doFilter(request, response);
    }

    private TenantAuthenticationToken getTenantAuthenticationToken(String domainName) {
        Tenant tenant = tenantService.getTenantByDomainName(domainName);
        return new TenantAuthenticationToken("", tenant.getId());
    }


    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {

        String path = request.getServletPath();
        return path.startsWith("/newdomain") || path.startsWith("/gethealthnoauth") 
                                                     ||  path.startsWith("/verify");
    }
}
