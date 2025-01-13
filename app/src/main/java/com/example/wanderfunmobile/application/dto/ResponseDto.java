package com.example.wanderfunmobile.application.dto;

import java.util.Date;
import java.util.Map;

public class ResponseDto<T> {
    private String statusCode;
    private boolean error;
    private String errorType;
    private Map<String, String> errorData;
    private Date timestamp;
    private String message;
    private T data;

    public ResponseDto() {}

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean isError) {
        error = isError;
    }

    public String getErrorType() {
        return errorType;
    }

    public void setErrorType(String errorType) {
        this.errorType = errorType;
    }

    public Map<String, String> getErrorData() {
        return errorData;
    }

    public void setErrorData(Map<String, String> errorData) {
        this.errorData = errorData;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}