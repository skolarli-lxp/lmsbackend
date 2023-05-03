package com.skolarli.lmsservice.controller;

import com.skolarli.lmsservice.models.db.VerificationCode;
import com.skolarli.lmsservice.services.VerificationService;
import org.apache.coyote.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;


@RestController
@RequestMapping(value = "/verify")
public class VerificationController {

    Logger logger = LoggerFactory.getLogger(VerificationController.class);

    @Autowired
    private VerificationService verificationService;

    @GetMapping(value = "/verifytoken")
    public ResponseEntity<String> verifyToken(@RequestParam String token) {
        logger.info("Received user verification request Token: " + token);
        Boolean result = verificationService.verifyCode(token);
        if (result) {
            return new ResponseEntity<>("OK", HttpStatus.OK);
        } else {
            throw new ResponseStatusException( HttpStatus.BAD_REQUEST,
                    "Verification token expired");
        }
    }

    @GetMapping(value = "/gettoken")
    public ResponseEntity<VerificationCode> getToken(@RequestParam Long userId) {
        VerificationCode code = verificationService.getVerificationCodeByUser(userId);
        return new ResponseEntity<>(code, HttpStatus.OK);
    }
}
