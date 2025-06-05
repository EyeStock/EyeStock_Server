package com.api.sss.test.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class TestController {

    @GetMapping("/test")
    public String healthCheck()
    {
        return "Connection successful";
    }
}
