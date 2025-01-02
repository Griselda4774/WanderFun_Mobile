package com.example.wanderfunmobile.infrastructure.util;

import com.example.wanderfunmobile.application.dto.ResponseDto;

public class ErrorGenerateUtil {


    public static ResponseDto<Object> createOnResponseError(String errorType) {
        ResponseDto<Object> errorResponse = new ResponseDto<>();
        errorResponse.setStatusCode("400 CLIENT ERROR");
        errorResponse.setErrorType(errorType);
        errorResponse.setMessage("Error during onResponse");
        errorResponse.setData(null);
        errorResponse.setError(true);
        return errorResponse;
    }

    public static ResponseDto<Object> createOnFailureError(String errorType) {
        ResponseDto<Object> errorResponse = new ResponseDto<>();
        errorResponse.setStatusCode("400 CLIENT ERROR");
        errorResponse.setErrorType(errorType);
        errorResponse.setMessage("Error during onFailure");
        errorResponse.setData(null);
        errorResponse.setError(true);
        return errorResponse;
    }

    public static ResponseDto<Object> createCatchError(String errorType) {
        ResponseDto<Object> errorResponse = new ResponseDto<>();
        errorResponse.setStatusCode("400 CLIENT ERROR");
        errorResponse.setErrorType(errorType);
        errorResponse.setMessage("Error during catch");
        errorResponse.setData(null);
        errorResponse.setError(true);
        return errorResponse;
    }
}
