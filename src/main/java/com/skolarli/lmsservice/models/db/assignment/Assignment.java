package com.skolarli.lmsservice.models.db.assignment;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.skolarli.lmsservice.models.db.core.LmsUser;
import com.skolarli.lmsservice.models.db.core.Tenantable;
import com.skolarli.lmsservice.models.db.course.Batch;
import com.skolarli.lmsservice.models.db.course.BatchSchedule;
import com.skolarli.lmsservice.models.db.course.Course;
import com.skolarli.lmsservice.models.db.resource.LmsResource;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;
import javax.persistence.*;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "assignment")
public class Assignment extends Tenantable {
    private static final Logger logger = LoggerFactory.getLogger(Assignment.class);
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String assignmentName;

    private String assignmentObjective;

    private String assignmentDescription;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "resource_id", referencedColumnName = "id")
    private LmsResource assignmentResourceFile;

    private ZonedDateTime assignmentPublishedDate;

    private ZonedDateTime assignmentDueDate;

    private AssignmentStatus assignmentStatus;

    private String assignmentInstructionsForStudents;

    private String assignmentInstructionsForTeachers;

    private int assignmentTotalMarks;

    @OneToMany(mappedBy = "assignment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AssignmentQuestion> assignmentQuestions;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id")
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    @JsonIdentityReference(alwaysAsId = true)
    LmsUser student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id")
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    @JsonIdentityReference(alwaysAsId = true)
    Course course;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "batch_id")
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    @JsonIdentityReference(alwaysAsId = true)
    Batch batch;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "batch_schedule_id")
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    @JsonIdentityReference(alwaysAsId = true)
    BatchSchedule schedule;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    @JsonIdentityReference(alwaysAsId = true)
    LmsUser createdBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "last_updated_by")
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    @JsonIdentityReference(alwaysAsId = true)
    LmsUser lastUpdatedBy;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_time", nullable = false, updatable = false)
    @CreationTimestamp
    private Date createdTime;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "last_updated_time", nullable = false)
    @UpdateTimestamp
    private Date lastUpdatedTime;


    /*
    assignmnet should have the following fields
    1. assignment id
    2. assignment name
    assignment objective
    3. assignment description
    7. Assignment published date
    4. assignment due date
    5. assignment status

    7. Assignment instructions for students
    8. Assignment instructions for teachers
    9. Total marks

    14. assignment student id
    15. assignment batch id
    16. assignment course id
    13. assignment schedule id

    6. assignment created date
    7. assignment updated date
    10. assignment created by
    11. assignment updated by

    17. assignment tenant id

     6. Assignment questions list


    Assignment questions

    1. question id
    2. question name
    3. question text
    4. question type
    5. additional instructions
    6. question marks
    7. assignment ID


    answer book

     */

}
