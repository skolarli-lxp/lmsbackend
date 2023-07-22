package com.skolarli.lmsservice.aspects;

import com.skolarli.lmsservice.contexts.TenantContext;
import com.skolarli.lmsservice.models.db.core.Tenantable;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Aspect
@Component
public class TenantAspect {
    @Autowired
    private TenantContext tenantContext;

    @PersistenceContext
    private EntityManager entityManager;

    @Before("execution(* com.skolarli.lmsservice.repository.core.TenantableRepository+.find*(..))")
    public void beforeFindOfTenantableRepository() {
        entityManager
                .unwrap(Session.class)
                .enableFilter(Tenantable.TENANT_FILTER_NAME)
                .setParameter(Tenantable.TENANT_PARAMETER_NAME, tenantContext.getTenantId());
    }
}
