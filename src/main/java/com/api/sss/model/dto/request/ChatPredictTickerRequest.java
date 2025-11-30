package com.api.sss.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ChatPredictTickerRequest {
	private String coinTicker;
}
