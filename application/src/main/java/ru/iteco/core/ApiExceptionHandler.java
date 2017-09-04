package ru.iteco.core;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ru.iteco.dto.ApiErrorFactory;
import ru.iteco.dto.ApiErrorResponse;
import ru.iteco.error.ApplicationException;

@ControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

    // 400
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            final MethodArgumentNotValidException ex,
            final HttpHeaders headers,
            final HttpStatus status,
            final WebRequest request) {
        logger.info(ex.getClass().getName());
        BindingResult bindingResult = ex.getBindingResult();
        String message = String.format("Validation failed for object='%s'. Error count: %s",
                bindingResult.getTarget().getClass().getSimpleName(),
                bindingResult.getErrorCount());
        final ApiErrorResponse response = ApiErrorResponse.valueOf(
                HttpStatus.BAD_REQUEST.value(), getPath(request), message, ex.getClass().getName());
        bindingResult.getAllErrors().forEach(e ->
                response.getErrors().add(ApiErrorFactory.buildFromObjectError(e)));
        return handleExceptionInternal(ex, response, headers, HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler({ ApplicationException.class })
    public ResponseEntity<Object> handleRestException(final Exception ex, final WebRequest request) {
        logger.info(ex.getClass().getName());
        String error = ex.getLocalizedMessage();
        if (StringUtils.isEmpty(error)) {
            error = "message not available";
        }
        final ApiErrorResponse response = ApiErrorResponse.valueOf(
                HttpStatus.BAD_REQUEST.value(), getPath(request), error, ex.getClass().getName());
        return new ResponseEntity<>(response, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    private String getPath(WebRequest request) {
        return ((ServletWebRequest) request)
                .getRequest()
                .getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE)
                .toString();
    }

    @Override
    protected ResponseEntity<Object> handleServletRequestBindingException(
            final ServletRequestBindingException ex,
            final HttpHeaders headers,
            final HttpStatus status,
            final WebRequest request) {
        logger.info(ex.getClass().getName());
        final ApiErrorResponse response = ApiErrorResponse.valueOf(
                HttpStatus.BAD_REQUEST.value(), getPath(request), ex.getLocalizedMessage(), ex.getClass().getName());
        return new ResponseEntity<>(response, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }
}
