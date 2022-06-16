package com.skolarli.lmsservice.models.db;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="tenants", uniqueConstraints = @UniqueConstraint(name= "domainname", columnNames = "domainName"))
public class Tenant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NonNull
    private String companyName;

    @NonNull
    private String domainName;

    private String website;

    public Tenant(String domainName, String companyName, String website) {
        this.domainName = domainName;
        this.companyName = companyName;
        this.website = website;
    }
}
