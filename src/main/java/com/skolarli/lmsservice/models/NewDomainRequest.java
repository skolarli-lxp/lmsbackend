package com.skolarli.lmsservice.models;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Data
public class NewDomainRequest {

    //Company Data - Mandatory
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



