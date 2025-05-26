package com.api.sss.config.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    TEST_ERROR_CODE(400, "응답 테스트 실패입니다."),
    DEVICE_ALREADY_REGISTERED(400, "이미 등록된 디바이스입니다.");

    private final int code;
    private final String message;
}
