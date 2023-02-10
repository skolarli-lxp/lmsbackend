package com.skolarli.lmsservice.listeners;

import com.skolarli.lmsservice.contexts.TenantContext;
import com.skolarli.lmsservice.models.db.Tenantable;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityNotFoundException;
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;

public class TenantEntityListener {

    @Autowired
    private TenantContext tenantContext;

    @PrePersist
    @PreUpdate
    public void prePersistOrUpdate(Object object) {
        if (object instanceof Tenantable) {
            if (((Tenantable)object).getTenantId() == 0) {
                ((Tenantable) object).setTenantId(tenantContext.getTenantId());
            };
        }
    }

    @PreRemove
    public void preRemove(Object object) {
        if (object instanceof Tenantable && ((Tenantable) object).getTenantId() != tenantContext.getTenantId()) {
            throw new EntityNotFoundException();
        }
    }

}
