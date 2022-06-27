package com.skolarli.lmsservice.controller;

import com.skolarli.lmsservice.exception.OperationNotSupportedException;
import com.skolarli.lmsservice.models.db.LmsUser;
import com.skolarli.lmsservice.services.LmsUserService;
import com.skolarli.lmsservice.utils.UserUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "user")
public class UserController {

    Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserUtils userUtils;

    @Autowired
    private LmsUserService lmsUserService;

    @PostMapping
    public ResponseEntity<LmsUser> addNewUser(@Valid @RequestBody LmsUser lmsUser) {
        logger.info("Received new user request Email: " + lmsUser.getEmail() );
        LmsUser currentUser = userUtils.getCurrentUser();
        if (currentUser.getIsAdmin() != true) {
            throw new ResponseStatusException( HttpStatus.FORBIDDEN, "Permission denied");
        }
        return new ResponseEntity<LmsUser>(lmsUserService.saveLmsUser(lmsUser), HttpStatus.CREATED);
    }

    @PutMapping(value="{id}")
    public ResponseEntity<LmsUser> updateUser(@PathVariable long id, @RequestBody LmsUser lmsUser) {
        logger.info("Received update user request Email: " + lmsUser.getEmail() );
        LmsUser currentUser = userUtils.getCurrentUser();
        if (currentUser.getIsAdmin() != true && currentUser.getId() != id) {
            throw new ResponseStatusException( HttpStatus.FORBIDDEN, "Permission denied");
        }

        try {
            return new ResponseEntity<>(lmsUserService.updateLmsUser(lmsUser, id), HttpStatus.OK);
        } catch (OperationNotSupportedException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<LmsUser>> getAllUsers() {
        logger.info("Received List All Users request");
        LmsUser currentUser = userUtils.getCurrentUser();
        if (currentUser.getIsAdmin() != true) {
            throw new ResponseStatusException( HttpStatus.FORBIDDEN, "Permission denied");
        }
        return new ResponseEntity<>(lmsUserService.getAllLmsUsers(), HttpStatus.OK);
    }

    @GetMapping(value = "{id}")
    public ResponseEntity<LmsUser> getUser(@PathVariable long id) {
        logger.info("Received Get User request UserId: " + id);
        LmsUser currentUser = userUtils.getCurrentUser();
        if (currentUser.getIsAdmin() != true && currentUser.getId() != id) {
            throw new ResponseStatusException( HttpStatus.FORBIDDEN, "Permission denied");
        }
        return new ResponseEntity<>(lmsUserService.getLmsUserById(id), HttpStatus.OK);
    }
}
