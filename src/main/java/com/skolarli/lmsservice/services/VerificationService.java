package com.skolarli.lmsservice.services;

import com.skolarli.lmsservice.models.db.VerificationCode;

import java.util.List;

public interface VerificationService {
    List<VerificationCode> getAllVerificationCodes();
    VerificationCode getVerificationCodeByToken(String token);
    
    VerificationCode saveVerificationCode(VerificationCode code);

    Boolean verifyCode(String token);
}
