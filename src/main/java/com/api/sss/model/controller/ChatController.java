package com.api.sss.model.controller;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.api.sss.config.exception.CustomException;
import com.api.sss.config.exception.ErrorCode;
import com.api.sss.config.response.dto.CustomResponse;
import com.api.sss.config.response.dto.SuccessStatus;
import com.api.sss.model.dto.ChatAskRequest;
import com.api.sss.model.dto.ChatAskResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/chat")
public class ChatController {

	RestTemplate restTemplate = new RestTemplate();

	@Operation(
		summary = "질문 전송 API",
		description = "질문을 FastAPI 서비스에 전달하고 답변을 받아옵니다."
	)
	@ApiResponse(responseCode = "200", description = "답변 수신 성공")
	@ApiResponse(
		responseCode = "500",
		description = "FastAPI 서비스 오류 또는 통신 실패",
		content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
        {
          "code": 500,
          "message": "FastAPI와의 통신 중 오류가 발생했습니다."
        }
        """))
	)
	@PostMapping("/ask")
	public ResponseEntity<CustomResponse<ChatAskResponse>> askQuestion(
		@Valid @RequestBody ChatAskRequest request) {

		String fastApiUrl = "http://203.153.148.56:5050/chatting";

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		HttpEntity<ChatAskRequest> entity = new HttpEntity<>(request, headers);

		try {
			ResponseEntity<ChatAskResponse> response = restTemplate.postForEntity(
				fastApiUrl, entity, ChatAskResponse.class
			);

			return ResponseEntity.ok(CustomResponse.success(response.getBody(), SuccessStatus.SUCCESS));
		} catch (Exception e) {
			throw new CustomException(ErrorCode.FASTAPI_COMMUNICATION_ERROR);
		}
	}
}
