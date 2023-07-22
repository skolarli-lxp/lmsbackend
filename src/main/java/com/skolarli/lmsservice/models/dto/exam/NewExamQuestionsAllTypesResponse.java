package com.skolarli.lmsservice.models.dto.exam;

import com.skolarli.lmsservice.models.db.exam.ExamQuestionMcq;
import com.skolarli.lmsservice.models.db.exam.ExamQuestionSubjective;
import com.skolarli.lmsservice.models.db.exam.ExamQuestionTrueOrFalse;
import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class NewExamQuestionsAllTypesResponse {

    List<ExamQuestionMcq> mcqQuestions;
    List<ExamQuestionSubjective> subjectiveQuestions;
    List<ExamQuestionTrueOrFalse> trueOrFalseQuestions;
}