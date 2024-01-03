package com.skolarli.lmsservice.services.core;

import com.skolarli.lmsservice.models.Role;
import com.skolarli.lmsservice.models.db.core.LmsUser;
import com.skolarli.lmsservice.models.db.course.Batch;
import com.skolarli.lmsservice.models.dto.core.PasswordResetTokenResponse;

import java.util.List;

public interface LmsUserService {
    List<LmsUser> getAllLmsUsers();

    LmsUser getLmsUserById(long id);

    LmsUser getLmsUserByEmailAndTenantId(String email, long tenantId);

    LmsUser getLmsUserByEmail(String email);

    List<LmsUser> getLmsUsersByRole(Role role);

    List<Batch> getBatchesEnrolledForStudent(Long studentId);

    List<Batch> getBatchesEnrolledForStudent(String email);

    List<Batch> getBatchesTaughtByInstructor(Long instructorId);

    List<Batch> getBatchesTaughtByInstructor(String email);

    LmsUser saveLmsUser(LmsUser lmsUser);

    List<LmsUser> saveLmsUsers(List<LmsUser> lmsUsers);

    LmsUser updateLmsUser(LmsUser lmsUser, long id);

    PasswordResetTokenResponse createAndGetPasswordResetToken(LmsUser lmsUser);

    void resetPassword(String token, String newPassword);

    void deleteLmsUser(long id);

    void hardDeleteLmsUser(long id);
}
