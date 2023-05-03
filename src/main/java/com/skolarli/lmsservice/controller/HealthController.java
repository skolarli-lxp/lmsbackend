package com.skolarli.lmsservice.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {

    final Logger logger = LoggerFactory.getLogger(HealthController.class);

    @RequestMapping("/gethealth")
    public String getHealth() {
        return "OK";
    }

    @RequestMapping("/gethealthnoauth")
    public String getHealthOpen() {
        logger.info("Health controller: No auth");
        return "OK";
    }

}
