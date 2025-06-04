package com.api.sss.test.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class TestController {
    RestTemplate restTemplate = new RestTemplate();
    String url = "http://203.153.148.56:5050";

    @GetMapping("/test")
    public String healthCheck()
    {
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        return "NAS 연결 성공: " + response.getBody();
    }
}
