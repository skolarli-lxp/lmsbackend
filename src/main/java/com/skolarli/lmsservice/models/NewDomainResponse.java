package com.skolarli.lmsservice.models;

import com.skolarli.lmsservice.models.db.LmsUser;
import com.skolarli.lmsservice.models.db.Tenant;
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

    public  NewDomainResponse(Tenant tenant, LmsUser lmsUser) {
        this.userId = lmsUser.getId();
        this.tenantId = tenant.getId();
        this.domainName = tenant.getDomainName();
        this.companyName = tenant.getCompanyName();
        this.website = tenant.getWebsite();
        this.firstName = lmsUser.getFirstName();
        this.lastName = lmsUser.getLastName();
        this.email = lmsUser.getEmail();
    }
}



