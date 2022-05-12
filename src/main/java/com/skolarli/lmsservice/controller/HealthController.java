package com.skolarli.lmsservice.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {

    @RequestMapping("/gethealth")
    public String getHealth(){
        return "OK";
    }

}
