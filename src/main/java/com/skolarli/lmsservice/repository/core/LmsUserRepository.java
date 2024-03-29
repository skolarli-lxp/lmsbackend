package com.skolarli.lmsservice.repository.core;

import com.skolarli.lmsservice.models.db.core.LmsUser;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface LmsUserRepository extends TenantableRepository<LmsUser>, CustomLmsUserRepository {
    List<LmsUser> findByEmail(String email);

    LmsUser findByEmailAndTenantId(String email, long tenantId);

    LmsUser findByPasswordResetToken(String passwordResetToken);

    @Query("SELECT u FROM LmsUser AS u WHERE u.isAdmin = true")
    List<LmsUser> findAllAdminUsers();

    @Query("SELECT u FROM LmsUser AS u WHERE u.isStudent = true")
    List<LmsUser> findAllStudents();

    @Query("SELECT u FROM LmsUser AS u WHERE u.isInstructor = true")
    List<LmsUser> findAllInstructors();

    @Query("SELECT count(*) FROM LmsUser")
    Long findLmsUserCount();

    @Query("SELECT count(*) FROM LmsUser AS u WHERE u.isStudent = true")
    Long findStudentUserCount();

    @Query("SELECT count(*) FROM LmsUser AS u WHERE u.isAdmin = true")
    Long findAdminUserCount();

    @Query("SELECT count(*) FROM LmsUser AS u WHERE u.isInstructor = true")
    Long findInstructorUserCount();
}