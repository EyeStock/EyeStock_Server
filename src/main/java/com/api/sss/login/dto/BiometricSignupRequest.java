package com.api.sss.login.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class BiometricSignupRequest {
	@Schema(description = "기기 고유 ID", example = "abc123device")
	private String deviceId;

	@Schema(description = "생성된 biometric 공개키 (Base64)", example = "MIIBIjANBgkqhkiG9...")
	private String publicKey;
}
