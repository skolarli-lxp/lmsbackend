package com.skolarli.lmsservice.services.core;

import com.skolarli.lmsservice.models.db.core.LmsUser;
import com.skolarli.lmsservice.models.db.core.VerificationCode;

import java.util.List;

public interface VerificationService {
    List<VerificationCode> getAllVerificationCodes();

    VerificationCode getVerificationCodeByToken(String token);

    VerificationCode getVerificationCodeByUser(Long userId);

    VerificationCode generateAndSaveVerificationCode(LmsUser user);

    VerificationCode saveVerificationCode(VerificationCode code);

    Boolean verifyCode(String token);
}
