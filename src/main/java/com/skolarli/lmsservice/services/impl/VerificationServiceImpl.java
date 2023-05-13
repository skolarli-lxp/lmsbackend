package com.skolarli.lmsservice.services.impl;

import com.skolarli.lmsservice.models.db.LmsUser;
import com.skolarli.lmsservice.models.db.VerificationCode;
import com.skolarli.lmsservice.repository.LmsUserRepository;
import com.skolarli.lmsservice.repository.VerificationCodeRepository;
import com.skolarli.lmsservice.services.VerificationService;
import net.bytebuddy.utility.RandomString;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class VerificationServiceImpl implements VerificationService {

    final VerificationCodeRepository verificationCodeRepository;
    final LmsUserRepository lmsUserRepository;

    public VerificationServiceImpl(VerificationCodeRepository verificationCodeRepository,
                                   LmsUserRepository lmsUserRepository) {
        this.verificationCodeRepository = verificationCodeRepository;
        this.lmsUserRepository = lmsUserRepository;
    }

    @Override
    public List<VerificationCode> getAllVerificationCodes() {
        return verificationCodeRepository.findAll();
    }

    @Override
    public VerificationCode getVerificationCodeByToken(String token) {
        return verificationCodeRepository.findByToken(token);
    }

    @Override
    public VerificationCode getVerificationCodeByUser(Long userId) {
        return verificationCodeRepository.findByUserId(userId);
    }

    @Override
    public VerificationCode saveVerificationCode(VerificationCode code) {
        return verificationCodeRepository.save(code);
    }

    @Override
    public VerificationCode generateAndSaveVerificationCode(LmsUser user) {

        VerificationCode code = verificationCodeRepository.findByUser(user);
        if (code == null) {
            code = new VerificationCode();
            code.setUser(user);
        }
        code.setToken(RandomString.make(64));

        code.setExpiryDate(Instant.now().plusSeconds(VerificationCode.EXPIRY_HOURS * 60 * 60));

        verificationCodeRepository.save(code);
        return code;
    }

    @Override
    public Boolean verifyCode(String token) {
        VerificationCode code = verificationCodeRepository.findByToken(token);
        Instant expiryDate = code.getExpiryDate();
        if (expiryDate != null && expiryDate.isBefore(Instant.now())) {
            return false;
        }
        LmsUser user = code.getUser();
        user.setEmailVerified(true);
        lmsUserRepository.save(user);
        return true;
    }
}