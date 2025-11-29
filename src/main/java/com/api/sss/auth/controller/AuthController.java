package com.api.sss.auth.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.sss.config.response.dto.CustomResponse;
import com.api.sss.config.response.dto.SuccessStatus;
import com.api.sss.auth.dto.request.BiometricLoginStartRequest;
import com.api.sss.auth.dto.response.BiometricLoginStartResponse;
import com.api.sss.auth.dto.request.BiometricLoginVerifyRequest;
import com.api.sss.auth.dto.response.BiometricLoginVerifyResponse;
import com.api.sss.auth.dto.request.BiometricSignupRequest;
import com.api.sss.auth.dto.request.RefreshTokenRequest;
import com.api.sss.auth.dto.response.RefreshTokenResponse;
import com.api.sss.auth.service.AuthService;

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
public class AuthController {
	private final AuthService authService;

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
		authService.signup(request);
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
		BiometricLoginStartResponse response = authService.startLogin(request);
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
		BiometricLoginVerifyResponse response = authService.verifyLogin(request);
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
		RefreshTokenResponse response = authService.reissue(request);
		return ResponseEntity.ok(CustomResponse.success(response, SuccessStatus.SUCCESS));
	}

	@Operation(
		summary = "로그아웃 API",
		description = """
			현재 Authorization 헤더에 포함된 Access Token의 소유자를 로그아웃 처리합니다.
			- 서버에서는 Access Token에서 userId를 추출한 뒤, 해당 유저의 refreshToken을 Redis에서 삭제합니다.
			- 이 후에는 해당 유저는 토큰 재발급이 불가능하며, 다시 로그인해야 합니다.
			"""
	)
	@ApiResponses(value = {
		@ApiResponse(
			responseCode = "200",
			description = "로그아웃 성공",
			content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
	        {
	          "code": 200,
	          "message": "로그아웃에 성공했습니다.",
	          "data": null
	        }
	        """))
		),
		@ApiResponse(
			responseCode = "401",
			description = "유효하지 않은 또는 만료된 Access Token",
			content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
	        {
	          "code": 401,
	          "message": "유효하지 않은 토큰입니다."
	        }
	        """))
		)
	})
	@PostMapping("/logout")
	public ResponseEntity<Void> logout(
		@RequestHeader("Authorization") String authorizationHeader
	) {
		authService.logout(authorizationHeader);
		return ResponseEntity.ok().build();
	}
}
