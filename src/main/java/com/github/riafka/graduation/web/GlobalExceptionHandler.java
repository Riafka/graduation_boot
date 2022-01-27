package com.github.riafka.graduation.web;

import com.github.riafka.graduation.error.AppException;
import com.github.riafka.graduation.error.DataConflictException;
import com.github.riafka.graduation.util.validation.ValidationUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.persistence.EntityNotFoundException;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.boot.web.error.ErrorAttributeOptions.Include.MESSAGE;

@RestControllerAdvice
@AllArgsConstructor
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    public static final String EXCEPTION_DUPLICATE_EMAIL = "exception.user.duplicateEmail";
    public static final String EXCEPTION_DUPLICATE_NAME = "exception.restaurant.duplicateName";
    public static final String EXCEPTION_DUPLICATE_NAME_DATE = "exception.restaurantMenu.duplicateNameDate";
    public static final String EXCEPTION_DUPLICATE_USER_VOTE_DATE = "exception.vote.duplicateUserVoteDate";
    public static final String EXCEPTION_RESTAURANT_NOT_FOUND = "exception.vote.restaurantNotFound";

    private static final Map<String, String> CONSTRAINS_MAP = Map.of(
            "users_unique_email_idx", EXCEPTION_DUPLICATE_EMAIL,
            "restaurant_unique_name_idx", EXCEPTION_DUPLICATE_NAME,
            "unique_name_menu_date_idx", EXCEPTION_DUPLICATE_NAME_DATE,
            "unique_user_vote_date_idx", EXCEPTION_DUPLICATE_USER_VOTE_DATE,
            "restaurant_vote_fk", EXCEPTION_RESTAURANT_NOT_FOUND);

    private final ErrorAttributes errorAttributes;

    @ExceptionHandler(AppException.class)
    public ResponseEntity<?> appException(WebRequest request, AppException ex) {
        log.error("ApplicationException: {}", ex.getMessage());
        return createResponseEntity(request, ex.getOptions(), null, ex.getStatus());
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<?> entityNotFoundException(WebRequest request, EntityNotFoundException ex) {
        log.error("EntityNotFoundException: {}", ex.getMessage());
        return createResponseEntity(request, ErrorAttributeOptions.of(MESSAGE), null, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(DataConflictException.class)
    public ResponseEntity<?> dataConflictException(WebRequest request, DataConflictException ex) {
        log.error("DataConflictException: {}", ex.getMessage());
        return createResponseEntity(request, ErrorAttributeOptions.of(MESSAGE), null, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<?> conflict(WebRequest request, DataIntegrityViolationException e) {
        log.error("DataIntegrityViolationException: {}", e.getMessage());
        String rootMsg = ValidationUtil.getRootCause(e).getMessage();
        if (rootMsg != null) {
            String lowerCaseMsg = rootMsg.toLowerCase();
            for (Map.Entry<String, String> entry : CONSTRAINS_MAP.entrySet()) {
                if (lowerCaseMsg.contains(entry.getKey())) {
                    return createResponseEntity(request, ErrorAttributeOptions.of(MESSAGE), entry.getValue(), HttpStatus.UNPROCESSABLE_ENTITY);
                }
            }
        }
        return createResponseEntity(request, ErrorAttributeOptions.of(MESSAGE), rootMsg, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @NonNull
    @Override
    protected ResponseEntity<Object> handleExceptionInternal(
            @NonNull Exception ex, Object body, @NonNull HttpHeaders headers, @NonNull HttpStatus status, @NonNull WebRequest request) {
        log.error("Exception", ex);
        super.handleExceptionInternal(ex, body, headers, status, request);
        return createResponseEntity(request, ErrorAttributeOptions.of(), ValidationUtil.getRootCause(ex).getMessage(), status);
    }

    @NonNull
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            @NonNull HttpHeaders headers, @NonNull HttpStatus status, @NonNull WebRequest request) {
        return handleBindingErrors(ex.getBindingResult(), request);
    }

    @NonNull
    @Override
    protected ResponseEntity<Object> handleBindException(
            BindException ex, @NonNull HttpHeaders headers, @NonNull HttpStatus status, @NonNull WebRequest request) {
        return handleBindingErrors(ex.getBindingResult(), request);
    }

    private ResponseEntity<Object> handleBindingErrors(BindingResult result, WebRequest request) {
        String msg = result.getFieldErrors().stream()
                .map(fe -> String.format("[%s] %s", fe.getField(), fe.getDefaultMessage()))
                .collect(Collectors.joining("\n"));
        return createResponseEntity(request, ErrorAttributeOptions.defaults(), msg, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @SuppressWarnings("unchecked")
    private <T> ResponseEntity<T> createResponseEntity(WebRequest request, ErrorAttributeOptions options, String msg, HttpStatus status) {
        Map<String, Object> body = errorAttributes.getErrorAttributes(request, options);
        if (msg != null) {
            body.put("message", msg);
        }
        body.put("status", status.value());
        body.put("error", status.getReasonPhrase());
        return (ResponseEntity<T>) ResponseEntity.status(status).body(body);
    }
}
