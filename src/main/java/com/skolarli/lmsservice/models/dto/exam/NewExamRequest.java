package com.skolarli.lmsservice.models.dto.exam;


import com.skolarli.lmsservice.models.db.ExamQuestionMcq;
import com.skolarli.lmsservice.models.db.ExamQuestionSubjective;
import com.skolarli.lmsservice.models.db.ExamQuestionTrueOrFalse;
import lombok.*;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class NewExamRequest {

    Long courseId;

    Long batchId;

    private String examName;

    private String examType;

    private String durationMins;

    private ZonedDateTime examPublishDate;
    private ZonedDateTime examExpiryDate;

    private Integer totalMarks;

    private Integer passingMarks;

    private List<NewExamQuestionMcqRequest> mcqQuestions;

    private List<NewExamQuestionSubjectiveRequest> subjectiveQuestions;

    private List<NewExamQuestionTrueOrFalseRequest> trueOrFalseQuestions;

    public List<ExamQuestionMcq> toExamQuestionMcqList() {
        if (mcqQuestions == null) {
            return null;
        }
        return mcqQuestions.stream().map(NewExamQuestionMcqRequest::toExamQuestionMcq)
                .collect(Collectors.toList());
    }

    public List<ExamQuestionSubjective> toExamQuestionSubjectiveList() {
        if (subjectiveQuestions == null) {
            return null;
        }
        return subjectiveQuestions.stream()
                .map(NewExamQuestionSubjectiveRequest::toExamQuestionSubjective)
                .collect(Collectors.toList());
    }

    public List<ExamQuestionTrueOrFalse> toExamQuestionTrueOrFalseList() {
        if (trueOrFalseQuestions == null) {
            return null;
        }
        return trueOrFalseQuestions.stream()
                .map(NewExamQuestionTrueOrFalseRequest::toExamQuestionTrueOrFalse)
                .collect(Collectors.toList());
    }

}