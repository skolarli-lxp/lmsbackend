package com.skolarli.lmsservice.models.dto.core;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class NewDomainRequest {

    //Company Data - Mandatory
    @NotNull
    private String domainName;
    @NotNull
    private String companyName;

    private String tagLine;

    private  String corporateEmail;
    private String logoUrl;
    @NotNull
    private String countryCode;
    @NotNull
    private String phoneNumber;
    @NotNull
    private String currency;
    @NotNull
    private String timeZone;

    // Company Data - Optional
    private String address;
    private String website;

    // User Data - Mandatory
    @NotNull
    private String firstName;
    @NotNull
    private String lastName;
    @NotNull
    @Email
    private String email;
    @NotNull
    private String password;
}



