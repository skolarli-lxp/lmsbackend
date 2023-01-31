package com.skolarli.lmsservice.repository;

import com.skolarli.lmsservice.models.db.LmsUser;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

public interface LmsUserRepository extends TenantableRepository<LmsUser>, CustomLmsUserRepository {
    List<LmsUser> findByEmail(String email);
    List<LmsUser> findOneByEmail(String email);
    LmsUser findByEmailAndTenantId(String email, long tenantId);
    @Query("SELECT u FROM LmsUser AS u WHERE u.isAdmin = true")
    List<LmsUser> findAllAdminUsers();
    @Query("SELECT u FROM LmsUser AS u WHERE u.isStudent = true")
    List<LmsUser> findAllStudents();
    @Query("SELECT u FROM LmsUser AS u WHERE u.isInstructor = true")
    List<LmsUser> findAllInstructors();
}