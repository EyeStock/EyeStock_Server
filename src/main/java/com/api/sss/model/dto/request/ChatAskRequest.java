package com.api.sss.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class ChatAskRequest {
	@NotBlank(message = "질문은 비어 있을 수 없습니다.")
	private String message;
}
