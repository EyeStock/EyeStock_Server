package com.api.sss.stt.service;

import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.api.sss.config.exception.CustomException;
import com.api.sss.config.exception.ErrorCode;
import com.api.sss.model.service.ModelService;
import com.api.sss.stt.dto.response.ChatPredictResponse;
import com.google.cloud.speech.v1.RecognitionAudio;
import com.google.cloud.speech.v1.RecognitionConfig;
import com.google.cloud.speech.v1.RecognizeResponse;
import com.google.cloud.speech.v1.SpeechClient;
import com.google.cloud.speech.v1.SpeechRecognitionAlternative;
import com.google.cloud.speech.v1.SpeechRecognitionResult;
import com.google.protobuf.ByteString;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SttService {

	private final SpeechClient speechClient;
	private final ModelService modelService;

	public ChatPredictResponse predictCoin(@RequestParam("file") MultipartFile file) {
		String coinName = transcribe(file);

		return ChatPredictResponse.builder()
			.prediction(modelService.predictCoin(coinName))
			.build();
	}

	public String transcribe(MultipartFile file) {
		if (file.isEmpty()) {
			throw new CustomException(ErrorCode.FILE_NOT_FOUND);
		}

		try {
			ByteString audioBytes = ByteString.copyFrom(file.getBytes());

			RecognitionAudio audio = RecognitionAudio.newBuilder()
				.setContent(audioBytes)
				.build();

			RecognitionConfig config = RecognitionConfig.newBuilder()
				.setEncoding(RecognitionConfig.AudioEncoding.MP3)
				.setSampleRateHertz(44100)
				.setLanguageCode("ko-KR")
				.build();

			RecognizeResponse response = speechClient.recognize(config, audio);

			List<SpeechRecognitionResult> results = response.getResultsList();
			StringBuilder transcription = new StringBuilder();

			for (SpeechRecognitionResult result : results) {
				SpeechRecognitionAlternative alternative = result.getAlternativesList().get(0);
				transcription.append(alternative.getTranscript());
			}

			return transcription.toString();
		} catch (IOException e) {
			throw new CustomException(ErrorCode.FILE_NOT_FOUND);
		}

	}

}
