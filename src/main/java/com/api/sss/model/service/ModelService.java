package com.api.sss.model.service;

import com.api.sss.model.dto.NewsRequest;
import com.api.sss.model.dto.NewsResponse;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ModelService {
    private final RestTemplate restTemplate = new RestTemplate();
    private final String FASTAPI_URL = "http://203.153.147.12:5050";

    public NewsResponse fetchNews(NewsRequest request) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<NewsRequest> entity = new HttpEntity<>(request, headers);

        ResponseEntity<NewsResponse> response = restTemplate.exchange(
                FASTAPI_URL + "/news",
                HttpMethod.POST,
                entity,
                NewsResponse.class
        );

        return response.getBody();
    }
}
