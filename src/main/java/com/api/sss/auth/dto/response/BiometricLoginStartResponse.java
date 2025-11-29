package com.api.sss.auth.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class

BiometricLoginStartResponse {

	@Schema(description = "서명 대상 Challenge 문자열", example = "f7a8f329-d2b4-4bd1-8c33-43008b87a212")
	private String challenge;

}
