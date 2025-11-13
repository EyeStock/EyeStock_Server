package com.api.sss.login.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RefreshTokenResponse {
	private String accessToken;
	private String refreshToken;
}
