package com.api.sss.model.service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.api.sss.config.exception.CustomException;
import com.api.sss.config.exception.ErrorCode;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CoinTickerService {

	private final RestTemplate restTemplate = new RestTemplate();
	private final Map<String, String> tickerMap = new ConcurrentHashMap<>();

	@PostConstruct
	public void getTickerMap() {
		String UPBIT_URL = "https://api.upbit.com/v1/market/all?isDetails=false";

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<Void> entity = new HttpEntity<>(headers);

		ResponseEntity<UpbitMarket[]> response = restTemplate.exchange(
			UPBIT_URL,
			HttpMethod.GET,
			entity,
			UpbitMarket[].class
		);

		UpbitMarket[] body = response.getBody();
		if (body == null) {
			return;
		}

		List<UpbitMarket> data = Arrays.asList(body);

		for (UpbitMarket coin : data) {
			String market = coin.getMarket();
			String coinName = coin.getCoinName();
			if (market != null && coinName != null && market.contains("KRW")) {
				tickerMap.put(coinName, market);
			}
		}

		tickerMap.put("엑스알피", "KRW-XRP");
		tickerMap.put("리플", "KRW-XRP");
	}

	public String getCoinTicker(String coinName) {
		if (tickerMap.get(coinName) != null) {
			return tickerMap.get(coinName);
		}
		throw new CustomException(ErrorCode.TICKER_NOT_FOUND);
	}

}


