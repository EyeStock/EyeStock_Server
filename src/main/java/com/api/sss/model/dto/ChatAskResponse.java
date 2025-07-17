package com.api.sss.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ChatAskResponse {
	private String question;
	private String answer;
	private double elapsed_time;
}
