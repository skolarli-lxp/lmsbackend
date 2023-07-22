package com.skolarli.lmsservice.models.db.core;

import com.skolarli.lmsservice.exception.OperationNotSupportedException;
import com.skolarli.lmsservice.models.dto.core.NewDomainRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tenants", uniqueConstraints = @UniqueConstraint(name = "domainname",
        columnNames = "domainName"))
@Where(clause = "tenant_is_deleted is null or tenant_is_deleted = false")
public class Tenant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull
    private String domainName;

    @NotNull
    private String companyName;

    private String tagLine;

    @NotNull
    private String countryCode;

    @NotNull
    private String phoneNumber;

    private String corporateEmail;

    private String logoUrl;

    @NotNull
    private String currency;

    @NotNull
    private String timeZone;

    private String website;
    private String address;

    private Boolean tenantIsDeleted;

    //TODO: Should we persist this? Or just convert to system timezone?
    //@NotNull
    //private String timeZone;

    public Tenant(NewDomainRequest newDomainRequest) {
        this.domainName = newDomainRequest.getDomainName();
        this.companyName = newDomainRequest.getCompanyName();
        this.countryCode = newDomainRequest.getCountryCode();
        this.phoneNumber = newDomainRequest.getPhoneNumber();
        this.currency = newDomainRequest.getCurrency();
        this.timeZone = newDomainRequest.getTimeZone();
        this.corporateEmail = newDomainRequest.getCorporateEmail();
        this.logoUrl = newDomainRequest.getLogoUrl();
        this.tagLine = newDomainRequest.getTagLine();
        if (newDomainRequest.getWebsite() != null) {
            this.website = newDomainRequest.getWebsite();
        }
        if (newDomainRequest.getAddress() != null) {
            this.address = newDomainRequest.getAddress();
        }
        this.tenantIsDeleted = false;
    }

    public void update(Tenant tenant) {
        if (tenant.getId() != 0) {
            throw new OperationNotSupportedException("Cannot change tenant id");
        }
        if (tenant.getDomainName() != null && !tenant.getDomainName().equals(this.domainName)) {
            throw new OperationNotSupportedException(
                    "Cannot update domainname for existing tenant");
        }
        if (tenant.getCompanyName() != null && !tenant.getCompanyName().isEmpty()) {
            this.setCompanyName(tenant.getCompanyName());
        }
        if (tenant.getTagLine() != null && !tenant.getTagLine().isEmpty()) {
            this.setTagLine(tenant.getTagLine());
        }
        if (tenant.getCorporateEmail() != null && !tenant.getCorporateEmail().isEmpty()) {
            this.setCorporateEmail(tenant.getCorporateEmail());
        }
        if (tenant.getLogoUrl() != null && !tenant.getLogoUrl().isEmpty()) {
            this.setLogoUrl(tenant.getLogoUrl());
        }
        if (tenant.getCountryCode() != null && !tenant.getCountryCode().isEmpty()) {
            this.setCountryCode(tenant.getCountryCode());
        }
        if (this.getTimeZone() != null && !this.getTimeZone().isEmpty()) {
            this.setTimeZone(tenant.getTimeZone());
        }
        if (tenant.getPhoneNumber() != null && !tenant.getPhoneNumber().isEmpty()) {
            this.setPhoneNumber(tenant.getPhoneNumber());
        }
        if (tenant.getCurrency() != null && !tenant.getCurrency().isEmpty()) {
            this.setCurrency(tenant.getCurrency());
        }
        if (tenant.getWebsite() != null && !tenant.getWebsite().isEmpty()) {
            this.setWebsite(tenant.getWebsite());
        }
        if (tenant.getAddress() != null && !tenant.getAddress().isEmpty()) {
            this.setAddress(tenant.getAddress());
        }
    }
}
