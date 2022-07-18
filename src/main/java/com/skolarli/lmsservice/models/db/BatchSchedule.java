package com.skolarli.lmsservice.models.db;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name="attendance")
public class Schedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
}
