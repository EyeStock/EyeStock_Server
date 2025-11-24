package com.api.sss.model.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.api.sss.config.exception.CustomException;
import com.api.sss.config.exception.ErrorCode;
import com.api.sss.model.dto.request.ChatAskRequest;
import com.api.sss.model.dto.request.NewsRequest;
import com.api.sss.model.dto.response.ChatAskResponse;
import com.api.sss.model.dto.response.NewsResponse;

@Service
public class ModelService {

	private final RestTemplate restTemplate = new RestTemplate();
	private final String FASTAPI_URL = "http://203.153.147.12:5050";

	public List<NewsResponse.Result> cardNews(NewsRequest request) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		HttpEntity<NewsRequest> entity = new HttpEntity<>(request, headers);

		try {
			ResponseEntity<NewsResponse> response = restTemplate.exchange(
				FASTAPI_URL + "/news",
				HttpMethod.POST,
				entity,
				NewsResponse.class
			);
			return response.getBody().getResults();
		} catch (Exception e) {
			throw new CustomException(ErrorCode.FASTAPI_COMMUNICATION_ERROR);
		}
	}

	public ChatAskResponse askQuestion(ChatAskRequest request) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		HttpEntity<ChatAskRequest> entity = new HttpEntity<>(request, headers);

		try {
			ResponseEntity<ChatAskResponse> response = restTemplate.exchange(
				FASTAPI_URL + "/chat",
				HttpMethod.POST,
				entity,
				ChatAskResponse.class
			);
			return response.getBody();
		} catch (Exception e) {
			throw new CustomException(ErrorCode.FASTAPI_COMMUNICATION_ERROR);
		}
	}

	public String predictCoin(String coinName) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		Map<String, String> body = new HashMap<>();
		body.put("coinName", coinName);
		HttpEntity<Map<String, String>> entity = new HttpEntity<>(body, headers);

		try {
			// TODO: return response.getBody();
			// ResponseEntity<String> response = restTemplate.exchange(
			// 	FASTAPI_URL + "/chat/predict",
			// 	HttpMethod.POST,
			// 	entity,
			// 	String.class
			// );
			return coinName;
		} catch (Exception e) {
			throw new CustomException(ErrorCode.FASTAPI_COMMUNICATION_ERROR);
		}
	}
}
