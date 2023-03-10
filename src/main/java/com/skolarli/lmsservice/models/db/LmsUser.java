package com.skolarli.lmsservice.models.db;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.skolarli.lmsservice.models.NewDomainRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Where;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="users", uniqueConstraints = @UniqueConstraint(name= "useremail", columnNames = {"email", "tenantId"}))
@Where(clause = "user_is_deleted is null or user_is_deleted = false")
public class LmsUser extends Tenantable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull
    private String firstName;

    @NotNull
    private String lastName;

    @NotNull
    private String email;

    @Column(columnDefinition = "VARCHAR(15)")
    private String phoneNumber;

    private String companyName;

    private int experience;

    @Column(columnDefinition = "TEXT")
    private String userBio;

    @Column(columnDefinition = "VARCHAR(1024)")
    private String profilePicUrl;

    @Column(columnDefinition = "TEXT")
    private String address;

    private String country;


    //TODO: Better password storage
    @NotNull
    @JsonProperty( value = "password", access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @NotNull
    @Column(columnDefinition = "bit(1) default false")
    private Boolean isAdmin;

    @NotNull
    @Column(columnDefinition = "bit(1) default false")
    private Boolean isInstructor;

    @NotNull
    @Column(columnDefinition = "bit(1) default false")
    private Boolean isStudent;

    @NotNull
    @Column(columnDefinition = "bit(1) default false")
    private Boolean emailVerified;

    @OneToMany(mappedBy = "courseOwner")
    @JsonIgnoreProperties("courseOwner") // To avoid infinite recursion during serialization
    private List<Course> courses;

    @OneToMany(mappedBy = "instructor")
    @JsonIgnoreProperties("instructor")
    private List<Batch> batches;

    @OneToMany(mappedBy = "student")
    @JsonIgnoreProperties("student")
    private List<Attendance> attendanceList;

    @OneToMany(mappedBy = "student")
    @JsonIgnoreProperties("student")
    private List<Enrollment> enrollments;

    @OneToOne(mappedBy = "user")
    @JsonIgnoreProperties("user")
    private VerificationCode verificationCode;

    private Boolean userIsDeleted;

    public LmsUser(NewDomainRequest newDomainRequest) {
        this.firstName = newDomainRequest.getFirstName();
        this.lastName = newDomainRequest.getLastName();
        this.email = newDomainRequest.getEmail();
        this.password = newDomainRequest.getPassword();
        this.isAdmin = true;
        this.isStudent = false;
        this.isInstructor = false;
    }

    public void update(LmsUser lmsUser) {
        if (lmsUser.getFirstName() != null && !lmsUser.getFirstName().isEmpty()) {
            this.setFirstName(lmsUser.getFirstName());
        }
        if (lmsUser.getLastName() != null && !lmsUser.getLastName().isEmpty()) {
            this.setLastName(lmsUser.getLastName());
        }
        if(lmsUser.getPhoneNumber() != null && !lmsUser.getPhoneNumber().isEmpty()) {
            this.setPhoneNumber(lmsUser.getPhoneNumber());
        }
        if(lmsUser.getUserBio() != null && !lmsUser.getUserBio().isEmpty()) {
            this.setUserBio(lmsUser.getUserBio());
        }
        if(lmsUser.getCompanyName() != null && !lmsUser.getCompanyName().isEmpty()) {
            this.setCompanyName(lmsUser.getCompanyName());
        }
        if(lmsUser.getExperience() == 0) {
            this.setExperience(lmsUser.getExperience());
        }
        if(lmsUser.getProfilePicUrl() != null && !lmsUser.getProfilePicUrl().isEmpty()) {
            this.setProfilePicUrl(lmsUser.getProfilePicUrl());
        }
        if(lmsUser.getAddress() != null && !lmsUser.getAddress().isEmpty()) {
            this.setAddress(lmsUser.getAddress());
        }
        if(lmsUser.getCountry() != null && !lmsUser.getCountry().isEmpty()) {
            this.setCountry(lmsUser.getCountry());
        }
        if (lmsUser.getIsAdmin() != null) {
            this.setIsAdmin(lmsUser.getIsAdmin());
        }
        if (lmsUser.getIsStudent() != null) {
            this.setIsStudent(lmsUser.getIsStudent());
        }
        if (lmsUser.getIsInstructor() != null) {
            this.setIsInstructor(lmsUser.getIsInstructor());
        }
        if(lmsUser.getEmailVerified() != null) {
            this.setEmailVerified(lmsUser.getEmailVerified());
        } else {
            this.setEmailVerified(false);
        }
        
    }
}
