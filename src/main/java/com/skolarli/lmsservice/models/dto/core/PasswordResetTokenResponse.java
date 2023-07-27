package com.skolarli.lmsservice.models.dto.core;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.ZonedDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PasswordResetTokenResponse {
    private String token;
    private ZonedDateTime expiryDate;
}
