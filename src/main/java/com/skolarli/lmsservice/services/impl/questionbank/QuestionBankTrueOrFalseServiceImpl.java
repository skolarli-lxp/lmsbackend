package com.skolarli.lmsservice.services.impl.questionbank;

import com.skolarli.lmsservice.exception.OperationNotSupportedException;
import com.skolarli.lmsservice.exception.ResourceNotFoundException;
import com.skolarli.lmsservice.models.db.core.LmsUser;
import com.skolarli.lmsservice.models.db.course.Batch;
import com.skolarli.lmsservice.models.db.course.Course;
import com.skolarli.lmsservice.models.db.exam.Exam;
import com.skolarli.lmsservice.models.db.exam.ExamQuestionTrueOrFalse;
import com.skolarli.lmsservice.models.db.questionbank.BankQuestionTrueOrFalse;
import com.skolarli.lmsservice.models.dto.questionbank.NewBankQuestionTrueOrFalseRequest;
import com.skolarli.lmsservice.repository.exam.ExamQuestionTrueOrFalseRepository;
import com.skolarli.lmsservice.repository.questionbank.QuestionBankTrueOrFalseRepository;
import com.skolarli.lmsservice.services.core.LmsUserService;
import com.skolarli.lmsservice.services.course.BatchService;
import com.skolarli.lmsservice.services.course.ChapterService;
import com.skolarli.lmsservice.services.course.CourseService;
import com.skolarli.lmsservice.services.course.LessonService;
import com.skolarli.lmsservice.services.exam.ExamService;
import com.skolarli.lmsservice.services.questionbank.QuestionBankTrueOrFalseService;
import com.skolarli.lmsservice.utils.UserUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;


@Service
public class QuestionBankTrueOrFalseServiceImpl implements QuestionBankTrueOrFalseService {

    final QuestionBankTrueOrFalseRepository questionBankTrueOrFalseRepository;
    final ExamQuestionTrueOrFalseRepository examQuestionTrueOrFalseRepository;
    final UserUtils userUtils;
    final CourseService courseService;
    final BatchService batchService;
    final ChapterService chapterService;
    final LessonService lessonService;
    final LmsUserService lmsUserService;
    Logger logger = LoggerFactory.getLogger(QuestionBankTrueOrFalseServiceImpl.class);
    ExamService examService;

    Job job;

    JobLauncher jobLauncher;

    public QuestionBankTrueOrFalseServiceImpl(QuestionBankTrueOrFalseRepository
                                                  questionBankTrueOrFalseRepository,
                                              ExamQuestionTrueOrFalseRepository
                                                  examQuestionTrueOrFalseRepository,
                                              UserUtils userUtils, CourseService courseService,
                                              BatchService batchService,
                                              ChapterService chapterService,
                                              LessonService lessonService,
                                              LmsUserService lmsUserService,
                                              ExamService examService,
                                              @Qualifier("importTrueFalseQuestionsJob") Job job,
                                              @Qualifier("AsyncJobLauncher3") JobLauncher jobLauncher) {
        this.questionBankTrueOrFalseRepository = questionBankTrueOrFalseRepository;
        this.examQuestionTrueOrFalseRepository = examQuestionTrueOrFalseRepository;
        this.userUtils = userUtils;
        this.courseService = courseService;
        this.batchService = batchService;
        this.chapterService = chapterService;
        this.lessonService = lessonService;
        this.lmsUserService = lmsUserService;
        this.examService = examService;
    }

    private Boolean checkPermission() {
        LmsUser currentUser = userUtils.getCurrentUser();
        return currentUser.getIsAdmin() || currentUser.getIsInstructor();
    }


