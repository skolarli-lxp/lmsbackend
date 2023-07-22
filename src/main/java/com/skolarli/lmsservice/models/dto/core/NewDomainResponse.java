package com.skolarli.lmsservice.models.dto.core;

import com.skolarli.lmsservice.models.db.core.LmsUser;
import com.skolarli.lmsservice.models.db.core.Tenant;
import com.skolarli.lmsservice.models.db.core.VerificationCode;
import lombok.Data;

@Data
public class NewDomainResponse {
    private long userId;
    private long tenantId;
    private String domainName;
    private String companyName;

    private String tagLine;

    private String corporateEmail;
    private String logoUrl;
    private String countryCode;
    private String phoneNumber;
    private String currency;
    private String timeZone;
    private String address;

    private String website;
    private String firstName;
    private String lastName;
    private String email;
    private String verificationCode;

    public NewDomainResponse(Tenant tenant, LmsUser lmsUser, VerificationCode code) {
        this.userId = lmsUser.getId();
        this.tenantId = tenant.getId();
        this.domainName = tenant.getDomainName();
        this.companyName = tenant.getCompanyName();
        this.tagLine = tenant.getTagLine();
        this.corporateEmail = tenant.getCorporateEmail();
        this.logoUrl = tenant.getLogoUrl();
        this.countryCode = tenant.getCountryCode();
        this.phoneNumber = tenant.getPhoneNumber();
        this.currency = tenant.getCurrency();
        this.timeZone = tenant.getTimeZone();
        this.address = tenant.getAddress();
        this.website = tenant.getWebsite();
        this.firstName = lmsUser.getFirstName();
        this.lastName = lmsUser.getLastName();
        this.email = lmsUser.getEmail();
        this.verificationCode = code.getToken();
    }
}



