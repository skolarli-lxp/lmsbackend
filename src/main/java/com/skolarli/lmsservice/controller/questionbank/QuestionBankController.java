package com.skolarli.lmsservice.controller.questionbank;

import com.skolarli.lmsservice.contexts.TenantContext;
import com.skolarli.lmsservice.models.QuestionType;
import com.skolarli.lmsservice.models.dto.questionbank.UploadJobStatusResponse;
import com.skolarli.lmsservice.services.questionbank.QuestionBankMcqService;
import com.skolarli.lmsservice.services.questionbank.QuestionBankSubjectiveService;
import com.skolarli.lmsservice.services.questionbank.QuestionBankTrueOrFalseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@RestController
@RequestMapping("/questionbank/upload")
public class QuestionBankController {

    static final String TEMP_STORAGE = "/tmp/lmsservice/";
    final Logger logger = LoggerFactory.getLogger(QuestionBankController.class);
    final QuestionBankMcqService questionBankMcqService;

    final QuestionBankTrueOrFalseService questionBankTrueOrFalseService;

    final QuestionBankSubjectiveService questionBankSubjectiveService;


    private final JobExplorer jobExplorer;

    TenantContext tenantContext;


    public QuestionBankController(QuestionBankMcqService questionBankMcqService,
                                  QuestionBankTrueOrFalseService questionBankTrueOrFalseService,
                                  QuestionBankSubjectiveService questionBankSubjectiveService,
                                  JobExplorer jobExplorer,
                                  TenantContext tenantContext) {
        this.questionBankMcqService = questionBankMcqService;
        this.questionBankTrueOrFalseService = questionBankTrueOrFalseService;
        this.questionBankSubjectiveService = questionBankSubjectiveService;
        this.jobExplorer = jobExplorer;
        this.tenantContext = tenantContext;
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<UploadJobStatusResponse> uploadQuestions(@RequestParam MultipartFile file,
                                                                   @RequestParam QuestionType questionType) {
        UUID uuid = UUID.randomUUID();
        MDC.put("requestId", uuid.toString());
        logger.info("Received request for upload questions from file : "
            + file.getOriginalFilename());

        String originalFileName = file.getOriginalFilename();
        String currentUserName = (String) SecurityContextHolder.getContext()
            .getAuthentication().getPrincipal();

        try {
            Path directory = Paths.get(TEMP_STORAGE);
            Files.createDirectories(directory);

            File fileToImport = new File(TEMP_STORAGE + originalFileName);
            file.transferTo(fileToImport);

            Long jobId = 0L;
            if (questionType == QuestionType.MCQ) {
                jobId = questionBankMcqService.uploadQuestionsFromCsvBatchJob(fileToImport.getPath(),
                    tenantContext.getTenantId(), currentUserName);
            } else if (questionType == QuestionType.TRUE_FALSE) {
                jobId = questionBankTrueOrFalseService.uploadQuestionsFromCsvBatchJob(fileToImport.getPath(),
                    tenantContext.getTenantId(), currentUserName);
            } else if (questionType == QuestionType.SUBJECTIVE) {
                jobId = questionBankSubjectiveService.uploadQuestionsFromCsvBatchJob(fileToImport.getPath(),
                    tenantContext.getTenantId(), currentUserName);
            } else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid question type");
            }

            JobExecution jobExecution = jobExplorer.getJobExecution(jobId);
            return new ResponseEntity<>(new UploadJobStatusResponse(jobExecution), HttpStatus.OK);

        } catch (Exception e) {
            logger.error("Error in uploadQuestions: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        } finally {
            MDC.remove("requestId");
        }
    }

    @RequestMapping(value = "/jobstatus/{jobId}", method = RequestMethod.GET)
    public ResponseEntity<UploadJobStatusResponse> getJobStatus(@PathVariable Long jobId) {
        UUID uuid = UUID.randomUUID();
        MDC.put("requestId", uuid.toString());
        logger.info("Received request for job status with id: " + jobId);

        try {
            JobExecution jobExecution = jobExplorer.getJobExecution(jobId);
            if (jobExecution == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Job with id " + jobId + " not found");
            }
            return new ResponseEntity<>(new UploadJobStatusResponse(jobExecution), HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error in getJobStatus: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        } finally {
            MDC.remove("requestId");
        }
    }
}
