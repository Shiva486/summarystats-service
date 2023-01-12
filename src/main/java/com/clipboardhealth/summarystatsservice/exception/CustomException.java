package com.clipboardhealth.summarystatsservice.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CustomException extends RuntimeException {
    private Integer code;
    private String message;

    public CustomException(ErrorCodes errorCodes) {
        this.code = errorCodes.value();
        this.message = errorCodes.getReasonPhrase();
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }
}
