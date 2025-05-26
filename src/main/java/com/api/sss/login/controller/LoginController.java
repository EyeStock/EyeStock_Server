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

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class LoginController {
	private final LoginService loginService;

	@PostMapping("/biometric-signup")
	public ResponseEntity<CustomResponse<Void>> biometricSignup(@RequestBody BiometricSignupRequest request) {
		loginService.signup(request);
		return ResponseEntity.ok(CustomResponse.success(SuccessStatus.SUCCESS));
	}
}
