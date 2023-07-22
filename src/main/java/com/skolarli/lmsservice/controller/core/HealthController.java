package com.skolarli.lmsservice.controller.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class HealthController {

    final Logger logger = LoggerFactory.getLogger(HealthController.class);

    @RequestMapping("/gethealth")
    public String getHealth() {

        UUID uuid = UUID.randomUUID();
        MDC.put("requestId", uuid.toString());
        logger.info("Health controller: With auth");

        MDC.remove("requestId");

        return "OK";
    }

    @RequestMapping("/gethealthnoauth")
    public String getHealthOpen() {

        UUID uuid = UUID.randomUUID();
        MDC.put("requestId", uuid.toString());
        logger.info("Health controller: No auth");

        MDC.remove("requestId");
        return "OK";
    }

}
