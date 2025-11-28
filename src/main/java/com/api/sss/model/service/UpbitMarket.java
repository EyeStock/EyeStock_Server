package com.api.sss.model.service;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;

@Getter
public class UpbitMarket {

	@JsonProperty("market")
	private String market;

	@JsonProperty("korean_name")
	private String coinName;
}
