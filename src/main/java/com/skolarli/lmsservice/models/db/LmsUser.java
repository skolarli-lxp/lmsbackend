package com.skolarli.lmsservice.models.db;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@Entity
@Table(name="users", uniqueConstraints = @UniqueConstraint(name= "useremail", columnNames = {"email", "tenantId"}))
public class LmsUser extends Tenantable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String firstName;

    private String lastName;

    private String email;

    //TODO: Better password storage
    private String password;

    private Boolean isAdmin;


    public LmsUser(String firstName, String lastName, String email, String password, Boolean isAdmin) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.email = email;
        this.isAdmin = isAdmin;
    }
}
