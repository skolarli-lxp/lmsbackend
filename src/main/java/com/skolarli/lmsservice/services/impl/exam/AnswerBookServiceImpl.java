package com.skolarli.lmsservice.services.impl.exam;

import com.skolarli.lmsservice.exception.ResourceNotFoundException;
import com.skolarli.lmsservice.exception.ValidationFailureException;
import com.skolarli.lmsservice.models.AnswerBookStatus;
import com.skolarli.lmsservice.models.db.exam.AnswerBook;
import com.skolarli.lmsservice.models.db.exam.AnswerMcq;
import com.skolarli.lmsservice.models.db.exam.AnswerSubjective;
import com.skolarli.lmsservice.models.db.exam.AnswerTrueFalse;
import com.skolarli.lmsservice.models.dto.exam.answerbook.*;
import com.skolarli.lmsservice.repository.exam.AnswerBookRepository;
import com.skolarli.lmsservice.repository.exam.AnswerMcqRepository;
import com.skolarli.lmsservice.repository.exam.AnswerSubjectiveRepository;
import com.skolarli.lmsservice.repository.exam.AnswerTrueFalseRepository;
import com.skolarli.lmsservice.services.core.LmsUserService;
import com.skolarli.lmsservice.services.course.BatchService;
import com.skolarli.lmsservice.services.course.CourseService;
import com.skolarli.lmsservice.services.exam.AnswerBookAnswerService;
import com.skolarli.lmsservice.services.exam.AnswerBookService;
import com.skolarli.lmsservice.services.exam.ExamService;
import com.skolarli.lmsservice.utils.UserUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AnswerBookServiceImpl implements AnswerBookService {

    ExamService examService;
    LmsUserService lmsUserService;

    CourseService courseService;

    BatchService batchService;
    AnswerBookAnswerService answerBookAnswerService;

    AnswerBookRepository answerBookRepository;

    AnswerMcqRepository answerMcqRepository;

    AnswerSubjectiveRepository answerSubjectiveRepository;
    AnswerTrueFalseRepository answerTrueFalseRepository;

    UserUtils userUtils;


    public AnswerBookServiceImpl(ExamService examService, LmsUserService lmsUserService,
                                 AnswerBookAnswerService answerBookAnswerService,
                                 CourseService courseService, BatchService batchService,
                                 AnswerBookRepository answerBookRepository,
                                 AnswerMcqRepository answerMcqRepository,
                                 AnswerSubjectiveRepository answerSubjectiveRepository,
                                 AnswerTrueFalseRepository answerTrueFalseRepository,
                                 UserUtils userUtils) {
        this.examService = examService;
        this.lmsUserService = lmsUserService;
        this.answerBookAnswerService = answerBookAnswerService;
        this.courseService = courseService;
        this.batchService = batchService;
        this.answerBookRepository = answerBookRepository;
        this.answerMcqRepository = answerMcqRepository;
        this.answerSubjectiveRepository = answerSubjectiveRepository;
        this.answerTrueFalseRepository = answerTrueFalseRepository;
        this.userUtils = userUtils;
    }

    @Override
    public AnswerBook toAnswerBook(NewAnswerBookRequest newAnswerBookRequest) {
        AnswerBook answerBook = new AnswerBook();
        if (newAnswerBookRequest.getExamId() != null) {
            answerBook.setExam(examService.getExam(newAnswerBookRequest.getExamId()));
        }
        if (newAnswerBookRequest.getStudentId() != null) {
            answerBook.setStudent(lmsUserService.getLmsUserById(newAnswerBookRequest
                .getStudentId()));
        }
        answerBook.setRemarks(newAnswerBookRequest.getRemarks());
        answerBook.setMcqAnswers(
            newAnswerBookRequest.getMcqAnswers() != null ? newAnswerBookRequest.getMcqAnswers()
                .stream()
                .map(newAnswerMcqRequest -> answerBookAnswerService
                    .toAnswerMcq(newAnswerMcqRequest, answerBook))
                .collect(Collectors.toList())
                : null);
        answerBook.setSubjectiveAnswers(
            newAnswerBookRequest.getSubjectiveAnswers() != null ? newAnswerBookRequest
                .getSubjectiveAnswers()
                .stream()
                .map(newAnswerSubjectiveRequest -> answerBookAnswerService
                    .toAnswerSubjective(newAnswerSubjectiveRequest, answerBook))
                .collect(Collectors.toList())
                : null);
        answerBook.setTrueFalseAnswers(
            newAnswerBookRequest.getTrueFalseAnswers() != null ? newAnswerBookRequest
                .getTrueFalseAnswers()
                .stream()
                .map(newAnswerTrueFalseRequest -> answerBookAnswerService
                    .toAnswerTrueFalse(newAnswerTrueFalseRequest, answerBook))
                .collect(Collectors.toList())
                : null);

        answerBook.setSessionStartTime(newAnswerBookRequest.getSessionStartTime());
        answerBook.setSessionEndTime(newAnswerBookRequest.getSessionEndTime());
        if (newAnswerBookRequest.getCourseId() != null) {
            answerBook.setCourse(courseService.getCourseById(newAnswerBookRequest.getCourseId()));
        }
        if (newAnswerBookRequest.getBatchId() != null) {
            answerBook.setBatch(batchService.getBatch(newAnswerBookRequest.getBatchId()));
        }
        return answerBook;
    }

    @Override
    public List<AnswerBook> getAllByExamId(Long examId) {
        return answerBookRepository.findAllByExam_Id(examId);
    }

    @Override
    public List<AnswerBook> getAllByStudentId(Long studentId) {
        return answerBookRepository.findAllByStudent_Id(studentId);
    }

    @Override
    public List<AnswerBook> getAllByExamIdAndStudentId(Long examId, Long studentId) {
        return answerBookRepository.findAllByExam_IdAndStudent_Id(examId, studentId);
    }

    @Override
    public AnswerBook getAnswerBookById(Long id) {
        List<AnswerBook> answerBooks = answerBookRepository.findAllById(List.of(id));
        if (answerBooks != null && !answerBooks.isEmpty()) {
            return answerBooks.get(0);
        } else {
            throw new ResourceNotFoundException("Answer Book not found with id " + id);
        }
    }

    @Override
    public List<AnswerBook> getAllAnswerBooks() {
        return answerBookRepository.findAll();
    }

    @Override
    public GetAllAnswersResponse getAnswersByAnswerBookId(Long id) {
        AnswerBook answerBook = getAnswerBookById(id);
        if (answerBook == null) {
            throw new ResourceNotFoundException("Answer Book not found with id " + id);
        }
        GetAllAnswersResponse getAllAnswersResponse = new GetAllAnswersResponse();
        getAllAnswersResponse.setAnswerBookId(id);
        getAllAnswersResponse.setMcqAnswers(answerBook.getMcqAnswers() != null ? answerBook
            .getMcqAnswers().stream().map(AnswerMcq::toGetAnswerResponse)
            .collect(Collectors.toList()) : null);
        getAllAnswersResponse.setSubjectiveAnswers(answerBook.getSubjectiveAnswers() != null
            ? answerBook.getSubjectiveAnswers().stream().map(AnswerSubjective::toGetAnswerResponse)
            .collect(Collectors.toList()) : null);
        getAllAnswersResponse.setTrueFalseAnswers(answerBook.getTrueFalseAnswers() != null
            ? answerBook.getTrueFalseAnswers().stream().map(AnswerTrueFalse::toGetAnswerResponse)
            .collect(Collectors.toList()) : null);
        return getAllAnswersResponse;
    }

    @Override
    public GetAnswerResponse getAnswerByAnswerBookIdAndQuestionId(Long answerBookId, Long questionId,
                                                                  String questionType) {
        AnswerBook answerBook = getAnswerBookById(answerBookId);
        if (answerBook == null) {
            throw new ResourceNotFoundException("Answer Book not found with id " + answerBookId);
        }
        if (questionType.equalsIgnoreCase("MCQ")) {
            return answerBookAnswerService.getAnswerMcqByQuestionId(answerBookId, questionId)
                .toGetAnswerResponse();
        } else if (questionType.equalsIgnoreCase("Subjective")) {
            return answerBookAnswerService.getAnswerSubjectiveByQuestionId(answerBookId, questionId)
                .toGetAnswerResponse();
        } else if (questionType.equalsIgnoreCase("TrueFalse")) {
            return answerBookAnswerService.getAnswerTrueFalseByQuestionId(answerBookId, questionId)
                .toGetAnswerResponse();
        } else {
            throw new ValidationFailureException("Invalid Question Type");
        }
    }

    @Override
    public AnswerBook saveAnswerBook(AnswerBook answerBook) {
        if (!answerBook.validate()) {
            throw new ValidationFailureException("Invalid Answer Book");
        }
        answerBook.setCreatedBy(userUtils.getCurrentUser());
        return answerBookRepository.save(answerBook);
    }

    @Override
    public AnswerBook updateAnswerBook(AnswerBook answerBook, Long id) {
        AnswerBook existingAnswerBook = getAnswerBookById(id);
        if (existingAnswerBook == null) {
            throw new ResourceNotFoundException("Answer Book not found with id " + id);
        }
        existingAnswerBook.update(answerBook);
        existingAnswerBook.setUpdatedBy(userUtils.getCurrentUser());
        if (!answerBook.validate()) {
            throw new ValidationFailureException("Invalid Answer Book");
        }
        return answerBookRepository.save(existingAnswerBook);
    }

    @Override
    public AnswerBook updateStatus(AnswerBookStatus status, Long id) {
        AnswerBook existingAnswerBook = getAnswerBookById(id);
        if (existingAnswerBook == null) {
            throw new ResourceNotFoundException("Answer Book not found with id " + id);
        }
        existingAnswerBook.setStatus(status);
        existingAnswerBook.setUpdatedBy(userUtils.getCurrentUser());
        return answerBookRepository.save(existingAnswerBook);
    }

    @Override
    public NewAnswerResponse addAnswerToAnswerBook(NewAnswerMcqRequest newAnswerMcqRequest, Long id) {
        AnswerBook existingAnswerBook = getAnswerBookById(id);
        if (existingAnswerBook == null) {
            throw new ResourceNotFoundException("Answer Book not found with id " + id);
        }

        AnswerMcq answerMcq = answerBookAnswerService.toAnswerMcq(newAnswerMcqRequest, existingAnswerBook);
        AnswerMcq savedAnswer = answerMcqRepository.save(answerMcq);
        return savedAnswer.toNewAnswerResponse();

    }

    @Override
    public NewAnswerResponse addAnswerToAnswerBook(NewAnswerSubjectiveRequest newAnswerSubjectiveRequest, Long id) {
        AnswerBook existingAnswerBook = getAnswerBookById(id);
        if (existingAnswerBook == null) {
            throw new ResourceNotFoundException("Answer Book not found with id " + id);
        }

        AnswerSubjective answerSubjective = answerBookAnswerService.toAnswerSubjective(
            newAnswerSubjectiveRequest, existingAnswerBook);
        AnswerSubjective savedAnswer = answerSubjectiveRepository.save(answerSubjective);
        return savedAnswer.toNewAnswerResponse();
    }

    @Override
    public NewAnswerResponse addAnswerToAnswerBook(NewAnswerTrueFalseRequest newAnswerTrueFalseRequest, Long id) {
        AnswerBook existingAnswerBook = getAnswerBookById(id);
        if (existingAnswerBook == null) {
            throw new ResourceNotFoundException("Answer Book not found with id " + id);
        }

        AnswerTrueFalse answerTrueFalse = answerBookAnswerService.toAnswerTrueFalse(
            newAnswerTrueFalseRequest, existingAnswerBook);
        AnswerTrueFalse savedAnswer = answerTrueFalseRepository.save(answerTrueFalse);
        return savedAnswer.toNewAnswerResponse();
    }

    @Override
    public void addMcqAnswers(List<AnswerMcq> answerMcqs, AnswerBook answerBook) {

        if (answerBook.getMcqAnswers() == null) {
            answerBook.setMcqAnswers(answerMcqs);
        } else {
            answerBook.getMcqAnswers().addAll(answerMcqs);
        }
    }

    @Override
    public void addTrueFalseAnswers(List<AnswerTrueFalse> answerTrueFalses,
                                    AnswerBook answerBook) {
        if (answerBook.getTrueFalseAnswers() == null) {
            answerBook.setTrueFalseAnswers(answerTrueFalses);
        } else {
            answerBook.getTrueFalseAnswers().addAll(answerTrueFalses);
        }
    }

    @Override
    public void addSubjectiveAnswers(List<AnswerSubjective> answerSubjectives,
                                     AnswerBook answerBook) {
        if (answerBook.getSubjectiveAnswers() == null) {
            answerBook.setSubjectiveAnswers(answerSubjectives);
        } else {
            answerBook.getSubjectiveAnswers().addAll(answerSubjectives);
        }
    }

    @Override
    public AnswerBook addAnswers(AddAnswerBookAnswerRequest addAnswerBookAnswerRequest,
                                 Long answerBookId) {
        AnswerBook existingAnswerBook = getAnswerBookById(answerBookId);
        if (existingAnswerBook == null) {
            throw new ResourceNotFoundException("Answer Book not found with id " + answerBookId);
        }
        if (addAnswerBookAnswerRequest.getMcqAnswers() != null) {
            List<AnswerMcq> answerMcqs = addAnswerBookAnswerRequest.getMcqAnswers().stream()
                .map(newAnswerMcqRequest -> answerBookAnswerService
                    .toAnswerMcq(newAnswerMcqRequest, existingAnswerBook))
                .collect(Collectors.toList());
            addMcqAnswers(answerMcqs, existingAnswerBook);
        }
        if (addAnswerBookAnswerRequest.getTrueFalseAnswers() != null) {
            List<AnswerTrueFalse> answerTrueFalses = addAnswerBookAnswerRequest
                .getTrueFalseAnswers().stream()
                .map(newAnswerTrueFalseRequest -> answerBookAnswerService
                    .toAnswerTrueFalse(newAnswerTrueFalseRequest, existingAnswerBook))
                .collect(Collectors.toList());
            addTrueFalseAnswers(answerTrueFalses, existingAnswerBook);
        }
        if (addAnswerBookAnswerRequest.getSubjectiveAnswers() != null) {
            List<AnswerSubjective> answerSubjectives = addAnswerBookAnswerRequest
                .getSubjectiveAnswers().stream()
                .map(newAnswerSubjectiveRequest -> answerBookAnswerService
                    .toAnswerSubjective(newAnswerSubjectiveRequest, existingAnswerBook))
                .collect(Collectors.toList());
            addSubjectiveAnswers(answerSubjectives, existingAnswerBook);
        }

        answerBookRepository.save(existingAnswerBook);
        return existingAnswerBook;
    }

    @Override
    public void evaluateAnswerBook(Long answerBookId,
                                   AnswerBookEvaulationRequest request) {
        AnswerBook answerBook = getAnswerBookById(answerBookId);
        if (answerBook == null) {
            throw new ResourceNotFoundException("Answer Book not found with id " + answerBookId);
        }
        if (request.getMcqAnswerEvaluations() != null) {
            answerBookAnswerService.manualEvaluateMcqAnswers(answerBook,
                request.getMcqAnswerEvaluations());
        }
        if (request.getSubjectiveAnswerEvaluations() != null) {
            answerBookAnswerService.manualEvaluateSubjectiveAnswers(answerBook,
                request.getSubjectiveAnswerEvaluations());
        }
        if (request.getTrueFalseAnswerEvaluations() != null) {
            answerBookAnswerService.manualEvaluateTrueFalseAnswers(answerBook,
                request.getTrueFalseAnswerEvaluations());
        }
        answerBookRepository.save(answerBook);
    }

    private void clearMarks(AnswerBook answerBook) {
        answerBook.setTotalMarks(0);
        answerBook.setObtainedMarks(0);
        answerBook.setTotalQuestions(0);
        answerBook.setAttemptedQuestions(0);
        answerBook.setCorrectAnswers(0);
        answerBook.setIncorrectAnswers(0);
        answerBook.setPartiallyCorrectAnswers(0);

        int totalQuestions = 0;
        totalQuestions += answerBook.getExam().getExamQuestionMcqs().size();
        totalQuestions += answerBook.getExam().getExamQuestionSubjectives().size();
        totalQuestions += answerBook.getExam().getExamQuestionTrueOrFalses().size();
        answerBook.setTotalQuestions(totalQuestions);
    }

    @Override
    public GetScoresResponse calculateFinalScores(Long answerBookId) {
        AnswerBook answerBook = getAnswerBookById(answerBookId);
        if (answerBook == null) {
            throw new ResourceNotFoundException("Answer Book not found with id " + answerBookId);
        }

        clearMarks(answerBook);

        if (answerBook.getMcqAnswers() != null) {
            answerBookAnswerService.calculateMcqScores(answerBook);
        }
        if (answerBook.getSubjectiveAnswers() != null) {
            answerBookAnswerService.calculateSubjectiveScores(answerBook);
        }
        if (answerBook.getTrueFalseAnswers() != null) {
            answerBookAnswerService.calculateTrueOrFalseScores(answerBook);
        }
        answerBookRepository.save(answerBook);
        return answerBook.toGetScoresResponse();
    }

    @Override
    public NewAnswerResponse updateAnswerBookAnswer(UpdateAnswerMcqRequest updateAnswerMcqRequest,
                                                    Long answerBookId, Long answerId) {
        AnswerBook existingAnswerBook = getAnswerBookById(answerBookId);
        if (existingAnswerBook == null) {
            throw new ResourceNotFoundException("Answer Book not found with id " + answerBookId);
        }
        List<AnswerMcq> answerMcqs = existingAnswerBook.getMcqAnswers();
        if (answerMcqs == null) {
            throw new ResourceNotFoundException("Answer not found with id " + answerId);
        }
        AnswerMcq existingAnswer = null;
        for (AnswerMcq answerMcq : answerMcqs) {
            if (answerMcq.getId().equals(answerId)) {
                existingAnswer = answerMcq;
                break;
            }
        }
        if (existingAnswer == null) {
            throw new ResourceNotFoundException("Answer not found with id " + answerId);
        }
        existingAnswer.update(updateAnswerMcqRequest.toAnswerMcq());
        existingAnswer.setUpdatedBy(userUtils.getCurrentUser());
        answerMcqRepository.save(existingAnswer);
        return existingAnswer.toNewAnswerResponse();
    }

    @Override
    public NewAnswerResponse updateAnswerBookAnswer(UpdateAnswerSubjectiveRequest updateAnswerMcqRequest,
                                                    Long answerBookId, Long answerId) {
        AnswerBook existingAnswerBook = getAnswerBookById(answerBookId);
        if (existingAnswerBook == null) {
            throw new ResourceNotFoundException("Answer Book not found with id " + answerBookId);
        }
        List<AnswerSubjective> answerSubjectives = existingAnswerBook.getSubjectiveAnswers();
        if (answerSubjectives == null) {
            throw new ResourceNotFoundException("Answer not found with id " + answerId);
        }
        AnswerSubjective existingAnswer = null;
        for (AnswerSubjective answerSubjective : answerSubjectives) {
            if (answerSubjective.getId().equals(answerId)) {
                existingAnswer = answerSubjective;
                break;
            }
        }
        if (existingAnswer == null) {
            throw new ResourceNotFoundException("Answer not found with id " + answerId);
        }
        existingAnswer.update(updateAnswerMcqRequest.toAnswerSubjective());
        existingAnswer.setUpdatedBy(userUtils.getCurrentUser());
        answerSubjectiveRepository.save(existingAnswer);
        return existingAnswer.toNewAnswerResponse();
    }

    @Override
    public NewAnswerResponse updateAnswerBookAnswer(UpdateAnswerTrueFalseRequest updateAnswerMcqRequest,
                                                    Long answerBookId, Long answerId) {
        AnswerBook existingAnswerBook = getAnswerBookById(answerBookId);
        if (existingAnswerBook == null) {
            throw new ResourceNotFoundException("Answer Book not found with id " + answerBookId);
        }
        List<AnswerTrueFalse> answerTrueFalses = existingAnswerBook.getTrueFalseAnswers();
        if (answerTrueFalses == null) {
            throw new ResourceNotFoundException("Answer not found with id " + answerId);
        }
        AnswerTrueFalse existingAnswer = null;
        for (AnswerTrueFalse answerTrueFalse : answerTrueFalses) {
            if (answerTrueFalse.getId().equals(answerId)) {
                existingAnswer = answerTrueFalse;
                break;
            }
        }
        if (existingAnswer == null) {
            throw new ResourceNotFoundException("Answer not found with id " + answerId);
        }
        existingAnswer.update(updateAnswerMcqRequest.toAnswerTrueOrFalse());
        existingAnswer.setUpdatedBy(userUtils.getCurrentUser());
        answerTrueFalseRepository.save(existingAnswer);
        return existingAnswer.toNewAnswerResponse();
    }

    @Override
    public void deleteAnswerBook(Long id) {
        AnswerBook answerBook = getAnswerBookById(id);
        if (answerBook == null) {
            throw new ResourceNotFoundException("Answer Book not found with id " + id);
        }
        answerBookRepository.delete(answerBook);
    }

    public void deleteAnswerBookAnswer(Long answerId,
                                       String questionType) {
        if (questionType.equalsIgnoreCase("MCQ")) {
            answerMcqRepository.deleteById(answerId);
        } else if (questionType.equalsIgnoreCase("Subjective")) {
            answerSubjectiveRepository.deleteById(answerId);
        } else if (questionType.equalsIgnoreCase("TrueFalse")) {
            answerTrueFalseRepository.deleteById(answerId);
        } else {
            throw new ValidationFailureException("Invalid Question Type");
        }
    }
}