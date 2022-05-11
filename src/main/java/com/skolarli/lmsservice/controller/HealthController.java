package com.skolarli.lmsservice.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {

    @RequestMapping("/getHealth")
    public String getHealth(){
        return "OK";
    }

    @RequestMapping("/getRestrictedHealth")
    public String getAuthorizedHealth(){
        return "OK: Authorized";
    }
}
