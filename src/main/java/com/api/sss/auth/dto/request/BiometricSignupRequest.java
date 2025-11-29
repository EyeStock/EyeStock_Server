package com.api.sss.login.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class BiometricSignupRequest {

	@NotBlank(message = "deviceId는 필수 값입니다.")
	@Schema(description = "기기 고유 ID", example = "abc123device")
	private String deviceId;

	@NotBlank(message = "publicKey는 필수 값입니다.")
	@Schema(description = "생성된 biometric 공개키 (Base64)", example = "MIIBIjANBgkqhkiG9...")
	private String publicKey;
}
