package com.example.wanderfunmobile.network.dto;

import java.util.Date;

public class ResponseDto<T> {
    private String statusCode;
    private Date timestamp;
    private String message;
    private T data;

    public ResponseDto() {
        this.timestamp = new Date();
        this.data = null;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
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