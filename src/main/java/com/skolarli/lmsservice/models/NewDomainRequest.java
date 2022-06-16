package com.skolarli.lmsservice.models;

import lombok.Data;
import org.springframework.lang.NonNull;

@Data
public class NewDomainRequest {
    @NonNull
    private String domainName;
    @NonNull
    private String companyName;
    private String website;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
}



