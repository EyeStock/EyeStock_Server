package com.api.sss.config;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.speech.v1.SpeechClient;
import com.google.cloud.speech.v1.SpeechSettings;
import com.google.cloud.speech.v1.stub.SpeechStubSettings;

@Configuration
public class GoogleCloudConfig {

	// encoded-key (base64 문자열) 그대로 주입
	private final GoogleCredentials googleCredentials;

	public GoogleCloudConfig(
		@Value("${spring.cloud.gcp.credentials.encoded-key:}")
		String encodedKey
	) throws IOException {
		byte[] decoded = Base64.getDecoder().decode(encodedKey);
		this.googleCredentials =
			GoogleCredentials.fromStream(new ByteArrayInputStream(decoded));
	}

	@Bean
	public SpeechSettings speechSettings() throws IOException {
		return SpeechSettings.newBuilder()
			.setCredentialsProvider(() -> googleCredentials)
			.build();
	}

	@Bean
	public SpeechClient speechClient(SpeechSettings speechSettings) throws IOException {
		return SpeechClient.create(speechSettings);
	}
}
