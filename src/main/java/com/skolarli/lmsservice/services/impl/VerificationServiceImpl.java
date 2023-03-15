package com.skolarli.lmsservice.services.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.skolarli.lmsservice.models.db.LmsUser;
import com.skolarli.lmsservice.models.db.VerificationCode;
import com.skolarli.lmsservice.repository.LmsUserRepository;
import com.skolarli.lmsservice.repository.VerificationCodeRepository;
import com.skolarli.lmsservice.services.VerificationService;

import net.bytebuddy.utility.RandomString;

@Service
public class VerificationServiceImpl implements VerificationService{

    VerificationCodeRepository verificationCodeRepository;
    LmsUserRepository lmsUserRepository;

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

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.HOUR_OF_DAY, VerificationCode.EXPIRY_HOURS);
        code.setExpiryDate(cal.getTime());

        verificationCodeRepository.save(code);
        return code;
    }

    @Override
    public Boolean verifyCode(String token) {
        VerificationCode code = verificationCodeRepository.findByToken(token);
        Date expiryDate = code.getExpiryDate();
        if (expiryDate != null && expiryDate.before(new Date())) {
            return false;
        } 
        LmsUser user = code.getUser();
        user.setEmailVerified(true);
        lmsUserRepository.save(user);
        return true;
    }
}