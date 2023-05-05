package com.skolarli.lmsservice.models.dto;

import com.skolarli.lmsservice.models.db.LmsUser;
import com.skolarli.lmsservice.models.db.Tenant;
import com.skolarli.lmsservice.models.db.VerificationCode;
import lombok.Data;

@Data
public class NewDomainResponse {
    private long userId;
    private long tenantId;
    private String domainName;
    private String companyName;
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
        this.website = tenant.getWebsite();
        this.firstName = lmsUser.getFirstName();
        this.lastName = lmsUser.getLastName();
        this.email = lmsUser.getEmail();
        this.verificationCode = code.getToken();
    }
}



