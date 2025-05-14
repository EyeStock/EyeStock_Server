package com.api.sss.config.response.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SuccessStatus {
    SUCCESS(200,"성공"),
    ;

    private final int code;
    private final String message;
}
