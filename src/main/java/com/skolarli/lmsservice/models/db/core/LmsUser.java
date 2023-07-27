package com.skolarli.lmsservice.models.db.core;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.skolarli.lmsservice.models.db.course.Attendance;
import com.skolarli.lmsservice.models.db.course.Batch;
import com.skolarli.lmsservice.models.db.course.Course;
import com.skolarli.lmsservice.models.db.course.Enrollment;
import com.skolarli.lmsservice.models.dto.core.NewDomainRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Where;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.ZonedDateTime;
import java.util.List;
import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users", uniqueConstraints = {
    @UniqueConstraint(name = "useremail", columnNames = {"email", "tenantId"}),
    @UniqueConstraint(name = "passwordtoken", columnNames = {"passwordResetToken"})
})
@Where(clause = "user_is_deleted is null or user_is_deleted = false")
public class LmsUser extends Tenantable {
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
    private String occupation;
    private String education;

    @Column(columnDefinition = "TEXT")
    private String userBio;

    @Column(columnDefinition = "VARCHAR(1024)")
    private String profilePicUrl;

    @Column(columnDefinition = "TEXT")
    private String address;

    private String country;


    //TODO: Better password storage
    @NotNull
    @JsonProperty(value = "password", access = JsonProperty.Access.WRITE_ONLY)
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

    @Column(columnDefinition = "bit(1) default false")
    private Boolean isSuperAdmin;

    @Column(columnDefinition = "bit(1) default false")
    private Boolean emailVerified;

    @OneToMany(mappedBy = "courseOwner", fetch = FetchType.LAZY)
    @JsonIgnoreProperties("courseOwner") // To avoid infinite recursion during serialization
    private List<Course> courses;

    @OneToMany(mappedBy = "instructor", fetch = FetchType.LAZY)
    @JsonIgnoreProperties("instructor")
    private List<Batch> batches;

    @OneToMany(mappedBy = "student", fetch = FetchType.LAZY)
    @JsonIgnoreProperties("student")
    private List<Attendance> attendanceList;

    @OneToMany(mappedBy = "student", fetch = FetchType.LAZY)
    @JsonIgnoreProperties("student")
    private List<Enrollment> enrollments;

    @OneToOne(mappedBy = "user")
    @JsonIgnoreProperties("user")
    private VerificationCode verificationCode;

    private Boolean userIsDeleted;

    private Boolean passwordResetRequested;

    private String passwordResetToken;

    private ZonedDateTime passwordResetTokenExpiry;

    public void setPassword(String password) {
        //This is used by default during Json deserialization
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        this.password = passwordEncoder.encode(password);
    }

    public void setPasswordWithoutEncoding(String password) {
        this.password = password;
    }

    public LmsUser(NewDomainRequest newDomainRequest) {
        this.firstName = newDomainRequest.getFirstName();
        this.lastName = newDomainRequest.getLastName();
        this.email = newDomainRequest.getEmail();
        this.setPassword(newDomainRequest.getPassword());
        this.isAdmin = true;
        this.isStudent = false;
        this.isInstructor = false;
        this.isSuperAdmin = false;
        this.userIsDeleted = false;
        this.emailVerified = false;
    }

    public void update(LmsUser lmsUser) {
        if (lmsUser.getFirstName() != null && !lmsUser.getFirstName().isEmpty()) {
            this.setFirstName(lmsUser.getFirstName());
        }
        if (lmsUser.getLastName() != null && !lmsUser.getLastName().isEmpty()) {
            this.setLastName(lmsUser.getLastName());
        }
        if (lmsUser.getPassword() != null && !lmsUser.getPassword().isEmpty()) {
            this.setPasswordWithoutEncoding(lmsUser.getPassword());
        }
        if (lmsUser.getPhoneNumber() != null && !lmsUser.getPhoneNumber().isEmpty()) {
            this.setPhoneNumber(lmsUser.getPhoneNumber());
        }
        if (lmsUser.getUserBio() != null && !lmsUser.getUserBio().isEmpty()) {
            this.setUserBio(lmsUser.getUserBio());
        }
        if (lmsUser.getCompanyName() != null && !lmsUser.getCompanyName().isEmpty()) {
            this.setCompanyName(lmsUser.getCompanyName());
        }
        if (lmsUser.getExperience() != 0) {
            this.setExperience(lmsUser.getExperience());
        }
        if (lmsUser.getOccupation() != null) {
            this.setOccupation(lmsUser.getOccupation());
        }
        if (lmsUser.getEducation() != null) {
            this.setEducation(lmsUser.getEducation());
        }
        if (lmsUser.getProfilePicUrl() != null && !lmsUser.getProfilePicUrl().isEmpty()) {
            this.setProfilePicUrl(lmsUser.getProfilePicUrl());
        }
        if (lmsUser.getAddress() != null && !lmsUser.getAddress().isEmpty()) {
            this.setAddress(lmsUser.getAddress());
        }
        if (lmsUser.getCountry() != null && !lmsUser.getCountry().isEmpty()) {
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
        if (lmsUser.getEmailVerified() != null) {
            this.setEmailVerified(lmsUser.getEmailVerified());
        }
    }
}