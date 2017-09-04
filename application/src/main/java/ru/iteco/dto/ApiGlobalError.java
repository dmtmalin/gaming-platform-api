package ru.iteco.dto;

import org.springframework.validation.ObjectError;

public class ApiGlobalError extends ApiErrorImpl {

    private ApiGlobalError(ObjectError e) {
        super(e);
        super.setType(ApiErrorType.GLOBAL_ERROR);
    }

    public static ApiGlobalError valueOf(ObjectError e) {
        return new ApiGlobalError(e);
    }
}
