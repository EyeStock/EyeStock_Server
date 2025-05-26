package com.api.sss.login.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.sss.config.response.dto.CustomResponse;
import com.api.sss.config.response.dto.SuccessStatus;
import com.api.sss.login.dto.BiometricSignupRequest;
import com.api.sss.login.service.LoginService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class LoginController {
	private final LoginService loginService;

	@Operation(summary = "지문 로그인 회원가입 API", description = "device-id와 publicKey 저장하는 회원가입 API입니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "device-id & public key 저장 성공"),
		@ApiResponse(
			responseCode = "400",
			description = "이미 등록된 기기",
			content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
			{
			  "code": 400,
			  "message": "이미 등록된 디바이스입니다."
			}"""))
		)
	})
	@PostMapping("/biometric-signup")
	public ResponseEntity<CustomResponse<Void>> biometricSignup(@RequestBody BiometricSignupRequest request) {
		loginService.signup(request);
		return ResponseEntity.ok(CustomResponse.success(SuccessStatus.SUCCESS));
	}
}
