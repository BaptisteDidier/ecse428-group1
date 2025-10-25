package com.ecse428.flowfinder.exception;

import java.util.ArrayList;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.ecse428.flowfinder.dto.ErrorDto;

@ControllerAdvice
public class FlowFinderExceptionHandler {
    @ExceptionHandler(FlowFinderException.class)
    public ResponseEntity<ErrorDto> handleGameShopException(FlowFinderException e) {
        return new ResponseEntity<ErrorDto>(new ErrorDto(e.getMessage()), e.getStatus());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDto> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex){
       ArrayList<String> errorMessages = new ArrayList<String>();
        for (ObjectError err : ex.getAllErrors()) {
            errorMessages.add(err.getDefaultMessage());
        }
        ErrorDto responseBody = new ErrorDto(errorMessages);
        return new ResponseEntity<ErrorDto>(responseBody, HttpStatus.BAD_REQUEST);
    }
}
