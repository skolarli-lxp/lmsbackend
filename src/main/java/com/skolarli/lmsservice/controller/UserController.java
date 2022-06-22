package com.skolarli.lmsservice.controller;

import com.skolarli.lmsservice.models.db.LmsUser;
import com.skolarli.lmsservice.services.LmsUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "user")
public class UserController {

    Logger logger = LoggerFactory.getLogger(UserController.class);
    @Autowired
    private LmsUserService lmsUserService;

    @PostMapping
    public ResponseEntity<LmsUser> addNewUser(@Valid @RequestBody LmsUser lmsUser) {
        logger.info("Received new user request Email: " + lmsUser.getEmail() );
        return new ResponseEntity<LmsUser>(lmsUserService.saveLmsUser(lmsUser), HttpStatus.CREATED);
    }
}
