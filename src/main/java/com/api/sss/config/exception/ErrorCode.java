package com.api.sss.config.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
	TEST_ERROR_CODE(400, "응답 테스트 실패입니다."),
	DEVICE_ALREADY_REGISTERED(400, "이미 등록된 디바이스입니다."),
	MEMBER_NOT_FOUND(404, "해당 사용자를 찾을 수 없습니다."),
	INVALID_CHALLENGE(401, "challenge 값이 유효하지 않습니다."),
	INVALID_SIGNATURE(401, "서명 검증에 실패했습니다."),
	EXPIRED_TOKEN(401, "토큰이 만료되었습니다."),
	INVALID_TOKEN(401, "유효하지 않은 토큰입니다."),
	INVALID_REFRESH_TOKEN(401, "Refresh Token이 유효하지 않습니다."),
	UNSUPPORTED_TOKEN(401, "지원하지 않는 토큰입니다."),
	FASTAPI_COMMUNICATION_ERROR(500, "FastAPI와의 통신 중 오류가 발생했습니다."),
	FILE_NOT_FOUND(400, "입력 파일이 없습니다."),
	FILE_READ_ERROR(500, "서버에서 파일을 읽어들이는 중 오류가 발생했습니다.");

	private final int code;
	private final String message;
}
