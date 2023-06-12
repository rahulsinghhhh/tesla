package com.example.tesla.api.controller;

import com.example.tesla.exception.IncorrectFormattingException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class TempExceptionController {
    @ExceptionHandler(value = IncorrectFormattingException.class)
    public ResponseEntity<Object> exception(IncorrectFormattingException exception) {
        Map<String, String> map = new HashMap();
        map.put("error","bad request");
        return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
    }
}