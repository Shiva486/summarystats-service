package com.clipboardhealth.summarystatsservice.pojo.response;

import lombok.Data;

import java.beans.ConstructorProperties;

@Data
public class Response<T> {
    private boolean success;
    private T data;
    private ErrorResponse error;

    @ConstructorProperties({"success", "data", "error"})
    public Response(boolean success, T data, ErrorResponse error) {
        this.success = success;
        this.data = data;
        this.error = error;
    }
}
