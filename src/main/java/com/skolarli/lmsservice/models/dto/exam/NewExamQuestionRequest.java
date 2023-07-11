package com.skolarli.lmsservice.models.dto.exam;

import com.skolarli.lmsservice.models.AnswerFormat;
import com.skolarli.lmsservice.models.DifficultyLevel;
import com.skolarli.lmsservice.models.QuestionFormat;
import com.skolarli.lmsservice.models.db.ExamQuestion;
import com.skolarli.lmsservice.models.dto.questionbank.NewQuestionResourceFileRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NewExamQuestionRequest {
    @NotNull
    private String question;

    private NewQuestionResourceFileRequest resourceFileRequest;


    private String questionType;

    private DifficultyLevel difficultyLevel;

    @NotNull
    private QuestionFormat questionFormat;
    @NotNull
    private AnswerFormat answerFormat;

    private String sampleAnswerText;

    private String sampleAnswerUrl;

    private Integer marks;

    public void toExamQuestion(ExamQuestion examQuestion) {
        examQuestion.setQuestion(question);
        if (resourceFileRequest != null) {
            examQuestion.setQuestionResourceFile(resourceFileRequest.toResourceFile());
        }
        examQuestion.setQuestionType(questionType);
        examQuestion.setDifficultyLevel(difficultyLevel);
        examQuestion.setQuestionFormat(questionFormat);
        examQuestion.setAnswerFormat(answerFormat);
        examQuestion.setSampleAnswerText(sampleAnswerText);
        examQuestion.setSampleAnswerUrl(sampleAnswerUrl);
        examQuestion.setMarks(marks);
    }
}
