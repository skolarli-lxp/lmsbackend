package com.skolarli.lmsservice.services.impl;

import java.util.List;

import com.skolarli.lmsservice.models.db.VerificationCode;
import com.skolarli.lmsservice.repository.VerificationCodeRepository;
import com.skolarli.lmsservice.services.VerificationService;

public class VerificationServiceImpl implements VerificationService{

    VerificationCodeRepository verificationCodeRepository;

    public VerificationServiceImpl(VerificationCodeRepository verificationCodeRepository) {
        this.verificationCodeRepository = verificationCodeRepository;
    }

    @Override
    public List<VerificationCode> getAllVerificationCodes() {
        return verificationCodeRepository.findAll();
    }

    @Override
    public VerificationCode getVerificationCodeByToken(String token) {
        return verificationCodeRepository.findAllByToken(token);
    }

    @Override
    public VerificationCode saveVerificationCode(VerificationCode code) {
        return verificationCodeRepository.save(code);
    }

    @Override
    public Boolean verifyCode(String token) {
        // TODO 
        return true;
    }
    
}