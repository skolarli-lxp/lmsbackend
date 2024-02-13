package com.skolarli.lmsservice.controller.questionbank;

import com.skolarli.lmsservice.services.questionbank.QuestionBankMcqService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@RestController
@RequestMapping("/questionbank")
public class QuestionBankController {

    static final String TEMP_STORAGE = "/tmp/lmsservice/";
    final Logger logger = LoggerFactory.getLogger(QuestionBankController.class);
    final QuestionBankMcqService questionBankMcqService;


    public QuestionBankController(QuestionBankMcqService questionBankMcqService) {
        this.questionBankMcqService = questionBankMcqService;
    }

    @RequestMapping(value = "upload", method = RequestMethod.POST)
    public ResponseEntity<Long> uploadQuestions(@RequestParam MultipartFile file) {
        UUID uuid = UUID.randomUUID();
        MDC.put("requestId", uuid.toString());
        logger.info("Received request for upload questions from file : "
            + file.getOriginalFilename());

        String originalFileName = file.getOriginalFilename();

        try {
            Path directory = Paths.get(TEMP_STORAGE);
            Files.createDirectories(directory);

            File fileToImport = new File(TEMP_STORAGE + originalFileName);
            file.transferTo(fileToImport);

            Long jobId = questionBankMcqService.uploadQuestionsFromCsvBatchJob(fileToImport.getPath());

            return new ResponseEntity<>(jobId, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error in uploadQuestions: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        } finally {
            MDC.remove("requestId");
        }
    }
}
