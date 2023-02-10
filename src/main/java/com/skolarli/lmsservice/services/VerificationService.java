package com.skolarli.lmsservice.services;

import com.skolarli.lmsservice.models.db.LmsUser;
import com.skolarli.lmsservice.models.db.VerificationCode;

import java.util.List;

public interface VerificationService {
    List<VerificationCode> getAllVerificationCodes();
    VerificationCode getVerificationCodeByToken(String token);
    
    VerificationCode generateAndSaveVerificationCode(LmsUser user);
    VerificationCode saveVerificationCode(VerificationCode code);

    Boolean verifyCode(String token);
}
