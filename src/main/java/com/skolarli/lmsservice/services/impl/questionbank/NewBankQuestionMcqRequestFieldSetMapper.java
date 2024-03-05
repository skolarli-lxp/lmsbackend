package com.skolarli.lmsservice.services.impl.questionbank;

import com.skolarli.lmsservice.models.AnswerFormat;
import com.skolarli.lmsservice.models.DifficultyLevel;
import com.skolarli.lmsservice.models.QuestionFormat;
import com.skolarli.lmsservice.models.dto.questionbank.NewBankQuestionMcqRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class NewBankQuestionMcqRequestFieldSetMapper implements FieldSetMapper<NewBankQuestionMcqRequest> {
    private static final Logger logger = LoggerFactory.getLogger(NewBankQuestionMcqRequestFieldSetMapper.class);

    @Override
    public NewBankQuestionMcqRequest mapFieldSet(FieldSet fieldSet) throws BindException {
        NewBankQuestionMcqRequest request = new NewBankQuestionMcqRequest();
        request.setQuestion(fieldSet.readString("Question"));
        request.setNumberOfOptions(fieldSet.readInt("NumberOfOptions"));
        request.setOption1(fieldSet.readString("Option1"));
        request.setOption2(fieldSet.readString("Option2"));
        request.setOption3(fieldSet.readString("Option3"));
        request.setOption4(fieldSet.readString("Option4"));
        request.setOption5(fieldSet.readString("Option5"));
        request.setOption6(fieldSet.readString("Option6"));
        request.setNumberOfCorrectAnswers(fieldSet.readInt("NumberOfCorrectAnswers"));
        String correctAnswersStr = fieldSet.readString("CorrectAnswer");
        if (correctAnswersStr != null && !correctAnswersStr.isEmpty()) {
            correctAnswersStr = correctAnswersStr.replace("[", "");
            correctAnswersStr = correctAnswersStr.replace("]", "");
            List<Integer> correctAnswers = Arrays.stream(correctAnswersStr.split(","))
                .map(Integer::parseInt)
                .collect(Collectors.toList());
            request.setCorrectAnswer(correctAnswers);
        }
        if (fieldSet.readString("Marks") != null && !fieldSet.readString("Marks").isEmpty()) {
            try {
                request.setMarks(fieldSet.readInt("Marks"));
            } catch (NumberFormatException e) {
                logger.error("Marks is not a number, setting marks to 0");
                request.setMarks(0);
            }
        }
        request.setSampleAnswerText(fieldSet.readString("SampleAnswerText"));
        request.setSampleAnswerUrl(fieldSet.readString("SampleAnswerUrl"));
        request.setQuestionType(fieldSet.readString("QuestionType"));
        if (fieldSet.readString("DifficultyLevel") != null && !fieldSet.readString("DifficultyLevel").isEmpty()) {
            DifficultyLevel difficultyLevel = DifficultyLevel.valueOf(fieldSet.readString("DifficultyLevel"));
            request.setDifficultyLevel(difficultyLevel);
        }
        if (fieldSet.readString("QuestionFormat") != null && !fieldSet.readString("QuestionFormat").isEmpty()) {
            QuestionFormat questionFormat = QuestionFormat.valueOf(fieldSet.readString("QuestionFormat"));
            request.setQuestionFormat(questionFormat);
        }
        if (fieldSet.readString("AnswerFormat") != null && !fieldSet.readString("AnswerFormat").isEmpty()) {
            AnswerFormat answerFormat = AnswerFormat.valueOf(fieldSet.readString("AnswerFormat"));
            request.setAnswerFormat(answerFormat);
        }
        String courseId = fieldSet.readString("CourseId");
        if (courseId != null && !courseId.isEmpty()) {
            request.setCourseId(Long.parseLong(courseId));
        }
        String batchId = fieldSet.readString("BatchId");
        if (batchId != null && !batchId.isEmpty()) {
            request.setBatchId(Long.parseLong(batchId));
        }
        String chapterId = fieldSet.readString("ChapterId");
        if (chapterId != null && !chapterId.isEmpty()) {
            request.setChapterId(Long.parseLong(chapterId));
        }
        String lessonId = fieldSet.readString("LessonId");
        if (lessonId != null && !lessonId.isEmpty()) {
            request.setLessonId(Long.parseLong(lessonId));
        }
        String studentId = fieldSet.readString("StudentId");
        if (studentId != null && !studentId.isEmpty()) {
            request.setStudentId(Long.parseLong(studentId));
        }
        return request;
    }
}