    @Override
    public BankQuestionTrueOrFalse toBankQuestionTrueOrFalse(
        NewBankQuestionTrueOrFalseRequest newBankQuestionTrueOrFalseRequest) {
        BankQuestionTrueOrFalse bankQuestionTrueOrFalse = new BankQuestionTrueOrFalse();

        if (newBankQuestionTrueOrFalseRequest.getCourseId() != null) {
            Course course = courseService.getCourseById(
                newBankQuestionTrueOrFalseRequest.getCourseId());
            bankQuestionTrueOrFalse.setCourse(course);
        }
        if (newBankQuestionTrueOrFalseRequest.getBatchId() != null) {
            Batch batch = batchService.getBatch(
                newBankQuestionTrueOrFalseRequest.getBatchId());
            bankQuestionTrueOrFalse.setBatch(batch);
        }
        if (newBankQuestionTrueOrFalseRequest.getChapterId() != null) {
            bankQuestionTrueOrFalse.setChapter(chapterService.getChapterById(
                newBankQuestionTrueOrFalseRequest.getChapterId()));
        }
        if (newBankQuestionTrueOrFalseRequest.getLessonId() != null) {
            bankQuestionTrueOrFalse.setLesson(lessonService.getLessonById(
                newBankQuestionTrueOrFalseRequest.getLessonId()));
        }
        if (newBankQuestionTrueOrFalseRequest.getStudentId() != null) {
            bankQuestionTrueOrFalse.setStudent(lmsUserService.getLmsUserById(
                newBankQuestionTrueOrFalseRequest.getStudentId()));
        }
        bankQuestionTrueOrFalse.setQuestion(newBankQuestionTrueOrFalseRequest.getQuestion());
        bankQuestionTrueOrFalse.setQuestionType(newBankQuestionTrueOrFalseRequest.getQuestionType());
        bankQuestionTrueOrFalse.setMarks(newBankQuestionTrueOrFalseRequest.getMarks());
        bankQuestionTrueOrFalse.setDifficultyLevel(newBankQuestionTrueOrFalseRequest.getDifficultyLevel());

        bankQuestionTrueOrFalse.setQuestionFormat(
            newBankQuestionTrueOrFalseRequest.getQuestionFormat());
        bankQuestionTrueOrFalse.setAnswerFormat(
            newBankQuestionTrueOrFalseRequest.getAnswerFormat());
        bankQuestionTrueOrFalse.setSampleAnswerText(newBankQuestionTrueOrFalseRequest
            .getSampleAnswerText());
        bankQuestionTrueOrFalse.setSampleAnswerUrl(newBankQuestionTrueOrFalseRequest
            .getSampleAnswerUrl());

        bankQuestionTrueOrFalse.setOption1(newBankQuestionTrueOrFalseRequest.getOption1());
        bankQuestionTrueOrFalse.setOption2(newBankQuestionTrueOrFalseRequest.getOption2());
        bankQuestionTrueOrFalse.setCorrectAnswer(
            newBankQuestionTrueOrFalseRequest.getCorrectAnswer());

        return bankQuestionTrueOrFalse;
    }

    public ExamQuestionTrueOrFalse toExamQuestionTrueOrFalse(
        BankQuestionTrueOrFalse bankQuestionTrueOrFalse, Integer marks,
        Exam existingExam) {

        ExamQuestionTrueOrFalse examQuestionTrueOrFalse = new ExamQuestionTrueOrFalse(
            bankQuestionTrueOrFalse, marks, existingExam);
        return examQuestionTrueOrFalse;
    }

    @Override
    public List<ExamQuestionTrueOrFalse> toExamQuestionTrueOrFalse(List<Long> bankQuestionTrueOrFalseIds,
                                                                   List<Integer> marks, Long examId) {
        Exam existingExam = examService.getExam(examId);
        if (existingExam == null) {
            throw new ResourceNotFoundException("Exam with Id " + examId + " not found");
        }

        if (marks == null) {
            marks = new ArrayList<>();
        }
        if (bankQuestionTrueOrFalseIds.size() != marks.size()) {
            for (int i = 0; i < bankQuestionTrueOrFalseIds.size() - marks.size(); i++) {
                marks.add(1);
            }
        }
        List<BankQuestionTrueOrFalse> bankQuestionTrueOrFalse =
            questionBankTrueOrFalseRepository.findAllById(bankQuestionTrueOrFalseIds);
        List<ExamQuestionTrueOrFalse> examQuestionTrueOrFalses = new ArrayList<>();
        for (int i = 0; i < bankQuestionTrueOrFalse.size(); i++) {
            examQuestionTrueOrFalses.add(toExamQuestionTrueOrFalse(
                bankQuestionTrueOrFalse.get(i), marks.get(i), existingExam));
        }

        List<ExamQuestionTrueOrFalse> savedQuestions =
            examQuestionTrueOrFalseRepository.saveAll(examQuestionTrueOrFalses);
        return savedQuestions;
    }

