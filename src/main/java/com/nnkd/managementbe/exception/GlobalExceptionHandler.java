package com.nnkd.managementbe.exception;

import com.nnkd.managementbe.dto.request.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailSendException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(value = RuntimeException.class)
    ResponseEntity<ApiResponse> handlingRuntimeException(RuntimeException e) {
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setStatus(false);
        apiResponse.setMessage(e.getMessage());
        return ResponseEntity.badRequest().body(apiResponse);
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    ResponseEntity<ApiResponse> handlingValidation(MethodArgumentNotValidException e) {
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setStatus(false);
        apiResponse.setMessage(e.getFieldError().getDefaultMessage());
        return ResponseEntity.badRequest().body(apiResponse);
    }

    @ExceptionHandler(value = MailSendException.class)
    ResponseEntity<ApiResponse> handlingMessagingException(MailSendException e) {
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setStatus(false);
        apiResponse.setMessage(e.getMessageExceptions()[0].getMessage());
        return ResponseEntity.badRequest().body(apiResponse);
    }


}
