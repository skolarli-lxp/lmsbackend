package com.skolarli.lmsservice.models.dto.core;

import com.skolarli.lmsservice.models.db.core.LmsUser;
import com.skolarli.lmsservice.models.db.core.VerificationCode;
import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;

@Getter
@Setter
public class GetLmsUserResponse {
    private final long id;
    private final String firstName;
    private final String lastName;
    private final String email;
    private final String phoneNumber;
    private final int experience;
    private final String occupation;
    private final String education;
    private final String userBio;
    private final String profilePicUrl;
    private final String address;
    private final String country;
    private final Boolean isAdmin;
    private final Boolean isInstructor;
    private String companyName;
    private String employeeId;
    private Boolean isStudent;

    private Boolean isSuperAdmin;

    private Boolean emailVerified;

    private VerificationCode verificationCode;

    private Boolean userIsDeleted;

    private Boolean passwordResetRequested;

    private String passwordResetToken;

    private ZonedDateTime passwordResetTokenExpiry;

    public GetLmsUserResponse(LmsUser lmsUser) {
        this.id = lmsUser.getId();
        this.firstName = lmsUser.getFirstName();
        this.lastName = lmsUser.getLastName();
        this.email = lmsUser.getEmail();
        this.phoneNumber = lmsUser.getPhoneNumber();
        this.companyName = lmsUser.getCompanyName();
        this.employeeId = lmsUser.getEmployeeId();
        this.experience = lmsUser.getExperience();
        this.occupation = lmsUser.getOccupation();
        this.education = lmsUser.getEducation();
        this.userBio = lmsUser.getUserBio();
        this.profilePicUrl = lmsUser.getProfilePicUrl();
        this.address = lmsUser.getAddress();
        this.country = lmsUser.getCountry();
        this.isAdmin = lmsUser.getIsAdmin();
        this.isInstructor = lmsUser.getIsInstructor();
        this.isStudent = lmsUser.getIsStudent();
        this.isSuperAdmin = lmsUser.getIsSuperAdmin();
        this.emailVerified = lmsUser.getEmailVerified();
        this.verificationCode = lmsUser.getVerificationCode();
        this.userIsDeleted = lmsUser.getUserIsDeleted();
        this.passwordResetRequested = lmsUser.getPasswordResetRequested();
        this.passwordResetToken = lmsUser.getPasswordResetToken();
        this.passwordResetTokenExpiry = lmsUser.getPasswordResetTokenExpiry();
    }
}
