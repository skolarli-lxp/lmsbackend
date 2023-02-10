package com.skolarli.lmsservice.repository;

import com.skolarli.lmsservice.models.db.VerificationCode;

import org.springframework.data.jpa.repository.JpaRepository;

public interface VerificationCodeRepository extends JpaRepository<VerificationCode, Long> {
    VerificationCode findAllByToken(String token);
}