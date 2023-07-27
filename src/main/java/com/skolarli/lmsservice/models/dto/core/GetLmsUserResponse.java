package com.skolarli.lmsservice.models.dto.core;

import com.skolarli.lmsservice.models.db.core.LmsUser;
import lombok.Getter;
import lombok.Setter;

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

    private final Boolean isStudent;
    private final Boolean emailVerified;

    private final Boolean passwordResetRequested;

    public GetLmsUserResponse(LmsUser lmsUser) {
        this.id = lmsUser.getId();
        this.firstName = lmsUser.getFirstName();
        this.lastName = lmsUser.getLastName();
        this.email = lmsUser.getEmail();
        this.phoneNumber = lmsUser.getPhoneNumber();
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
        this.emailVerified = lmsUser.getEmailVerified();
        this.passwordResetRequested = lmsUser.getPasswordResetRequested();
    }
}
