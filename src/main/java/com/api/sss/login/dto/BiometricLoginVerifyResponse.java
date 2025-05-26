package com.api.sss.login.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BiometricLoginVerifyResponse {
	private String accessToken;
	private String refreshToken;
}
