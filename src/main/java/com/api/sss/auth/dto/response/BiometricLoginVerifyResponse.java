package com.api.sss.auth.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BiometricLoginVerifyResponse {
	private String accessToken;
	private String refreshToken;
}
