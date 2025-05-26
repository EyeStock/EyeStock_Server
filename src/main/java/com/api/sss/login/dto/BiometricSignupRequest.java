package com.api.sss.login.dto;

import lombok.Getter;

@Getter
public class BiometricSignupRequest {
	private String deviceId;
	private String publicKey;
}
