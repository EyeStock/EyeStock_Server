package com.api.sss.login.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class BiometricLoginStartRequest {

	@NotBlank(message = "deviceId는 필수입니다.")
	@Schema(description = "기기 고유 ID", example = "abc123device")
	private String deviceId;

}