    @Override
    public BankQuestionTrueOrFalse getQuestion(long id) {
        List<BankQuestionTrueOrFalse> bankQuestionTrueOrFalses =
            questionBankTrueOrFalseRepository.findAllById(new ArrayList<>(List.of(id)));
        if (bankQuestionTrueOrFalses.size() == 0) {
            throw new ResourceNotFoundException("Question with Id " + id + " not found");
        }
        return bankQuestionTrueOrFalses.get(0);
    }

    @Override
    public List<BankQuestionTrueOrFalse> getAllQuestions() {
        return questionBankTrueOrFalseRepository.findAll();
    }

    @Override
    public List<BankQuestionTrueOrFalse> getQuestionsByParameters(Long courseId, Long batchId,
                                                                  Long lessonId, Long chapterId,
                                                                  Long studentId) {
        Double courseIdDouble = courseId != null ? courseId.doubleValue() : null;
        Double batchIdDouble = batchId != null ? batchId.doubleValue() : null;
        Double lessonIdDouble = lessonId != null ? lessonId.doubleValue() : null;
        Double chapterIdDouble = chapterId != null ? chapterId.doubleValue() : null;
        Double studentIdDouble = studentId != null ? studentId.doubleValue() : null;

        return questionBankTrueOrFalseRepository.findQuestionsByParameters(courseIdDouble,
            batchIdDouble, lessonIdDouble, chapterIdDouble, studentIdDouble);
    }

    @Override
    public BankQuestionTrueOrFalse saveQuestion(BankQuestionTrueOrFalse question) {
        LmsUser currentUser = userUtils.getCurrentUser();
        question.setCreatedBy(currentUser);
        if (checkPermission()) {
            return questionBankTrueOrFalseRepository.save(question);
        } else {
            throw new OperationNotSupportedException("User does not have permission to perform "
                + "this operation");
        }
    }

    @Override
    public List<BankQuestionTrueOrFalse> saveAllQuestions(List<BankQuestionTrueOrFalse> questions) {
        LmsUser currentUser = userUtils.getCurrentUser();
        questions.forEach(question -> question.setCreatedBy(currentUser));
        if (checkPermission()) {
            return questionBankTrueOrFalseRepository.saveAll(questions);
        } else {
            throw new OperationNotSupportedException("User does not have permission to perform "
                + "this operation");
        }
    }

    @Override
    public Long uploadQuestionsFromCsvBatchJob(String filePath, Long tenantId, String userName) {
        logger.info("Uploading questions from csv file: " + filePath);
        File file = Paths.get(filePath).toAbsolutePath().toFile();

        if (!file.exists()) {
            logger.error("File does not exist: " + filePath);
            return 0L;
        }
        try {

            JobParameters jobParameters = new JobParametersBuilder()
                .addString("fullPathFileName", filePath)
                .addLong("startAt", System.currentTimeMillis())
                .addLong("tenantId", tenantId)
                .addString("userName", userName)
                .toJobParameters();

            JobExecution execution = jobLauncher.run(job, jobParameters);
            return execution.getJobId();
        } catch (Exception e) {
            logger.error("Error creating file: " + e.getMessage());
            return 0L;
        }
    }

    @Override
    public BankQuestionTrueOrFalse updateQuestion(BankQuestionTrueOrFalse question, long id) {
        LmsUser currentUser = userUtils.getCurrentUser();
        BankQuestionTrueOrFalse existingQuestion = getQuestion(id);
        if (!currentUser.getIsAdmin() && existingQuestion.getCreatedBy() != currentUser) {
            throw new OperationNotSupportedException("User does not have permission to perform "
                + "this operation");
        }
        existingQuestion.setUpdatedBy(currentUser);
        existingQuestion.update(question);
        return questionBankTrueOrFalseRepository.save(existingQuestion);
    }

    @Override
    public void hardDeleteQuestion(long id) {
        LmsUser currentUser = userUtils.getCurrentUser();
        BankQuestionTrueOrFalse existingQuestion = getQuestion(id);
        if (!currentUser.getIsAdmin() && existingQuestion.getCreatedBy() != currentUser) {
            throw new OperationNotSupportedException("User does not have permission to perform "
                + "this operation");
        }
        questionBankTrueOrFalseRepository.delete(existingQuestion);
    }
}
