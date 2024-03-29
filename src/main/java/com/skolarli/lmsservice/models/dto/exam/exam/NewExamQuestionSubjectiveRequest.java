package com.skolarli.lmsservice.models.dto.exam.exam;

import com.skolarli.lmsservice.models.db.exam.ExamQuestionSubjective;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class NewExamQuestionSubjectiveRequest extends NewExamQuestionRequest {
    private Integer wordCount;

    private String correctAnswer;

    public ExamQuestionSubjective toExamQuestionSubjective() {
        ExamQuestionSubjective examQuestionSubjective = new ExamQuestionSubjective();
        super.toExamQuestion(examQuestionSubjective);
        examQuestionSubjective.setWordCount(wordCount);
        examQuestionSubjective.setCorrectAnswer(correctAnswer);
        return examQuestionSubjective;
    }
}