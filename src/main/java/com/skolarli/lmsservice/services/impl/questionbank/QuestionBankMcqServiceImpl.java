package com.skolarli.lmsservice.services.impl.questionbank;

import com.skolarli.lmsservice.authentications.TenantAuthenticationToken;
import com.skolarli.lmsservice.contexts.TenantContext;
import com.skolarli.lmsservice.exception.OperationNotSupportedException;
import com.skolarli.lmsservice.exception.ResourceNotFoundException;
import com.skolarli.lmsservice.exception.ValidationFailureException;
import com.skolarli.lmsservice.models.db.core.LmsUser;
import com.skolarli.lmsservice.models.db.course.Batch;
import com.skolarli.lmsservice.models.db.course.Course;
import com.skolarli.lmsservice.models.db.exam.Exam;
import com.skolarli.lmsservice.models.db.exam.ExamQuestionMcq;
import com.skolarli.lmsservice.models.db.questionbank.BankQuestionMcq;
import com.skolarli.lmsservice.models.dto.questionbank.NewBankQuestionMcqRequest;
import com.skolarli.lmsservice.repository.exam.ExamQuestionMcqRepository;
import com.skolarli.lmsservice.repository.questionbank.QuestionBankMcqRepository;
import com.skolarli.lmsservice.services.core.LmsUserService;
import com.skolarli.lmsservice.services.course.BatchService;
import com.skolarli.lmsservice.services.course.ChapterService;
import com.skolarli.lmsservice.services.course.CourseService;
import com.skolarli.lmsservice.services.course.LessonService;
import com.skolarli.lmsservice.services.exam.ExamService;
import com.skolarli.lmsservice.services.questionbank.QuestionBankMcqService;
import com.skolarli.lmsservice.utils.UserUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class QuestionBankMcqServiceImpl implements QuestionBankMcqService {

    static final Logger logger = LoggerFactory.getLogger(QuestionBankMcqServiceImpl.class);

    final QuestionBankMcqRepository questionBankMcqRepository;

    final ExamQuestionMcqRepository examQuestionMcqRepository;
    final CourseService courseService;

    final BatchService batchService;

    final ChapterService chapterService;

    final LessonService lessonService;

    final LmsUserService lmsUserService;

    final UserUtils userUtils;

    final ExamService examService;

    final JobLauncher jobLauncher;

    final Job job;

    public QuestionBankMcqServiceImpl(QuestionBankMcqRepository questionBankMcqRepository,
                                      ExamQuestionMcqRepository examQuestionMcqRepository,
                                      UserUtils userUtils, CourseService courseService,
                                      BatchService batchService, ChapterService chapterService,
                                      LessonService lessonService, LmsUserService lmsUserService,
                                      ExamService examService,
                                      @Qualifier("AsyncJobLauncher") JobLauncher jobLauncher,
                                      @Qualifier("importMcqQuestionsJob") Job job) {
        this.questionBankMcqRepository = questionBankMcqRepository;
        this.examQuestionMcqRepository = examQuestionMcqRepository;
        this.userUtils = userUtils;
        this.courseService = courseService;
        this.batchService = batchService;
        this.chapterService = chapterService;
        this.lessonService = lessonService;
        this.lmsUserService = lmsUserService;
        this.examService = examService;
        this.jobLauncher = jobLauncher;
        this.job = job;
    }

    public BankQuestionMcq toBankQuestionMcq(NewBankQuestionMcqRequest newBankQuestionMcqRequest) {
        BankQuestionMcq bankQuestionMcq = new BankQuestionMcq();
        if (newBankQuestionMcqRequest.getCourseId() != null) {
            Course course = courseService.getCourseById(newBankQuestionMcqRequest.getCourseId());
            bankQuestionMcq.setCourse(course);
        }
        if (newBankQuestionMcqRequest.getBatchId() != null) {
            Batch batch = batchService.getBatch(newBankQuestionMcqRequest.getBatchId());
            bankQuestionMcq.setBatch(batch);
        }
        if (newBankQuestionMcqRequest.getChapterId() != null) {
            bankQuestionMcq.setChapter(chapterService.getChapterById(newBankQuestionMcqRequest
                .getChapterId()));
        }
        if (newBankQuestionMcqRequest.getLessonId() != null) {
            bankQuestionMcq.setLesson(lessonService.getLessonById(newBankQuestionMcqRequest
                .getLessonId()));
        }
        if (newBankQuestionMcqRequest.getStudentId() != null) {
            bankQuestionMcq.setStudent(lmsUserService.getLmsUserById(newBankQuestionMcqRequest
                .getStudentId()));
        }
        bankQuestionMcq.setQuestion(newBankQuestionMcqRequest.getQuestion());
        if (newBankQuestionMcqRequest.getResourceFileRequest() != null) {
            bankQuestionMcq.setQuestionResourceFile(newBankQuestionMcqRequest
                .getResourceFileRequest().toResourceFile());
        }
        bankQuestionMcq.setQuestionType(newBankQuestionMcqRequest.getQuestionType());
        bankQuestionMcq.setMarks(newBankQuestionMcqRequest.getMarks());
        bankQuestionMcq.setDifficultyLevel(newBankQuestionMcqRequest.getDifficultyLevel());
        bankQuestionMcq.setQuestionFormat(newBankQuestionMcqRequest.getQuestionFormat());
        bankQuestionMcq.setAnswerFormat(newBankQuestionMcqRequest.getAnswerFormat());

        bankQuestionMcq.setNumberOfOptions(newBankQuestionMcqRequest.getNumberOfOptions());
        bankQuestionMcq.setOption1(newBankQuestionMcqRequest.getOption1());
        bankQuestionMcq.setOption2(newBankQuestionMcqRequest.getOption2());
        bankQuestionMcq.setOption3(newBankQuestionMcqRequest.getOption3());
        bankQuestionMcq.setOption4(newBankQuestionMcqRequest.getOption4());
        bankQuestionMcq.setOption5(newBankQuestionMcqRequest.getOption5());
        bankQuestionMcq.setOption6(newBankQuestionMcqRequest.getOption6());

        if (newBankQuestionMcqRequest.getCorrectAnswer() != null) {
            bankQuestionMcq.setCorrectAnswer(newBankQuestionMcqRequest.getCorrectAnswer()
                .stream().map(String::valueOf).collect(Collectors.joining(",")));
        } else {
            bankQuestionMcq.setCorrectAnswer("");
        }

        bankQuestionMcq.setSampleAnswerText(newBankQuestionMcqRequest.getSampleAnswerText());
        bankQuestionMcq.setSampleAnswerUrl(newBankQuestionMcqRequest.getSampleAnswerUrl());
        bankQuestionMcq.setNumberOfCorrectAnswers(newBankQuestionMcqRequest
            .getNumberOfCorrectAnswers());

        return bankQuestionMcq;
    }

    private Boolean checkPermission() {
        LmsUser currentUser = userUtils.getCurrentUser();
        return currentUser.getIsAdmin() || currentUser.getIsInstructor();
    }

    public ExamQuestionMcq toExamQuestionMcq(BankQuestionMcq bankQuestionMcq,
                                             Integer marks,
                                             Exam existingExam) {
        ExamQuestionMcq examQuestionMcq = new ExamQuestionMcq(bankQuestionMcq, marks, existingExam);
        return examQuestionMcq;
    }

    @Override
    public List<ExamQuestionMcq> toExamQuestionMcq(List<Long> bankQuestionMcqIds,
                                                   List<Integer> marks,
                                                   Long examId) {
        Exam existingExam = examService.getExam(examId);
        if (existingExam == null) {
            throw new ResourceNotFoundException("Exam with Id " + examId + " not found");
        }

        if (marks == null) {
            marks = new ArrayList<>();
        }
        if (bankQuestionMcqIds.size() != marks.size()) {
            for (int i = 0; i < bankQuestionMcqIds.size() - marks.size(); i++) {
                marks.add(null);
            }
        }

        List<BankQuestionMcq> bankQuestionMcqs =
            questionBankMcqRepository.findAllById(bankQuestionMcqIds);

        List<ExamQuestionMcq> examQuestionMcqs = new ArrayList<>();
        for (int i = 0; i < bankQuestionMcqs.size(); i++) {
            examQuestionMcqs.add(toExamQuestionMcq(bankQuestionMcqs.get(i), marks.get(i),
                existingExam));
        }
        List<ExamQuestionMcq> savedQuestions = examQuestionMcqRepository.saveAll(examQuestionMcqs);
        return savedQuestions;
    }

    @Override
    public BankQuestionMcq getQuestion(long id) {
        List<BankQuestionMcq> bankQuestionMcqs =
            questionBankMcqRepository.findAllById(new ArrayList<>(List.of(id)));
        if (bankQuestionMcqs.size() == 0) {
            throw new ResourceNotFoundException("Question with Id " + id + " not found");
        }
        return bankQuestionMcqs.get(0);
    }

    @Override
    public List<BankQuestionMcq> getAllQuestions() {
        return questionBankMcqRepository.findAll();
    }

    @Override
    public List<BankQuestionMcq> getQuestionsByParameters(Long courseId, Long batchId,
                                                          Long lessonId, Long chapterId,
                                                          Long studentId) {

        Double courseIdDouble = courseId != null ? courseId.doubleValue() : null;
        Double batchIdDouble = batchId != null ? batchId.doubleValue() : null;
        Double lessonIdDouble = lessonId != null ? lessonId.doubleValue() : null;
        Double chapterIdDouble = chapterId != null ? chapterId.doubleValue() : null;
        Double studentIdDouble = studentId != null ? studentId.doubleValue() : null;

        return questionBankMcqRepository.findQuestionsByParameters(courseIdDouble,
            batchIdDouble, lessonIdDouble, chapterIdDouble, studentIdDouble);

    }

    @Override
    public BankQuestionMcq saveQuestion(BankQuestionMcq question) {
        if (!question.validateFields()) {
            throw new ValidationFailureException("Question fields are not valid");
        }
        LmsUser currentUser = userUtils.getCurrentUser();
        question.setCreatedBy(currentUser);
        if (checkPermission()) {
            return questionBankMcqRepository.save(question);
        } else {
            throw new OperationNotSupportedException("User does not have permission to perform "
                + "this operation");
        }
    }

    @Override
    public List<BankQuestionMcq> saveAllQuestions(List<BankQuestionMcq> questions) {
        LmsUser currentUser = userUtils.getCurrentUser();
        questions.forEach(question -> {
            question.setCreatedBy(currentUser);
            if (!question.validateFields()) {
                throw new ValidationFailureException("Question fields are not valid");
            }
        });
        if (checkPermission()) {
            return questionBankMcqRepository.saveAll(questions);
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
    public BankQuestionMcq updateQuestion(BankQuestionMcq question, long id) {
        LmsUser currentUser = userUtils.getCurrentUser();
        BankQuestionMcq existingQuestion = getQuestion(id);
        if (!currentUser.getIsAdmin() && existingQuestion.getCreatedBy() != currentUser) {
            throw new OperationNotSupportedException("User does not have permission to perform "
                + "this operation");
        }
        existingQuestion.setUpdatedBy(currentUser);
        existingQuestion.update(question);
        if (!question.validateFields()) {
            throw new ValidationFailureException("Question fields are not valid");
        }
        return questionBankMcqRepository.save(existingQuestion);
    }

    @Override
    public void hardDeleteQuestion(long id) {
        LmsUser currentUser = userUtils.getCurrentUser();
        BankQuestionMcq existingQuestion = getQuestion(id);
        if (!currentUser.getIsAdmin() && existingQuestion.getCreatedBy() != currentUser) {
            throw new OperationNotSupportedException("User does not have permission to perform "
                + "this operation");
        }
        questionBankMcqRepository.deleteById(id);
    }
}
