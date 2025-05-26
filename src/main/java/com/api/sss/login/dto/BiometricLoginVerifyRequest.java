package com.api.sss.login.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class BiometricLoginVerifyRequest {

	@NotBlank(message = "deviceId는 필수입니다.")
	private String deviceId;

	@NotBlank(message = "challenge는 필수입니다.")
	private String challenge;

	@NotBlank(message = "signature는 필수입니다.")
	private String signature;

}
