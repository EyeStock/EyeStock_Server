package com.api.sss.model.controller;

import java.util.List;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.api.sss.config.response.dto.CustomResponse;
import com.api.sss.config.response.dto.SuccessStatus;
import com.api.sss.model.dto.request.ChatAskRequest;
import com.api.sss.model.dto.request.ChatPredictRequest;
import com.api.sss.model.dto.request.NewsRequest;
import com.api.sss.model.dto.response.ChatAskResponse;
import com.api.sss.model.dto.response.ChatPredictResponse;
import com.api.sss.model.dto.response.NewsResponse;
import com.api.sss.model.service.CoinTickerService;
import com.api.sss.model.service.ModelService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class ModelController {

	RestTemplate restTemplate = new RestTemplate();
	private final ModelService modelService;
	private final CoinTickerService coinTickerService;

	@Operation(
		summary = "코인 시세 예측 API",
		description = "코인종목 명을 입력받아 FastAPI 서비스에 전달하고 답변을 받아옵니다."
	)
	@ApiResponse(responseCode = "200", description = "답변 수신 성공")
	@ApiResponse(
		responseCode = "400",
		description = "코인 종목-ticker 간 매칭 실패",
		content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
			{
			  "code": 400,
			  "message": "해당 코인을 찾을 수 없습니다."
			}
			"""))
	)
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
	@PostMapping(path = "/chat/predict")
	public CustomResponse<ChatPredictResponse> predictCoin(
		@RequestBody ChatPredictRequest request) {
		ChatPredictResponse response = modelService.predictCoin(request);
		return CustomResponse.success(response, SuccessStatus.SUCCESS);
	}

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
	@PostMapping("/chat/ask")
	public CustomResponse<ChatAskResponse> askQuestion(@RequestBody ChatAskRequest request) {
		ChatAskResponse response = modelService.askQuestion(request);
		return CustomResponse.success(response, SuccessStatus.SUCCESS);
	}

	@Operation(
		summary = "카드뉴스 API",
		description = "키워드를 입력하면 관련 뉴스 URL을 반환합니다."
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
	@PostMapping("/news")
	public CustomResponse<List<NewsResponse.Result>> getNews(@RequestBody NewsRequest request) {
		List<NewsResponse.Result> results = modelService.cardNews(request);
		return CustomResponse.success(results, SuccessStatus.SUCCESS);
	}

}
