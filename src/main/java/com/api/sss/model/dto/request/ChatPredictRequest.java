package com.api.sss.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class ChatPredictRequest {
	@NotBlank(message = "종목명은 비어 있을 수 없습니다.")
	private String coinName;
}
