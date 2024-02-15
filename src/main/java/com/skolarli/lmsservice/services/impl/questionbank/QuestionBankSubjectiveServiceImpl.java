package com.skolarli.lmsservice.services.impl.questionbank;

import com.skolarli.lmsservice.exception.OperationNotSupportedException;
import com.skolarli.lmsservice.exception.ResourceNotFoundException;
import com.skolarli.lmsservice.models.db.core.LmsUser;
import com.skolarli.lmsservice.models.db.course.Course;
import com.skolarli.lmsservice.models.db.exam.Exam;
import com.skolarli.lmsservice.models.db.exam.ExamQuestionSubjective;
import com.skolarli.lmsservice.models.db.questionbank.BankQuestionSubjective;
import com.skolarli.lmsservice.models.dto.questionbank.NewBankQuestionSubjectiveRequest;
import com.skolarli.lmsservice.repository.exam.ExamQuestionSubjectiveRepository;
import com.skolarli.lmsservice.repository.questionbank.QuestionBankSubjectiveRepository;
import com.skolarli.lmsservice.services.core.LmsUserService;
import com.skolarli.lmsservice.services.course.BatchService;
import com.skolarli.lmsservice.services.course.ChapterService;
import com.skolarli.lmsservice.services.course.CourseService;
import com.skolarli.lmsservice.services.course.LessonService;
import com.skolarli.lmsservice.services.exam.ExamService;
import com.skolarli.lmsservice.services.questionbank.QuestionBankSubjectiveService;
import com.skolarli.lmsservice.utils.UserUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class QuestionBankSubjectiveServiceImpl implements QuestionBankSubjectiveService {

    final QuestionBankSubjectiveRepository questionBankSubjectiveRepository;

    final ExamQuestionSubjectiveRepository examQuestionSubjectiveRepository;
    final UserUtils userUtils;

    final CourseService courseService;

    final BatchService batchService;

    final ChapterService chapterService;

    final LessonService lessonService;

    final LmsUserService lmsUserService;

    final ExamService examService;

    public QuestionBankSubjectiveServiceImpl(QuestionBankSubjectiveRepository
                                                 questionBankSubjectiveRepository,
                                             ExamQuestionSubjectiveRepository
                                                 examQuestionSubjectiveRepository,
                                             UserUtils userUtils,
                                             CourseService courseService, BatchService batchService,
                                             ChapterService chapterService,
                                             LessonService lessonService,
                                             LmsUserService lmsUserService,
                                             ExamService examService) {
        this.questionBankSubjectiveRepository = questionBankSubjectiveRepository;
        this.examQuestionSubjectiveRepository = examQuestionSubjectiveRepository;
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
    public BankQuestionSubjective toBankQuestionSubjective(
        NewBankQuestionSubjectiveRequest newBankQuestionSubjectiveRequest) {
        BankQuestionSubjective bankQuestionSubjective = new BankQuestionSubjective();

        if (newBankQuestionSubjectiveRequest.getCourseId() != null) {
            Course course = courseService.getCourseById(
                newBankQuestionSubjectiveRequest.getCourseId());
            bankQuestionSubjective.setCourse(course);
        }
        if (newBankQuestionSubjectiveRequest.getBatchId() != null) {
            bankQuestionSubjective.setBatch(batchService.getBatch(
                newBankQuestionSubjectiveRequest.getBatchId()));
        }
        if (newBankQuestionSubjectiveRequest.getChapterId() != null) {
            bankQuestionSubjective.setChapter(chapterService.getChapterById(
                newBankQuestionSubjectiveRequest.getChapterId()));
        }
        if (newBankQuestionSubjectiveRequest.getLessonId() != null) {
            bankQuestionSubjective.setLesson(lessonService.getLessonById(
                newBankQuestionSubjectiveRequest.getLessonId()));
        }
        if (newBankQuestionSubjectiveRequest.getStudentId() != null) {
            bankQuestionSubjective.setStudent(lmsUserService.getLmsUserById(
                newBankQuestionSubjectiveRequest.getStudentId()));
        }
        bankQuestionSubjective.setQuestion(newBankQuestionSubjectiveRequest.getQuestion());
        bankQuestionSubjective.setQuestionType(newBankQuestionSubjectiveRequest.getQuestionType());
        bankQuestionSubjective.setMarks(newBankQuestionSubjectiveRequest.getMarks());
        bankQuestionSubjective.setDifficultyLevel(newBankQuestionSubjectiveRequest
            .getDifficultyLevel());

        bankQuestionSubjective.setQuestionFormat(newBankQuestionSubjectiveRequest
            .getQuestionFormat());
        bankQuestionSubjective.setAnswerFormat(newBankQuestionSubjectiveRequest.getAnswerFormat());
        bankQuestionSubjective.setSampleAnswerText(newBankQuestionSubjectiveRequest
            .getSampleAnswerText());
        bankQuestionSubjective.setSampleAnswerUrl(newBankQuestionSubjectiveRequest
            .getSampleAnswerUrl());


        bankQuestionSubjective.setWordCount(newBankQuestionSubjectiveRequest.getWordCount());
        bankQuestionSubjective.setCorrectAnswer(newBankQuestionSubjectiveRequest
            .getCorrectAnswer());

        return bankQuestionSubjective;
    }

    public ExamQuestionSubjective toExamQuestionSubjective(
        BankQuestionSubjective bankQuestionSubjective,
        Integer marks,
        Exam existingExam) {

        ExamQuestionSubjective examQuestionSubjective = new ExamQuestionSubjective(
            bankQuestionSubjective, marks, existingExam);
        return examQuestionSubjective;
    }

    @Override
    public List<ExamQuestionSubjective> toExamQuestionSubjective(List<Long> bankQuestionSubjectiveIds,
                                                                 List<Integer> marks, Long examId) {

        Exam existingExam = examService.getExam(examId);
        if (existingExam == null) {
            throw new ResourceNotFoundException("Exam with Id " + examId + " not found");
        }

        if (marks == null) {
            marks = new ArrayList<>();
        }
        if (bankQuestionSubjectiveIds.size() != marks.size()) {
            for (int i = 0; i < bankQuestionSubjectiveIds.size() - marks.size(); i++) {
                marks.add(null);
            }
        }

        List<BankQuestionSubjective> bankQuestionSubjective =
            questionBankSubjectiveRepository.findAllById(bankQuestionSubjectiveIds);

        List<ExamQuestionSubjective> examQuestionSubjectives = new ArrayList<>();
        for (int i = 0; i < bankQuestionSubjective.size(); i++) {
            examQuestionSubjectives.add(toExamQuestionSubjective(
                bankQuestionSubjective.get(i), marks.get(i), existingExam));
        }
        List<ExamQuestionSubjective> savedExamQuestionSubjectives =
            examQuestionSubjectiveRepository.saveAll(examQuestionSubjectives);
        return savedExamQuestionSubjectives;
    }

    @Override
    public BankQuestionSubjective getQuestion(long id) {
        List<BankQuestionSubjective> bankQuestionMcqs =
            questionBankSubjectiveRepository.findAllById(new ArrayList<>(List.of(id)));
        if (bankQuestionMcqs.size() == 0) {
            throw new ResourceNotFoundException("Question with Id " + id + " not found");
        }
        return bankQuestionMcqs.get(0);
    }

    @Override
    public List<BankQuestionSubjective> getAllQuestions() {
        return questionBankSubjectiveRepository.findAll();
    }

    @Override
    public List<BankQuestionSubjective> getQuestionsByParameters(Long courseId, Long batchId,
                                                                 Long lessonId, Long chapterId,
                                                                 Long studentId) {
        Double courseIdDouble = courseId != null ? courseId.doubleValue() : null;
        Double batchIdDouble = batchId != null ? batchId.doubleValue() : null;
        Double lessonIdDouble = lessonId != null ? lessonId.doubleValue() : null;
        Double chapterIdDouble = chapterId != null ? chapterId.doubleValue() : null;
        Double studentIdDouble = studentId != null ? studentId.doubleValue() : null;

        return questionBankSubjectiveRepository.findQuestionsByParameters(courseIdDouble,
            batchIdDouble, lessonIdDouble, chapterIdDouble, studentIdDouble);
    }

    @Override
    public BankQuestionSubjective saveQuestion(BankQuestionSubjective question) {
        LmsUser currentUser = userUtils.getCurrentUser();
        question.setCreatedBy(currentUser);
        if (checkPermission()) {
            return questionBankSubjectiveRepository.save(question);
        } else {
            throw new UnsupportedOperationException("User does not have permission to perform "
                + "this operation");
        }
    }

    @Override
    public List<BankQuestionSubjective> saveAllQuestions(List<BankQuestionSubjective> questions) {
        LmsUser currentUser = userUtils.getCurrentUser();
        questions.forEach(question -> question.setCreatedBy(currentUser));
        if (checkPermission()) {
            return questionBankSubjectiveRepository.saveAll(questions);
        } else {
            throw new OperationNotSupportedException("User does not have permission to perform "
                + "this operation");
        }
    }

    @Override
    public BankQuestionSubjective updateQuestion(BankQuestionSubjective question, long id) {
        LmsUser currentUser = userUtils.getCurrentUser();
        BankQuestionSubjective existingQuestion = getQuestion(id);
        if (!currentUser.getIsAdmin() && existingQuestion.getCreatedBy() != currentUser) {
            throw new OperationNotSupportedException("User does not have permission to perform "
                + "this operation");
        }
        existingQuestion.update(question);
        return questionBankSubjectiveRepository.save(existingQuestion);
    }

    @Override
    public void hardDeleteQuestion(long id) {
        LmsUser currentUser = userUtils.getCurrentUser();
        BankQuestionSubjective existingQuestion = getQuestion(id);
        if (!currentUser.getIsAdmin() && existingQuestion.getCreatedBy() != currentUser) {
            throw new OperationNotSupportedException("User does not have permission to perform "
                + "this operation");
        }
        questionBankSubjectiveRepository.delete(existingQuestion);
    }
}
