package com.skolarli.lmsservice.repository.core;

import com.skolarli.lmsservice.models.db.core.LmsUser;
import com.skolarli.lmsservice.models.db.core.VerificationCode;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VerificationCodeRepository extends JpaRepository<VerificationCode, Long> {
    VerificationCode findByToken(String token);

    VerificationCode findByUser(LmsUser user);

    VerificationCode findByUserId(Long userId);
}