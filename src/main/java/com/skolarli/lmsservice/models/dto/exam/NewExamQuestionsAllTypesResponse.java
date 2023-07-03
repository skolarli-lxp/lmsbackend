package com.skolarli.lmsservice.models.dto.exam;

import com.skolarli.lmsservice.models.db.ExamQuestionMcq;
import com.skolarli.lmsservice.models.db.ExamQuestionSubjective;
import com.skolarli.lmsservice.models.db.ExamQuestionTrueOrFalse;
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