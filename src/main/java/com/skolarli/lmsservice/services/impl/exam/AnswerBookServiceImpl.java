package com.skolarli.lmsservice.services.impl.exam;

import com.skolarli.lmsservice.exception.ResourceNotFoundException;
import com.skolarli.lmsservice.exception.ValidationFailureException;
import com.skolarli.lmsservice.models.AnswerBookStatus;
import com.skolarli.lmsservice.models.db.exam.AnswerBook;
import com.skolarli.lmsservice.models.db.exam.AnswerMcq;
import com.skolarli.lmsservice.models.db.exam.AnswerSubjective;
import com.skolarli.lmsservice.models.db.exam.AnswerTrueFalse;
import com.skolarli.lmsservice.models.dto.exam.AddAnswerBookAnswerRequest;
import com.skolarli.lmsservice.models.dto.exam.AnswerBookEvaulationRequest;
import com.skolarli.lmsservice.models.dto.exam.NewAnswerBookRequest;
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

    public void evaluateAnswerBook(Long answerBookId,
                                   AnswerBookEvaulationRequest request) {
        AnswerBook answerBook = getAnswerBookById(answerBookId);
        if (answerBook == null) {
            throw new ResourceNotFoundException("Answer Book not found with id " + answerBookId);
        }


    }

    @Override
    public void deleteAnswerBook(Long id) {
        AnswerBook answerBook = getAnswerBookById(id);
        if (answerBook == null) {
            throw new ResourceNotFoundException("Answer Book not found with id " + id);
        }
        answerBookRepository.delete(answerBook);
    }
}