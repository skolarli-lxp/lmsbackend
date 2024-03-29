package com.skolarli.lmsservice.models.db.exam;


import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.skolarli.lmsservice.models.ExamStatus;
import com.skolarli.lmsservice.models.db.core.LmsUser;
import com.skolarli.lmsservice.models.db.core.Tenantable;
import com.skolarli.lmsservice.models.db.course.Batch;
import com.skolarli.lmsservice.models.db.course.Chapter;
import com.skolarli.lmsservice.models.db.course.Course;
import com.skolarli.lmsservice.models.db.course.Lesson;
import com.skolarli.lmsservice.models.dto.exam.exam.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "exams")
public class Exam extends Tenantable {
    private static final Logger logger = LoggerFactory.getLogger(Exam.class);
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
    @JoinColumn(name = "student_id")
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    @JsonIdentityReference(alwaysAsId = true)
    LmsUser student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lesson_id")
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    @JsonIdentityReference(alwaysAsId = true)
    Lesson lesson;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chapter_id")
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    @JsonIdentityReference(alwaysAsId = true)
    Chapter chapter;

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
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String examName;

    private String examType;

    private String durationMins;

    private ExamStatus status;

    private ZonedDateTime examPublishDate;
    private ZonedDateTime examExpiryDate;

    private Integer totalMarks;

    private Integer passingMarks;

    @Column(columnDefinition = "bit(1) default false")
    Boolean blockDevTools;

    @Column(columnDefinition = "bit(1) default false")
    Boolean randomizeQuestions;

    @OneToMany(mappedBy = "exam", cascade = CascadeType.ALL)
    private List<ExamQuestionMcq> examQuestionMcqs;

