package com.e_aganda.demo.controllerAdvice;

import com.e_aganda.demo.error.ErrorHandler;
import com.e_aganda.demo.error.ProjetNotFound;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ProjetControllerAdvice {
    @ExceptionHandler(ProjetNotFound.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorHandler handleExeption(ProjetNotFound ex){
        return new ErrorHandler(ex.getMessage());
    }
}
