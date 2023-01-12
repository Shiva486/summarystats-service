package com.clipboardhealth.summarystatsservice.exception;

import org.springframework.lang.Nullable;

public enum ErrorCodes {

    INTERNAL_SERVER_ERROR(501, "Internal Server Error"),
    NO_ENTITY_FOUND(502, "No entity found for the request"),
    OPERATION_NOT_ALLOWED(503, "Operation not permitted for current user"),
    SQL_EXCEPTION(504, "SQLIntegrityConstraintViolationException"),
    CONSTRAINT_VIOLATION_EXCEPTION(505, "ConstraintViolationException"),
    DATA_INTEGRITY_VIOLATION_EXCEPTION(506, "DataIntegrityViolationException"),
    METHOD_ARGUMENT_NOT_VALID(507, "MethodArgumentNotValidException"),
    EXCEPTION(508, "Exception");

    private final int value;
    private final String reasonPhrase;

    ErrorCodes(int value, String reasonPhrase) {
        this.value = value;
        this.reasonPhrase = reasonPhrase;
    }

    public int value() {
        return this.value;
    }

    public String getReasonPhrase() {
        return this.reasonPhrase;
    }

    public String toString() {
        return this.value + " " + this.name();
    }

    public static ErrorCodes valueOf(int statusCode) {
        ErrorCodes status = resolve(statusCode);
        if (status == null) {
            throw new IllegalArgumentException("No matching constant for [" + statusCode + "]");
        } else {
            return status;
        }
    }

    @Nullable
    public static ErrorCodes resolve(int statusCode) {
        ErrorCodes[] errorCodes = values();

        for (ErrorCodes status : errorCodes) {
            if (status.value == statusCode) {
                return status;
            }
        }

        return null;
    }
}