    @OneToMany(mappedBy = "exam", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ExamQuestionSubjective> examQuestionSubjectives;

    @OneToMany(mappedBy = "exam", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ExamQuestionTrueOrFalse> examQuestionTrueOrFalses;


    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_time", nullable = false, updatable = false)
    @CreationTimestamp
    private Date createdTime;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "last_updated_time", nullable = false)
    @UpdateTimestamp
    private Date lastUpdatedTime;

    public void update(Exam exam) {
        if (exam.getCourse() != null) {
            this.course = exam.getCourse();
        }
        if (exam.getBatch() != null) {
            this.batch = exam.getBatch();
        }
        if (exam.getStudent() != null) {
            this.student = exam.getStudent();
        }
        if (exam.getLesson() != null) {
            this.lesson = exam.getLesson();
        }
        if (exam.getChapter() != null) {
            this.chapter = exam.getChapter();
        }
        if (exam.getExamName() != null) {
            this.examName = exam.getExamName();
        }
        if (exam.getExamType() != null) {
            this.examType = exam.getExamType();
        }
        if (exam.getDurationMins() != null) {
            this.durationMins = exam.getDurationMins();
        }
        if (exam.getExamPublishDate() != null) {
            this.examPublishDate = exam.getExamPublishDate();
        }
        if (exam.getExamExpiryDate() != null) {
            this.examExpiryDate = exam.getExamExpiryDate();
        }
        if (exam.getTotalMarks() != null) {
            this.totalMarks = exam.getTotalMarks();
        }
        if (exam.getPassingMarks() != null) {
            this.passingMarks = exam.getPassingMarks();
        }
        if (exam.blockDevTools !=  null) {
            this.blockDevTools = exam.blockDevTools;
        }
        if (exam.randomizeQuestions != null) {
            this.randomizeQuestions = exam.randomizeQuestions;
        }
        if (exam.getExamQuestionMcqs() != null) {
            if (this.examQuestionMcqs != null) {
                this.examQuestionMcqs.addAll(exam.getExamQuestionMcqs());
            } else {
                this.examQuestionMcqs = exam.getExamQuestionMcqs();
            }
        }
        if (exam.getExamQuestionSubjectives() != null) {
            if (this.examQuestionSubjectives != null) {
                this.examQuestionSubjectives.addAll(exam.getExamQuestionSubjectives());
            } else {
                this.examQuestionSubjectives = exam.getExamQuestionSubjectives();
            }
        }
        if (exam.getExamQuestionTrueOrFalses() != null) {
            if (this.examQuestionTrueOrFalses != null) {
                this.examQuestionTrueOrFalses.addAll(exam.getExamQuestionTrueOrFalses());
            } else {
                this.examQuestionTrueOrFalses = exam.getExamQuestionTrueOrFalses();
            }
        }
    }

    public Boolean validateFields() {
        if (course != null && batch != null) {
            if (batch.getCourse().getId() != course.getId()) {
                logger.error("Provided batch doesn't belong to the course");
                return false;
            }
        }

        if (examPublishDate != null && examExpiryDate != null) {
            if (examPublishDate.isAfter(examExpiryDate)) {
                logger.error("Exam publish date is after exam expiry date");
                return false;
            }
        }

        if (totalMarks != null && passingMarks != null) {
            if (totalMarks < passingMarks) {
                logger.error("Total marks is less than passing marks");
                return false;
            }
        }

        if (examQuestionMcqs != null) {
            for (ExamQuestionMcq examQuestionMcq : examQuestionMcqs) {
                if (!examQuestionMcq.validateFields()) {
                    logger.error("Exam question mcq validation failed");
                    return false;
                }
            }
        }

        return true;
    }

    public NewExamQuestionsAllTypesResponse fetchAllExamQuestions() {
        NewExamQuestionsAllTypesResponse newExamQuestionsAllTypesResponse
                = new NewExamQuestionsAllTypesResponse();
        newExamQuestionsAllTypesResponse.setMcqQuestions(examQuestionMcqs);
        newExamQuestionsAllTypesResponse.setSubjectiveQuestions(examQuestionSubjectives);
        newExamQuestionsAllTypesResponse.setTrueOrFalseQuestions(examQuestionTrueOrFalses);
        return newExamQuestionsAllTypesResponse;
    }

    public void addMcqQuestions(List<NewExamQuestionMcqRequest> newExamQuestionMcqRequests) {
        List<ExamQuestionMcq> examQuestionMcqs = newExamQuestionMcqRequests.stream()
                .map(NewExamQuestionMcqRequest::toExamQuestionMcq)
                .collect(Collectors.toList());
        examQuestionMcqs.forEach(examQuestionMcq -> examQuestionMcq.setExam(this));

        if (this.examQuestionMcqs == null) {
            this.examQuestionMcqs = examQuestionMcqs;
        } else {
            this.examQuestionMcqs.addAll(examQuestionMcqs);
        }
    }

    public void addSubjectiveQuestions(List<NewExamQuestionSubjectiveRequest>
                                               newExamQuestionSubjectiveRequests) {
        List<ExamQuestionSubjective> examQuestionSubjectives = newExamQuestionSubjectiveRequests
                .stream()
                .map(NewExamQuestionSubjectiveRequest::toExamQuestionSubjective)
                .collect(Collectors.toList());
        examQuestionSubjectives.forEach(examQuestionSubjective
                -> examQuestionSubjective.setExam(this));
        if (this.examQuestionSubjectives == null) {
            this.examQuestionSubjectives = examQuestionSubjectives;
        } else {
            this.examQuestionSubjectives.addAll(examQuestionSubjectives);
        }
    }

    public void addTrueOrFalseQuestions(List<NewExamQuestionTrueOrFalseRequest>
                                                newExamQuestionTrueOrFalseRequests) {
        List<ExamQuestionTrueOrFalse> examQuestionTrueOrFalses = newExamQuestionTrueOrFalseRequests
                .stream()
                .map(NewExamQuestionTrueOrFalseRequest::toExamQuestionTrueOrFalse)
                .collect(Collectors.toList());
        examQuestionTrueOrFalses.forEach(examQuestionTrueOrFalse -> examQuestionTrueOrFalse
                .setExam(this));
        if (this.examQuestionTrueOrFalses == null) {
            this.examQuestionTrueOrFalses = examQuestionTrueOrFalses;
        } else {
            this.examQuestionTrueOrFalses.addAll(examQuestionTrueOrFalses);
        }
    }

    public QuestionSortOrderResponse toQuestionSortOrderResponse() {
        QuestionSortOrderResponse questionSortOrderResponse = new QuestionSortOrderResponse();
        questionSortOrderResponse.setExamId(this.id);
        List<IndividualQuestionSortOrder> mcqQuestions = this.examQuestionMcqs.stream()
                .map(examQuestionMcq -> new IndividualQuestionSortOrder(examQuestionMcq.getId(),
                        examQuestionMcq.getQuestionSortOrder()))
                .collect(Collectors.toList());
        List<IndividualQuestionSortOrder> subjectiveQuestions = this.examQuestionSubjectives.stream()
                .map(examQuestionSubjective -> new IndividualQuestionSortOrder(
                        examQuestionSubjective.getId(),
                        examQuestionSubjective.getQuestionSortOrder()))
                .collect(Collectors.toList());
        List<IndividualQuestionSortOrder> trueOrFalseQuestions = this.examQuestionTrueOrFalses
                .stream()
                .map(examQuestionTrueOrFalse -> new IndividualQuestionSortOrder(
                        examQuestionTrueOrFalse.getId(),
                        examQuestionTrueOrFalse.getQuestionSortOrder()))
                .collect(Collectors.toList());
        questionSortOrderResponse.setMcqQuestions(mcqQuestions);
        questionSortOrderResponse.setSubjectiveQuestions(subjectiveQuestions);
        questionSortOrderResponse.setTrueOrFalseQuestions(trueOrFalseQuestions);
        return questionSortOrderResponse;
    }

    public void changeExamStatus(ExamStatus status) {
        this.status = status;
        //TODO: Exam status should be deactivated only if there are no AnswerBooks in the DRAFT state
    }
}
