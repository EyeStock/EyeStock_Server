package com.api.sss.model.dto.response;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class ChatPredictResponse {
	private String coinTicker;
	private String coinName;
	private String prediction;
	private float prob;
	private LocalDateTime timestamp;
}
