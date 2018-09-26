package com.kazan.exception;

import com.kazan.dto.ErrorResponseDto;
import com.kazan.dto.ResponseDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(ValidationCustomException.class)
    public ResponseEntity<ErrorResponseDto> handleValidationException(ValidationCustomException ex) {
        ErrorResponseDto errorResponse = new ErrorResponseDto(ex.getMessage());
        LOGGER.error(ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
}
