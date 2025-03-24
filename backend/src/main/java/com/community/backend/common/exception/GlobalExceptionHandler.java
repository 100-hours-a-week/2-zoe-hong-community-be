package com.community.backend.common.exception;

import com.community.backend.dto.ResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ResponseDTO> handleCustomException(CustomException e) {
        ResponseDTO res = new ResponseDTO(false, e.getMessage());
        return ResponseEntity.status(e.getStatus()).body(res);
    }
}
