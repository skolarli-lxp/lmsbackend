package com.skolarli.lmsservice.models.db;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name="batches")
public class Batch {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private long courseId;

    private long instructor;

    private long schedule;
}
