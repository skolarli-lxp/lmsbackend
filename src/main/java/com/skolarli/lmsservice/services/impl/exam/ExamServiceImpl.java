package com.skolarli.lmsservice.services.impl.exam;

import com.skolarli.lmsservice.exception.OperationNotSupportedException;
import com.skolarli.lmsservice.exception.ResourceNotFoundException;
import com.skolarli.lmsservice.exception.ValidationFailureException;
import com.skolarli.lmsservice.models.db.core.LmsUser;
import com.skolarli.lmsservice.models.db.exam.Exam;
import com.skolarli.lmsservice.models.db.exam.ExamQuestionMcq;
import com.skolarli.lmsservice.models.db.exam.ExamQuestionSubjective;
import com.skolarli.lmsservice.models.db.exam.ExamQuestionTrueOrFalse;
import com.skolarli.lmsservice.models.db.questionbank.BankQuestionMcq;
import com.skolarli.lmsservice.models.db.questionbank.BankQuestionSubjective;
import com.skolarli.lmsservice.models.db.questionbank.BankQuestionTrueOrFalse;
import com.skolarli.lmsservice.models.dto.exam.*;
import com.skolarli.lmsservice.repository.exam.ExamRepository;
import com.skolarli.lmsservice.repository.questionbank.QuestionBankMcqRepository;
import com.skolarli.lmsservice.repository.questionbank.QuestionBankSubjectiveRepository;
import com.skolarli.lmsservice.repository.questionbank.QuestionBankTrueOrFalseRepository;
import com.skolarli.lmsservice.services.course.BatchService;
import com.skolarli.lmsservice.services.course.CourseService;
import com.skolarli.lmsservice.services.exam.ExamQuestionMcqService;
import com.skolarli.lmsservice.services.exam.ExamQuestionSubjectiveService;
import com.skolarli.lmsservice.services.exam.ExamQuestionTrueOrFalseService;
import com.skolarli.lmsservice.services.exam.ExamService;
import com.skolarli.lmsservice.utils.UserUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class ExamServiceImpl implements ExamService {
    private static final Logger logger = LoggerFactory.getLogger(ExamServiceImpl.class);

    ExamRepository examRepository;
    QuestionBankMcqRepository questionBankMcqRepository;
    QuestionBankSubjectiveRepository questionBankSubjectiveRepository;
    QuestionBankTrueOrFalseRepository questionBankTrueOrFalseRepository;

    CourseService courseService;

    BatchService batchService;

    ExamQuestionMcqService examQuestionMcqService;

    ExamQuestionTrueOrFalseService examQuestionTrueOrFalseService;

    ExamQuestionSubjectiveService examQuestionSubjectiveService;
    UserUtils userUtils;

    public ExamServiceImpl(ExamRepository examRepository,
                           QuestionBankMcqRepository questionBankMcqRepository,
                           QuestionBankSubjectiveRepository questionBankSubjectiveRepository,
                           QuestionBankTrueOrFalseRepository questionBankTrueOrFalseRepository,
                           CourseService courseService,
                           BatchService batchService,
                           ExamQuestionMcqService examQuestionMcqService,
                           ExamQuestionTrueOrFalseService examQuestionTrueOrFalseService,
                           ExamQuestionSubjectiveService examQuestionSubjectiveService,
                           UserUtils userUtils) {
        this.examRepository = examRepository;
        this.questionBankMcqRepository = questionBankMcqRepository;
        this.questionBankSubjectiveRepository = questionBankSubjectiveRepository;
        this.questionBankTrueOrFalseRepository = questionBankTrueOrFalseRepository;
        this.courseService = courseService;
        this.batchService = batchService;
        this.examQuestionMcqService = examQuestionMcqService;
        this.examQuestionTrueOrFalseService = examQuestionTrueOrFalseService;
        this.examQuestionSubjectiveService = examQuestionSubjectiveService;
        this.userUtils = userUtils;
    }

    private Boolean checkPermission() {
        LmsUser currentUser = userUtils.getCurrentUser();
        return currentUser.getIsAdmin() || currentUser.getIsInstructor();
    }

    public Exam toExam(NewExamRequest request) {
        Exam exam = new Exam();

        if (request.getCourseId() != null) {
            exam.setCourse(courseService.getCourseById(request.getCourseId()));
        }
        if (request.getBatchId() != null) {
            exam.setBatch(batchService.getBatch(request.getBatchId()));
        }

        exam.setExamName(request.getExamName());
        exam.setExamType(request.getExamType());
        exam.setDurationMins(request.getDurationMins());
        exam.setExamPublishDate(request.getExamPublishDate());
        exam.setExamExpiryDate(request.getExamExpiryDate());
        exam.setTotalMarks(request.getTotalMarks());
        exam.setPassingMarks(request.getPassingMarks());

        if (request.getMcqQuestions() != null) {
            exam.setExamQuestionMcqs(request.toExamQuestionMcqList());
            exam.getExamQuestionMcqs().forEach(examQuestionMcq -> examQuestionMcq.setExam(exam));
        }
        if (request.getSubjectiveQuestions() != null) {
            exam.setExamQuestionSubjectives(request.toExamQuestionSubjectiveList());
            exam.getExamQuestionSubjectives().forEach(examQuestionSubjective
                    -> examQuestionSubjective.setExam(exam));
        }
        if (request.getTrueOrFalseQuestions() != null) {
            exam.setExamQuestionTrueOrFalses(request.toExamQuestionTrueOrFalseList());
            exam.getExamQuestionTrueOrFalses().forEach(examQuestionTrueOrFalse
                    -> examQuestionTrueOrFalse.setExam(exam));
        }
        return exam;
    }


    @Override
    public Exam getExam(long id) {
        List<Exam> exams =
                examRepository.findAllById(new ArrayList<>(List.of(id)));
        if (exams.size() == 0) {
            throw new ResourceNotFoundException("Exam with Id " + id + " not found");
        }
        return exams.get(0);
    }

    @Override
    public List<Exam> getAllExams() {
        return examRepository.findAll();
    }

    @Override
    public List<Exam> getAllExamsForCourse(Long courseId) {
        return examRepository.findAllByCourse_Id(courseId);
    }

    @Override
    public List<Exam> getAllExamsForBatch(Long batchId) {
        return examRepository.findAllByBatch_Id(batchId);
    }

    @Override
    public NewExamQuestionsAllTypesResponse getAllQuestions(Long id) {
        Exam existingExam = getExam(id);
        if (existingExam != null) {
            return existingExam.fetchAllExamQuestions();
        }
        return null;
    }

    @Override
    public Exam saveExam(Exam exam) {
        LmsUser currentUser = userUtils.getCurrentUser();

        if (!exam.validateFields()) {
            logger.error("Exam fields validation failed");
            throw new ValidationFailureException("Exam fields are not valid");
        }
        if (!checkPermission()) {
            logger.error("User does not have permission to perform this operation");
            throw new OperationNotSupportedException("User does not have permission to perform "
                    + "this operation");
        }

        exam.setCreatedBy(currentUser);
        return examRepository.save(exam);
    }

    @Override
    public Exam addQuestionsToExam(NewExamQuestionsAllTypesRequest newExamQuestionRequest,
                                   long id) {
        Exam existingExam = getExam(id);
        if (existingExam == null) {
            logger.error("Exam with Id " + id + " not found");
            throw new ResourceNotFoundException("Exam with Id " + id + " not found");
        }
        if (!checkPermission()) {
            logger.error("User does not have permission to perform this operation");
            throw new OperationNotSupportedException("User does not have permission to perform "
                    + "this operation");
        }

        if (newExamQuestionRequest.getMcqQuestions() != null) {
            Integer mcqMaxSortOrder = examQuestionMcqService.getMaxQuestionSortOrder(id);
            for (NewExamQuestionMcqRequest newExamQuestionMcqRequest : newExamQuestionRequest.getMcqQuestions()) {
                if (newExamQuestionMcqRequest.getQuestionSortOrder() == null) {
                    newExamQuestionMcqRequest.setQuestionSortOrder(mcqMaxSortOrder + 1);
                    mcqMaxSortOrder++;
                } else {
                    if (newExamQuestionMcqRequest.getQuestionSortOrder() > mcqMaxSortOrder) {
                        mcqMaxSortOrder = newExamQuestionMcqRequest.getQuestionSortOrder();
                    }
                }
            }
            existingExam.addMcqQuestions(newExamQuestionRequest.getMcqQuestions());
        }

        if (newExamQuestionRequest.getSubjectiveQuestions() != null) {
            Integer subjectiveMaxSortOrder = examQuestionSubjectiveService.getMaxQuestionSortOrder(id);
            for (NewExamQuestionSubjectiveRequest newExamQuestionSubjectiveRequest :
                    newExamQuestionRequest.getSubjectiveQuestions()) {
                if (newExamQuestionSubjectiveRequest.getQuestionSortOrder() == null) {
                    newExamQuestionSubjectiveRequest.setQuestionSortOrder(subjectiveMaxSortOrder + 1);
                    subjectiveMaxSortOrder++;
                } else {
                    if (newExamQuestionSubjectiveRequest.getQuestionSortOrder() > subjectiveMaxSortOrder) {
                        subjectiveMaxSortOrder = newExamQuestionSubjectiveRequest.getQuestionSortOrder();
                    }
                }
            }
            existingExam.addSubjectiveQuestions(newExamQuestionRequest.getSubjectiveQuestions());
        }
        if (newExamQuestionRequest.getTrueOrFalseQuestions() != null) {
            Integer trueOrFalseMaxSortOrder = examQuestionTrueOrFalseService.getMaxQuestionSortOrder(id);
            for (NewExamQuestionTrueOrFalseRequest newExamQuestionTrueOrFalseRequest :
                    newExamQuestionRequest.getTrueOrFalseQuestions()) {
                if (newExamQuestionTrueOrFalseRequest.getQuestionSortOrder() == null) {
                    newExamQuestionTrueOrFalseRequest.setQuestionSortOrder(trueOrFalseMaxSortOrder + 1);
                    trueOrFalseMaxSortOrder++;
                } else {
                    if (newExamQuestionTrueOrFalseRequest.getQuestionSortOrder() > trueOrFalseMaxSortOrder) {
                        trueOrFalseMaxSortOrder = newExamQuestionTrueOrFalseRequest.getQuestionSortOrder();
                    }
                }
            }
            existingExam.addTrueOrFalseQuestions(newExamQuestionRequest.getTrueOrFalseQuestions());
        }

        if (!existingExam.validateFields()) {
            logger.error("Exam fields validation failed");
            throw new ValidationFailureException("Exam fields are not valid");
        }

        LmsUser currentUser = userUtils.getCurrentUser();
        existingExam.setUpdatedBy(currentUser);

        return examRepository.save(existingExam);
    }

    @Override
    public Exam updateExams(Exam exam, long id) {
        Exam existingExam = getExam(id);
        if (existingExam == null) {
            logger.error("Exam with Id " + id + " not found");
            throw new ResourceNotFoundException("Exam with Id " + id + " not found");
        }
        existingExam.update(exam);
        if (!existingExam.validateFields()) {
            logger.error("Exam fields validation failed");
            throw new ValidationFailureException("Exam fields are not valid");
        }
        if (!checkPermission()) {
            logger.error("User does not have permission to perform this operation");
            throw new OperationNotSupportedException("User does not have permission to perform "
                    + "this operation");
        }

        LmsUser currentUser = userUtils.getCurrentUser();
        exam.setUpdatedBy(currentUser);

        return examRepository.save(existingExam);
    }

    @Override
    public Exam updateSortOrder(QuestionSortOrderRequest questionSortOrderRequest, Long examId) {
        Exam existingExam = getExam(examId);

        List<ExamQuestionMcq> examQuestionMcqs = existingExam.getExamQuestionMcqs();
        List<IndividualQuestionSortOrder> mcqSortOrderList = questionSortOrderRequest.getMcqQuestions();
        if (mcqSortOrderList != null && examQuestionMcqs != null) {
            for (IndividualQuestionSortOrder individualQuestionSortOrder : mcqSortOrderList) {
                for (ExamQuestionMcq examQuestionMcq : examQuestionMcqs) {
                    if (examQuestionMcq.getId() == individualQuestionSortOrder.getQuestionId()) {
                        examQuestionMcq.setQuestionSortOrder(individualQuestionSortOrder.getQuestionSortOrder());
                        break;
                    }
                }
            }
        }

        List<ExamQuestionSubjective> examQuestionSubjectives = existingExam.getExamQuestionSubjectives();
        List<IndividualQuestionSortOrder> subjectiveSortOrderList = questionSortOrderRequest.getSubjectiveQuestions();
        if (subjectiveSortOrderList != null && examQuestionSubjectives != null) {
            for (IndividualQuestionSortOrder individualQuestionSortOrder : subjectiveSortOrderList) {
                for (ExamQuestionSubjective examQuestionSubjective : examQuestionSubjectives) {
                    if (examQuestionSubjective.getId() == individualQuestionSortOrder.getQuestionId()) {
                        examQuestionSubjective.setQuestionSortOrder(individualQuestionSortOrder.getQuestionSortOrder());
                        break;
                    }
                }
            }
        }

        List<ExamQuestionTrueOrFalse> examQuestionTrueOrFalses = existingExam.getExamQuestionTrueOrFalses();
        List<IndividualQuestionSortOrder> trueOrFalseSortOrderList = questionSortOrderRequest.getTrueOrFalseQuestions();
        if (trueOrFalseSortOrderList != null && examQuestionTrueOrFalses != null) {
            for (IndividualQuestionSortOrder individualQuestionSortOrder : trueOrFalseSortOrderList) {
                for (ExamQuestionTrueOrFalse examQuestionTrueOrFalse : examQuestionTrueOrFalses) {
                    if (examQuestionTrueOrFalse.getId() == individualQuestionSortOrder.getQuestionId()) {
                        examQuestionTrueOrFalse.setQuestionSortOrder(individualQuestionSortOrder
                                .getQuestionSortOrder());
                        break;
                    }
                }
            }
        }

        Exam savedExam = examRepository.save(existingExam);
        return savedExam;
    }

    @Override
    public Exam updateExamQuestion(NewExamQuestionRequest newExamQuestionRequest, Long examId,
                                   Long questionId, String questionType) {
        Exam existingExam = getExam(examId);
        if (existingExam == null) {
            logger.error("Exam with Id " + examId + " not found");
            throw new ResourceNotFoundException("Exam with Id " + examId + " not found");
        }

        if (!checkPermission()) {
            logger.error("User does not have permission to perform this operation");
            throw new OperationNotSupportedException("User does not have permission to perform "
                    + "this operation");
        }

        if (questionType.equalsIgnoreCase("MCQ")) {
            for (ExamQuestionMcq examQuestionMcq : existingExam.getExamQuestionMcqs()) {
                if (examQuestionMcq.getId() == questionId) {
                    ExamQuestionMcq newExamQuestionMcq =
                            ((NewExamQuestionMcqRequest) newExamQuestionRequest).toExamQuestionMcq();
                    examQuestionMcq.update(newExamQuestionMcq);
                }
            }
        } else if (questionType.equalsIgnoreCase("Subjective")) {
            for (ExamQuestionSubjective examQuestionSubjective : existingExam.getExamQuestionSubjectives()) {
                if (examQuestionSubjective.getId() == questionId) {
                    ExamQuestionSubjective newExamQuestionSubjective =
                            ((NewExamQuestionSubjectiveRequest) newExamQuestionRequest).toExamQuestionSubjective();
                    examQuestionSubjective.update(newExamQuestionSubjective);
                }
            }
        } else if (questionType.equalsIgnoreCase("TrueOrFalse")) {
            for (ExamQuestionTrueOrFalse examQuestionTrueOrFalse : existingExam.getExamQuestionTrueOrFalses()) {
                if (examQuestionTrueOrFalse.getId() == questionId) {
                    ExamQuestionTrueOrFalse newExamQuestionTrueOrFalse =
                            ((NewExamQuestionTrueOrFalseRequest) newExamQuestionRequest).toExamQuestionTrueOrFalse();
                    examQuestionTrueOrFalse.update(newExamQuestionTrueOrFalse);
                }
            }
        } else {
            logger.error("Invalid question type");
            throw new ValidationFailureException("Invalid question type");
        }
        Exam savedExam = examRepository.save(existingExam);
        return savedExam;
    }

    @Override
    public Exam nullifyFields(List<String> fieldNames, long id) {
        Exam existingExam = getExam(id);
        if (existingExam == null) {
            logger.error("Exam with Id " + id + " not found");
            throw new ResourceNotFoundException("Exam with Id " + id + " not found");
        }
        if (!checkPermission()) {
            logger.error("User does not have permission to perform this operation");
            throw new OperationNotSupportedException("User does not have permission to perform "
                    + "this operation");
        }
        if (fieldNames.contains("courseId")) {
            existingExam.setCourse(null);
        }
        if (fieldNames.contains("batchId")) {
            existingExam.setBatch(null);
        }
        Exam savedExam = examRepository.save(existingExam);
        return savedExam;
    }

    @Override
    public void hardDeleteExam(long id) {
        Exam existingExam = getExam(id);
        if (existingExam == null) {
            logger.error("Exam with Id " + id + " not found");
            throw new ResourceNotFoundException("Exam with Id " + id + " not found");
        }
        if (!checkPermission()) {
            logger.error("User does not have permission to perform this operation");
            throw new OperationNotSupportedException("User does not have permission to perform "
                    + "this operation");
        }
        examRepository.delete(existingExam);
    }

    @Override
    public void deleteQuestions(Long examId, DeleteExamQuestionsRequest questionIds) {
        Exam existingExam = getExam(examId);
        if (existingExam == null) {
            logger.error("Exam with Id " + examId + " not found");
            throw new ResourceNotFoundException("Exam with Id " + examId + " not found");
        }
        if (!checkPermission()) {
            logger.error("User does not have permission to perform this operation");
            throw new OperationNotSupportedException("User does not have permission to perform "
                    + "this operation");
        }
        examQuestionMcqService.hardDeleteQuestions(questionIds.getMcqQuestionsIds(), existingExam);
        examQuestionSubjectiveService.hardDeleteQuestions(questionIds.getSubjectiveQuestionsIds(),
                existingExam);
        examQuestionTrueOrFalseService.hardDeleteQuestions(questionIds.getTrueOrFalseQuestionsIds(),
                existingExam);
    }

    @Override
    public AddExamQuestionToQbResponse addExamQuestionToQuestionBank(AddExamQuestionToQbRequest
                                                                             request) {
        List<Long> mcqQuestions = request.getMcqQuestionsList();
        List<Long> subjectiveQuestions = request.getSubjectiveQuestionsList();
        List<Long> trueOrFalseQuestions = request.getTrueOrFalseQuestionsList();

        AddExamQuestionToQbResponse response = new AddExamQuestionToQbResponse();

        if (mcqQuestions != null) {
            List<BankQuestionMcq> bankQuestionMcqs = examQuestionMcqService
                    .toBankQuestionMcq(mcqQuestions);
            List<BankQuestionMcq> savedQuestions = questionBankMcqRepository
                    .saveAll(bankQuestionMcqs);
            response.setMcqQuestionsList(savedQuestions);
        }

        if (subjectiveQuestions != null) {
            List<BankQuestionSubjective> bankQuestionSubjectives = examQuestionSubjectiveService
                    .toBankQuestionSubjective(subjectiveQuestions);
            List<BankQuestionSubjective> savedQuestions = questionBankSubjectiveRepository
                    .saveAll(bankQuestionSubjectives);
            response.setSubjectiveQuestionsList(savedQuestions);
        }

        if (trueOrFalseQuestions != null) {
            List<BankQuestionTrueOrFalse> bankQuestionTrueOrFalses = examQuestionTrueOrFalseService
                    .toBankQuestionTrueOrFalse(trueOrFalseQuestions);
            List<BankQuestionTrueOrFalse> savedQuestions = questionBankTrueOrFalseRepository
                    .saveAll(bankQuestionTrueOrFalses);
            response.setTrueOrFalseQuestionsList(savedQuestions);
        }

        return response;
    }
}
