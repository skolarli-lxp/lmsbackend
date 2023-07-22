package com.skolarli.lmsservice.services.impl;

import com.skolarli.lmsservice.exception.OperationNotSupportedException;
import com.skolarli.lmsservice.exception.ResourceNotFoundException;
import com.skolarli.lmsservice.models.Role;
import com.skolarli.lmsservice.models.db.core.LmsUser;
import com.skolarli.lmsservice.models.db.course.Batch;
import com.skolarli.lmsservice.models.db.course.Enrollment;
import com.skolarli.lmsservice.repository.LmsUserRepository;
import com.skolarli.lmsservice.services.LmsUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LmsUserServiceImpl implements LmsUserService {

    final Logger logger = LoggerFactory.getLogger(LmsUserServiceImpl.class);
    private final LmsUserRepository lmsUserRepository;


    public LmsUserServiceImpl(LmsUserRepository lmsUserRepository) {
        super();
        this.lmsUserRepository = lmsUserRepository;
    }

    private LmsUser getCurrentUser() {
        String userName = (String) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        return getLmsUserByEmail(userName);
    }

    @Override
    public List<LmsUser> getAllLmsUsers() {
        return lmsUserRepository.findAll();
    }

    @Override
    public LmsUser getLmsUserById(long id) {
        List<LmsUser> existingUser = lmsUserRepository.findAllById(new ArrayList<>(List.of(id)));
        if (existingUser.isEmpty()) {
            throw new ResourceNotFoundException("LmsUser", "Id", id);
        }
        return existingUser.get(0);
    }

    @Override
    public LmsUser getLmsUserByEmail(String email) {
        List<LmsUser> existingLmsUser = lmsUserRepository.findByEmail(email);
        if (!existingLmsUser.isEmpty()) {
            return existingLmsUser.get(0);
        } else {
            throw new ResourceNotFoundException("LmsUser", "email", email);
        }
        //LmsUser existingLmsUser = lmsUserRepository.findLmsUserByEmail(email);
        //if (existingLmsUser != null) {
        //    return  existingLmsUser;
        //} else {
        //    throw  new ResourceNotFoundException("LmsUser", "email", email);
        //}


        //LmsUser existingUser = lmsUserRepository.getByEmail(email);
        //if (existingUser != null) {
        //    return existingUser;
        //} else {
        //    throw  new ResourceNotFoundException("LmsUser", "email", email);
        //}
    }

    @Override
    public LmsUser getLmsUserByEmailAndTenantId(String email, long tenantId) {
        LmsUser existingLmsUser = lmsUserRepository.findByEmailAndTenantId(email, tenantId);
        if (existingLmsUser != null) {
            return existingLmsUser;
        } else {
            throw new ResourceNotFoundException("LmsUser", "email", email);
        }
    }

    @Override
    public List<LmsUser> getLmsUsersByRole(Role role) {
        if (role == Role.ADMIN) {
            return lmsUserRepository.findAllAdminUsers();
        } else if (role == Role.STUDENT) {
            return lmsUserRepository.findAllStudents();
        } else if (role == Role.INSTRUCTOR) {
            return lmsUserRepository.findAllInstructors();
        } else {
            logger.error("Role not found");
        }
        return null;
    }

    private List<Batch> getBatchesTaughtByInstructor(LmsUser instructor) {
        if (!instructor.getIsInstructor()) {
            throw new OperationNotSupportedException("User is not an instructor");
        } else {
            return instructor.getBatches();
        }
    }


    @Override
    public List<Batch> getBatchesTaughtByInstructor(Long instructorId) {
        LmsUser user = getLmsUserById(instructorId);
        return getBatchesTaughtByInstructor(user);
    }

    @Override
    public List<Batch> getBatchesTaughtByInstructor(String instructorEmail) {
        LmsUser user = getLmsUserByEmail(instructorEmail);
        return getBatchesTaughtByInstructor(user);
    }

    private List<Batch> getBatchesEnrolledForStudent(LmsUser student) {
        if (!student.getIsStudent()) {
            throw new OperationNotSupportedException("User is not an student");
        } else {
            List<Enrollment> enrollments = student.getEnrollments();
            return enrollments.stream().map(
                    Enrollment::getBatch).collect(Collectors.toList());
        }
    }

    @Override
    public List<Batch> getBatchesEnrolledForStudent(Long studentId) {
        LmsUser user = getLmsUserById(studentId);
        return getBatchesEnrolledForStudent(user);
    }

    @Override
    public List<Batch> getBatchesEnrolledForStudent(String studentEmail) {
        LmsUser user = getLmsUserByEmail(studentEmail);
        return getBatchesEnrolledForStudent(user);
    }

    @Override
    public LmsUser saveLmsUser(LmsUser lmsUser) {
        if (lmsUser.getUserIsDeleted() == null) {
            lmsUser.setUserIsDeleted(false);
        }
        if (lmsUser.getEmailVerified() == null) {
            lmsUser.setEmailVerified(false);
        }
        return lmsUserRepository.save(lmsUser);
    }

    @Override
    public LmsUser updateLmsUser(LmsUser lmsUser, long id) {
        LmsUser existingUser = lmsUserRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("LmsUser", "Id", id));
        existingUser.update(lmsUser);
        lmsUserRepository.save(existingUser);
        return existingUser;
    }

    @Override
    public void deleteLmsUser(long id) {
        LmsUser existingUser = lmsUserRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("LmsUser", "Id", id));
        if (getCurrentUser().getIsAdmin()) {
            existingUser.setUserIsDeleted(true);
            lmsUserRepository.save(existingUser);
        } else {
            throw new OperationNotSupportedException("User does not have permission to perform "
                    + "Delete operation");
        }
    }

    @Override
    public void hardDeleteLmsUser(long id) {
        LmsUser existingUser = lmsUserRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("LmsUser", "Id", id));
        if (!getCurrentUser().getIsAdmin()) {
            throw new OperationNotSupportedException("User does not have permission to perform "
                    + "Delete operation");
        }
        lmsUserRepository.delete(existingUser);
    }
}
