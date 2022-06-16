package com.skolarli.lmsservice.models.db;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.skolarli.lmsservice.listeners.TenantEntityListener;
import lombok.Data;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;

@Data
@MappedSuperclass
@FilterDef(name = Tenantable.TENANT_FILTER_NAME,
        parameters = @ParamDef(name = Tenantable.TENANT_PARAMETER_NAME, type = "long"),
        defaultCondition = Tenantable.TENANT_COLUMN + " = :" + Tenantable.TENANT_PARAMETER_NAME)
@Filter(name = Tenantable.TENANT_FILTER_NAME)
@EntityListeners(TenantEntityListener.class)
public class Tenantable {
    public static final String TENANT_FILTER_NAME = "tenantFilter";
    public static final String TENANT_PARAMETER_NAME = "tenantId";
    public static final String TENANT_COLUMN = "tenant_id";

    @JsonIgnore
    @Column(nullable = false)
    private long tenantId;
}
