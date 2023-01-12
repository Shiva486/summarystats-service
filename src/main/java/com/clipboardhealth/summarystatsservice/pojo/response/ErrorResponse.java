package com.clipboardhealth.summarystatsservice.pojo.response;

import lombok.Data;

import java.beans.ConstructorProperties;

@Data
public class ErrorResponse {
    private int code;
    private String type;
    private String message;

    @ConstructorProperties({"errorCode", "type", "message"})
    public ErrorResponse(int code, String type, String message) {
        this.code = code;
        this.type = type;
        this.message = message;
    }
}
