package com.api.sss.auth.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class BiometricLoginVerifyRequest {

	@NotBlank(message = "deviceId는 필수입니다.")
	@Schema(description = "기기 고유 ID", example = "abc123device")
	private String deviceId;

	@NotBlank(message = "challenge는 필수입니다.")
	private String challenge;

	@NotBlank(message = "signature는 필수입니다.")
	private String signature;

}
