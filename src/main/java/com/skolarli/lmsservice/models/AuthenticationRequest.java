package com.skolarli.lmsservice.models;

import lombok.*;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationRequest {
    @NotNull(message = "username cannot be empty")
    private String username;
    @NotNull(message = "password cannot be empty")
    private String password;
}
