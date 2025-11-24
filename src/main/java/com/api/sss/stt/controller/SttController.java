package com.api.sss.stt.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.api.sss.config.response.dto.CustomResponse;
import com.api.sss.config.response.dto.SuccessStatus;
import com.api.sss.stt.dto.response.ChatPredictResponse;
import com.api.sss.stt.service.SttService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class SttController {

	private final SttService sttService;

	@Operation(
		summary = "코인 시세 예측 API",
		description = "STT로 코인종목 명을 입력받아 FastAPI 서비스에 전달하고 답변을 받아옵니다."
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
		// 	종목명 인식 실패했을 시 response 추가 필요
	)
	@PostMapping(path = "/chat/predict", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public CustomResponse<ChatPredictResponse> predictCoin(
		@Parameter(
			description = "업로드할 44100Hz mp3 음성 파일",
			content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE)
		)
		@RequestParam("file") MultipartFile file) {
		ChatPredictResponse response = sttService.predictCoin(file);
		return CustomResponse.success(response, SuccessStatus.SUCCESS);
	}

}
