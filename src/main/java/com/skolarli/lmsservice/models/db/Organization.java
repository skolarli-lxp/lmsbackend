package com.skolarli.lmsservice.models.db;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name="organizations")
public class Organization {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
}
