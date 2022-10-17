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
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="users", uniqueConstraints = @UniqueConstraint(name= "useremail", columnNames = {"email", "tenantId"}))
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

    //TODO: Better password storage
    @NotNull
    @JsonProperty( value = "password", access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @NotNull
    private Boolean isAdmin;

    @NotNull
    private Boolean isInstructor;

    @NotNull
    private Boolean isStudent;

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
        if (lmsUser.getIsAdmin() != null) {
            this.setIsAdmin(lmsUser.getIsAdmin());
        }
        if (lmsUser.getIsStudent() != null) {
            this.setIsStudent(lmsUser.getIsStudent());
        }
        if (lmsUser.getIsInstructor() != null) {
            this.setIsInstructor(lmsUser.getIsInstructor());
        }
    }
}
