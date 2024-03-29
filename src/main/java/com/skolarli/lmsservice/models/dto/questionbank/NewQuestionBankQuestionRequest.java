package com.skolarli.lmsservice.models.dto.questionbank;

import com.skolarli.lmsservice.models.AnswerFormat;
import com.skolarli.lmsservice.models.DifficultyLevel;
import com.skolarli.lmsservice.models.QuestionFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NewQuestionBankQuestionRequest {
    Long courseId;

    Long batchId;

    Long chapterId;

    Long lessonId;

    Long studentId;

    @NotNull
    private String question;

    private NewQuestionResourceFileRequest resourceFileRequest;

    private String questionType;

    private Integer marks;

    private DifficultyLevel difficultyLevel;

    @NotNull
    private QuestionFormat questionFormat;
    @NotNull
    private AnswerFormat answerFormat;

    private String sampleAnswerText;

    private String sampleAnswerUrl;
}
