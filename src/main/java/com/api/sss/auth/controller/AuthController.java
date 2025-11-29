package com.api.sss.login.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.sss.config.response.dto.CustomResponse;
import com.api.sss.config.response.dto.SuccessStatus;
import com.api.sss.login.dto.request.BiometricLoginStartRequest;
import com.api.sss.login.dto.response.BiometricLoginStartResponse;
import com.api.sss.login.dto.request.BiometricLoginVerifyRequest;
import com.api.sss.login.dto.response.BiometricLoginVerifyResponse;
import com.api.sss.login.dto.request.BiometricSignupRequest;
import com.api.sss.login.dto.request.RefreshTokenRequest;
import com.api.sss.login.dto.response.RefreshTokenResponse;
import com.api.sss.login.service.LoginService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
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
	public ResponseEntity<CustomResponse<Void>> biometricSignup(@Valid @RequestBody BiometricSignupRequest request) {
		loginService.signup(request);
		return ResponseEntity.ok(CustomResponse.success(SuccessStatus.SUCCESS));
	}

	@Operation(summary = "지문 로그인 시작 API", description = "deviceId로 challenge를 발급하여 반환합니다.")
	@ApiResponse(
		responseCode = "200",
		description = "challenge 발급 성공"
	)
	@ApiResponse(
		responseCode = "404",
		description = "등록되지 않은 deviceId",
		content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
        {
          "code": 404,
          "message": "해당 사용자를 찾을 수 없습니다."
        }
        """))
	)
	@PostMapping("/biometric-login/start")
	public ResponseEntity<CustomResponse<BiometricLoginStartResponse>> biometricLoginStart(
		@Valid @RequestBody BiometricLoginStartRequest request) {
		BiometricLoginStartResponse response = loginService.startLogin(request);
		return ResponseEntity.ok(CustomResponse.success(response, SuccessStatus.SUCCESS));
	}

	@Operation(summary = "지문 로그인 검증 API", description = "challenge 및 signature를 검증하고 토큰을 발급합니다.")
	@ApiResponse(
		responseCode = "200",
		description = "토큰 발급 성공"
	)
	@ApiResponse(
		responseCode = "400",
		description = "challenge 또는 signature가 유효하지 않음",
		content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
    {
      "code": 400,
      "message": "유효하지 않은 challenge 또는 signature입니다."
    }
    """))
	)
	@ApiResponse(
		responseCode = "404",
		description = "등록되지 않은 deviceId",
		content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
    {
      "code": 404,
      "message": "해당 사용자를 찾을 수 없습니다."
    }
    """))
	)
	@PostMapping("/biometric-login/verify")
	public ResponseEntity<CustomResponse<BiometricLoginVerifyResponse>> biometricLoginVerify(
		@Valid @RequestBody BiometricLoginVerifyRequest request) {
		BiometricLoginVerifyResponse response = loginService.verifyLogin(request);
		return ResponseEntity.ok(CustomResponse.success(response, SuccessStatus.SUCCESS));
	}

	@Operation(
		summary = "토큰 재발급 API",
		description = "유효한 refreshToken을 통해 accessToken과 refreshToken을 재발급합니다."
	)
	@ApiResponse(
		responseCode = "200",
		description = "토큰 재발급 성공"
	)
	@ApiResponse(
		responseCode = "400",
		description = "refreshToken이 유효하지 않음",
		content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
    {
      "code": 400,
      "message": "Refresh Token이 유효하지 않습니다."
    }
    """))
	)
	@PostMapping("/reissue")
	public ResponseEntity<CustomResponse<RefreshTokenResponse>> reissue(
		@Valid @RequestBody RefreshTokenRequest request) {
		RefreshTokenResponse response = loginService.reissue(request);
		return ResponseEntity.ok(CustomResponse.success(response, SuccessStatus.SUCCESS));
	}
}
