package com.skolarli.lmsservice.services.impl.exam;

import com.skolarli.lmsservice.models.db.exam.AnswerBook;
import com.skolarli.lmsservice.models.db.exam.AnswerMcq;
import com.skolarli.lmsservice.models.db.exam.AnswerSubjective;
import com.skolarli.lmsservice.models.db.exam.AnswerTrueFalse;
import com.skolarli.lmsservice.models.dto.exam.AddAnswerBookAnswerRequest;
import com.skolarli.lmsservice.models.dto.exam.NewAnswerBookRequest;
import com.skolarli.lmsservice.services.core.LmsUserService;
import com.skolarli.lmsservice.services.course.BatchService;
import com.skolarli.lmsservice.services.course.CourseService;
import com.skolarli.lmsservice.services.exam.AnswerBookAnswerService;
import com.skolarli.lmsservice.services.exam.AnswerBookService;
import com.skolarli.lmsservice.services.exam.ExamService;
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

    public AnswerBookServiceImpl(ExamService examService, LmsUserService lmsUserService,
                                 AnswerBookAnswerService answerBookAnswerService,
                                 CourseService courseService, BatchService batchService) {
        this.examService = examService;
        this.lmsUserService = lmsUserService;
        this.answerBookAnswerService = answerBookAnswerService;
        this.courseService = courseService;
        this.batchService = batchService;
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
    public List<AnswerBook> findAllByExam_Id(Long examId) {
        return null;
    }

    @Override
    public List<AnswerBook> findAllByExam_IdAndStudent_Id(Long examId, Long studentId) {
        return null;
    }

    @Override
    public AnswerBook getAnswerBookById(Long id) {
        return null;
    }

    @Override
    public AnswerBook saveAnswerBook(AnswerBook answerBook) {
        return null;
    }

    @Override
    public AnswerBook saveAnswerBook(NewAnswerBookRequest newAnswerBookRequest) {
        return null;
    }

    @Override
    public AnswerBook updateAnswerBook(AnswerBook answerBook, Long id) {
        return null;
    }

    @Override
    public AnswerBook addMcqAnswers(AnswerBook answerBook, List<AnswerMcq> answerMcqs) {
        return null;
    }

    @Override
    public AnswerBook addTrueFalseAnswers(AnswerBook answerBook,
                                          List<AnswerTrueFalse> answerTrueFalses) {
        return null;
    }

    @Override
    public AnswerBook addSubjectiveAnswers(AnswerBook answerBook,
                                           List<AnswerSubjective> answerSubjectives) {
        return null;
    }

    @Override
    public AnswerBook addAnswers(AddAnswerBookAnswerRequest addAnswerBookAnswerRequest, Long id) {
        return null;
    }

    @Override
    public void deleteAnswerBook(Long id) {

    }
}