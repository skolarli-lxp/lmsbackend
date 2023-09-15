package com.skolarli.lmsservice.models.db.feedback;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.skolarli.lmsservice.models.FeedbackType;
import com.skolarli.lmsservice.models.db.core.LmsUser;
import com.skolarli.lmsservice.models.db.core.Tenantable;
import com.skolarli.lmsservice.models.db.course.Batch;
import com.skolarli.lmsservice.models.db.course.BatchSchedule;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;
import java.util.List;
import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "feedback_questionnaires")
public class FeedbackQuestionnaire extends Tenantable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Enumerated(EnumType.STRING)
    @NotNull
    private FeedbackType feedbackType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "batch_id")
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    @JsonIdentityReference(alwaysAsId = true)
    Batch batch;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trainer_id")
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    @JsonIdentityReference(alwaysAsId = true)
    LmsUser trainer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "batch_schedule_id")
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    @JsonIdentityReference(alwaysAsId = true)
    BatchSchedule batchSchedule;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id")
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    @JsonIdentityReference(alwaysAsId = true)
    LmsUser student;

    @OneToMany(mappedBy = "feedbackQuestionnaire", cascade = CascadeType.ALL)
    @JsonIgnoreProperties("feedbackQuestionnaire")
    private List<FeedbackQuestion> questions;

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

    public void update(FeedbackQuestionnaire feedbackQuestionnaire) {
        if (feedbackQuestionnaire.getFeedbackType() != null) {
            this.setFeedbackType(feedbackQuestionnaire.getFeedbackType());
        }
        if (feedbackQuestionnaire.getBatch() != null) {
            this.setBatch(feedbackQuestionnaire.getBatch());
        }
        if (feedbackQuestionnaire.getBatchSchedule() != null) {
            this.setBatchSchedule(feedbackQuestionnaire.getBatchSchedule());
        }
        if (feedbackQuestionnaire.getStudent() != null) {
            this.setStudent(feedbackQuestionnaire.getStudent());
        }
        if (feedbackQuestionnaire.getTrainer() != null) {
            this.setTrainer(feedbackQuestionnaire.getTrainer());
        }
    }
}