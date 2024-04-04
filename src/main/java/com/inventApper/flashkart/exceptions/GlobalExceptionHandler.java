package com.inventApper.flashkart.exceptions;

import com.inventApper.flashkart.dtos.ApiResponseMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponseMessage> handleResourceNotFoundException(ResourceNotFoundException ex) {
        logger.info("Exception handler invoked !!");
        return new ResponseEntity<>(new ApiResponseMessage(ex.getMessage(), false, HttpStatus.NOT_FOUND),
                HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleMethodArgsNotValidException(MethodArgumentNotValidException ex) {
        List<ObjectError> errorList = ex.getBindingResult().getAllErrors();
        Map<String, String> response = new HashMap<>();
        errorList.forEach(error -> response.put(((FieldError) error).getField(), error.getDefaultMessage()));
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BadApiRequestException.class)
    public ResponseEntity<ApiResponseMessage> badApiRequestHandler(BadApiRequestException exception) {

        logger.info("Bad Api Request !!");

        String message = exception.getMessage();
        return new ResponseEntity<>(new ApiResponseMessage(message, false, HttpStatus.BAD_REQUEST),
                HttpStatus.BAD_REQUEST);
    }

}
