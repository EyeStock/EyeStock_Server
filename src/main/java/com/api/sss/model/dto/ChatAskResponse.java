package com.api.sss.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ChatAskResponse {
	private String answer;
	private List<String> sources;

	@JsonProperty("latency_ms")  // ← 서버의 latency_ms를 매핑
	private long latencyMs;
}
