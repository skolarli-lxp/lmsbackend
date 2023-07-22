package com.skolarli.lmsservice.services.impl;

import com.skolarli.lmsservice.exception.OperationNotSupportedException;
import com.skolarli.lmsservice.exception.ResourceNotFoundException;
import com.skolarli.lmsservice.models.db.core.LmsUser;
import com.skolarli.lmsservice.models.db.core.VerificationCode;
import com.skolarli.lmsservice.repository.LmsUserRepository;
import com.skolarli.lmsservice.repository.VerificationCodeRepository;
import com.skolarli.lmsservice.services.VerificationService;
import net.bytebuddy.utility.RandomString;
import org.springframework.stereotype.Service;

import java.time.Instant;
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
        if (code == null) {
            throw new ResourceNotFoundException("Verification code not found");
        }
        Instant expiryDate = code.getExpiryDate();
        if (expiryDate != null && expiryDate.isBefore(Instant.now())) {
            throw new OperationNotSupportedException("Verification code expired");
        }
        LmsUser user = code.getUser();
        user.setEmailVerified(true);
        lmsUserRepository.save(user);
        return true;
    }
}