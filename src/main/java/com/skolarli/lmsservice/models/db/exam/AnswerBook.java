package com.skolarli.lmsservice.models.db.exam;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.skolarli.lmsservice.models.AnswerBookStatus;
import com.skolarli.lmsservice.models.db.core.LmsUser;
import com.skolarli.lmsservice.models.db.core.Tenantable;
import com.skolarli.lmsservice.models.db.course.Batch;
import com.skolarli.lmsservice.models.db.course.Course;
import lombok.*;
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
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "answer_books")
public class AnswerBook extends Tenantable {
    private static final Logger logger = LoggerFactory.getLogger(AnswerBook.class);

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exam_id")
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    @JsonIdentityReference(alwaysAsId = true)
    private Exam exam;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id")
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    @JsonIdentityReference(alwaysAsId = true)
    private LmsUser student;

    AnswerBookStatus status;

    private Integer totalMarks;

    private Integer obtainedMarks;

    private Integer additionalMarks;

    private Integer totalQuestions;

    private Integer attemptedQuestions;

    private Integer correctAnswers;

    private Integer incorrectAnswers;

    private Integer totalDuration;

    private Integer timeTaken;

    private ZonedDateTime sessionStartTime;

    private ZonedDateTime sessionEndTime;

    private String remarks;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "answer_book_id")
    @JsonIgnoreProperties("answerBook")
    private List<AnswerMcq> mcqAnswers;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "answer_book_id")
    @JsonIgnoreProperties("answerBook")
    private List<AnswerSubjective> subjectiveAnswers;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "answer_book_id")
    @JsonIgnoreProperties("answerBook")
    private List<AnswerTrueFalse> trueFalseAnswers;

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
    @JoinColumn(name = "created_by")
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    @JsonIdentityReference(alwaysAsId = true)
    LmsUser createdBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "updated_by")
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    @JsonIdentityReference(alwaysAsId = true)
    LmsUser updatedBy;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_time", nullable = false, updatable = false)
    @CreationTimestamp
    private Date createdTime;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "last_updated_time", nullable = false)
    @UpdateTimestamp
    private Date lastUpdatedTime;

    public void update(AnswerBook answerBook) {
        if (answerBook.getStatus() != null) {
            this.setStatus(answerBook.getStatus());
        }
        if (answerBook.getTotalMarks() != null) {
            this.setTotalMarks(answerBook.getTotalMarks());
        }
        if (answerBook.getObtainedMarks() != null) {
            this.setObtainedMarks(answerBook.getObtainedMarks());
        }
        if (answerBook.getAdditionalMarks() != null) {
            this.setAdditionalMarks(answerBook.getAdditionalMarks());
        }
        if (answerBook.getTotalQuestions() != null) {
            this.setTotalQuestions(answerBook.getTotalQuestions());
        }
        if (answerBook.getAttemptedQuestions() != null) {
            this.setAttemptedQuestions(answerBook.getAttemptedQuestions());
        }
        if (answerBook.getCorrectAnswers() != null) {
            this.setCorrectAnswers(answerBook.getCorrectAnswers());
        }
        if (answerBook.getIncorrectAnswers() != null) {
            this.setIncorrectAnswers(answerBook.getIncorrectAnswers());
        }
        if (answerBook.getTotalDuration() != null) {
            this.setTotalDuration(answerBook.getTotalDuration());
        }
        if (answerBook.getTimeTaken() != null) {
            this.setTimeTaken(answerBook.getTimeTaken());
        }
        if (answerBook.getSessionStartTime() != null) {
            this.setSessionStartTime(answerBook.getSessionStartTime());
        }
        if (answerBook.getSessionEndTime() != null) {
            this.setSessionEndTime(answerBook.getSessionEndTime());
        }
        if (answerBook.getRemarks() != null) {
            this.setRemarks(answerBook.getRemarks());
        }
        if (answerBook.getMcqAnswers() != null) {
            if (this.getMcqAnswers() != null) {
                this.getMcqAnswers().addAll(answerBook.getMcqAnswers());
            } else {
                this.setMcqAnswers(answerBook.getMcqAnswers());
            }
        }
        if (answerBook.getSubjectiveAnswers() != null) {
            if (this.getSubjectiveAnswers() != null) {
                this.getSubjectiveAnswers().addAll(answerBook.getSubjectiveAnswers());
            } else {
                this.setSubjectiveAnswers(answerBook.getSubjectiveAnswers());
            }
        }
        if (answerBook.getTrueFalseAnswers() != null) {
            if (this.getTrueFalseAnswers() != null) {
                this.getTrueFalseAnswers().addAll(answerBook.getTrueFalseAnswers());
            } else {
                this.setTrueFalseAnswers(answerBook.getTrueFalseAnswers());
            }
        }
        if (answerBook.getCourse() != null) {
            this.setCourse(answerBook.getCourse());
        }
        if (answerBook.getBatch() != null) {
            this.setBatch(answerBook.getBatch());
        }
        // Created by should not be updated
        if (answerBook.getUpdatedBy() != null) {
            this.setUpdatedBy(answerBook.getUpdatedBy());
        }
    }
}
