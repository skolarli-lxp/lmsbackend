package com.skolarli.lmsservice.models.db.questionbank;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.skolarli.lmsservice.models.AnswerFormat;
import com.skolarli.lmsservice.models.DifficultyLevel;
import com.skolarli.lmsservice.models.QuestionFormat;
import com.skolarli.lmsservice.models.db.core.LmsUser;
import com.skolarli.lmsservice.models.db.core.Tenantable;
import com.skolarli.lmsservice.models.db.course.Batch;
import com.skolarli.lmsservice.models.db.course.Chapter;
import com.skolarli.lmsservice.models.db.course.Course;
import com.skolarli.lmsservice.models.db.course.Lesson;
import com.skolarli.lmsservice.models.db.exam.ExamQuestion;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;
import javax.persistence.*;
import javax.validation.constraints.NotNull;

@MappedSuperclass
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BankQuestion extends Tenantable {

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
    @JoinColumn(name = "chapter_id")
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    @JsonIdentityReference(alwaysAsId = true)
    Chapter chapter;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lesson_id")
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    @JsonIdentityReference(alwaysAsId = true)
    Lesson lesson;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id")
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    @JsonIdentityReference(alwaysAsId = true)
    LmsUser student;

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
    @Column(columnDefinition = "VARCHAR(1024)")
    @NotNull
    private String question;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "resource_id", referencedColumnName = "id")
    private ResourceFile questionResourceFile;

    private String questionType;

    private Integer marks;

    private DifficultyLevel difficultyLevel;
    private QuestionFormat questionFormat;
    private AnswerFormat answerFormat;

    @Column(columnDefinition = "VARCHAR(8192)")
    private String sampleAnswerText;

    private String sampleAnswerUrl;

    private int testAdditionCount;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_time", nullable = false, updatable = false)
    @CreationTimestamp
    private Date createdTime;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "last_updated_time", nullable = false)
    @UpdateTimestamp
    private Date lastUpdatedTime;

    public BankQuestion(ExamQuestion examQuestion) {
        if (examQuestion.getExam() != null && examQuestion.getExam().getCreatedBy() != null) {
            this.createdBy = examQuestion.getExam().getCreatedBy();
        }
        this.question = examQuestion.getQuestion();
        this.questionResourceFile = examQuestion.getQuestionResourceFile();
        this.questionType = examQuestion.getQuestionType();
        this.marks = examQuestion.getMarks();
        this.difficultyLevel = examQuestion.getDifficultyLevel();
        this.questionFormat = examQuestion.getQuestionFormat();
        this.answerFormat = examQuestion.getAnswerFormat();
        this.sampleAnswerText = examQuestion.getSampleAnswerText();
        this.sampleAnswerUrl = examQuestion.getSampleAnswerUrl();
    }

    public void update(BankQuestion question) {
        if (question.getQuestion() != null) {
            this.question = question.getQuestion();
        }
        if (question.getQuestionResourceFile() != null) {
            if (this.questionResourceFile != null) {
                this.questionResourceFile.update(question.getQuestionResourceFile());
            } else {
                this.questionResourceFile = question.getQuestionResourceFile();
            }
        }
        if (question.getQuestionType() != null) {
            this.questionType = question.getQuestionType();
        }
        if (question.getMarks() != null) {
            this.marks = question.getMarks();
        }
        if (question.getDifficultyLevel() != null) {
            this.difficultyLevel = question.getDifficultyLevel();
        }
        if (question.getQuestionFormat() != null) {
            this.questionFormat = question.getQuestionFormat();
        }
        if (question.getAnswerFormat() != null) {
            this.answerFormat = question.getAnswerFormat();
        }
        if (question.getSampleAnswerText() != null) {
            this.sampleAnswerText = question.getSampleAnswerText();
        }
        if (question.getSampleAnswerUrl() != null) {
            this.sampleAnswerUrl = question.getSampleAnswerUrl();
        }
        if (question.getCourse() != null) {
            this.course = question.getCourse();
        }
        if (question.getBatch() != null) {
            this.batch = question.getBatch();
        }
        if (question.getChapter() != null) {
            this.chapter = question.getChapter();
        }
        if (question.getLesson() != null) {
            this.lesson = question.getLesson();
        }
        if (question.getStudent() != null) {
            this.student = question.getStudent();
        }
    }
}
