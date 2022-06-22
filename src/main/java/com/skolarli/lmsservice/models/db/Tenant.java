package com.skolarli.lmsservice.models.db;

import com.skolarli.lmsservice.models.NewDomainRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="tenants", uniqueConstraints = @UniqueConstraint(name= "domainname", columnNames = "domainName"))
public class Tenant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull
    private String domainName;

    @NotNull
    private String companyName;

    @NotNull
    private String countryCode;

    @NotNull
    private String phoneNumber;

    @NotNull
    private String currency;

    private String website;
    private String address;

    //TODO: Should we persist this? Or just convert to system timezone?
    //@NotNull
    //private String timeZone;

    public Tenant(NewDomainRequest newDomainRequest) {
        this.domainName = newDomainRequest.getDomainName();
        this.companyName = newDomainRequest.getCompanyName();
        this.countryCode = newDomainRequest.getCountryCode();
        this.phoneNumber = newDomainRequest.getPhoneNumber();
        this.currency = newDomainRequest.getCurrency();
        if (newDomainRequest.getWebsite() != null) {
            this.website = newDomainRequest.getWebsite();
        }
        if (newDomainRequest.getAddress() != null) {
            this.address = newDomainRequest.getAddress();
        }
    }
}
