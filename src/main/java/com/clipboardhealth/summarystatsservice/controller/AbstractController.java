package com.clipboardhealth.summarystatsservice.controller;

import com.clipboardhealth.summarystatsservice.pojo.response.ErrorResponse;
import com.clipboardhealth.summarystatsservice.pojo.response.Response;

public abstract class AbstractController {
    protected <T> Response<?> getSuccessResponse(T t) {
        return new Response<>(true, t, null);
    }

    protected <T> Response<?> getFailureResponse(T t, ErrorResponse error) {
        return new Response<>(true, t, error);
    }
}
