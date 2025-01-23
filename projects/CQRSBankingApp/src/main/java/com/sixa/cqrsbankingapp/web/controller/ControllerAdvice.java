package com.sixa.cqrsbankingapp.web.controller;

import com.sixa.cqrsbankingapp.domain.exception.ResourceAlreadyExistsException;
import com.sixa.cqrsbankingapp.domain.exception.ResourceNotFoundException;
import com.sixa.cqrsbankingapp.web.dto.MessageDto;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ControllerAdvice {

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public MessageDto resourceNotFound(
            final ResourceNotFoundException e
    ) {
        return new MessageDto(e.getMessage() != null
        ? e.getMessage()
                :"Not Found"
        );
    }


    @ExceptionHandler(ResourceAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public MessageDto resourceAlreadyExists(
            final ResourceAlreadyExistsException e
    ) {
        return new MessageDto(e.getMessage() != null
                ? e.getMessage()
                :"Already exists"
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public MessageDto validation(
            final MethodArgumentNotValidException e
    ) {
        Map<String, String> errors = e.getBindingResult()
                .getFieldErrors().stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        DefaultMessageSourceResolvable::getDefaultMessage,
                        (existingResult, newMessage) -> existingResult + " " + newMessage
                ));
        return new MessageDto(
                "Validation failed",
                errors
        );
    }

    @ExceptionHandler(IllegalStateException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public MessageDto illegalState(
            final IllegalStateException e
    ) {
        return new MessageDto(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public MessageDto exception(
            final Exception e
    ) {
        e.printStackTrace();
        return new MessageDto(e.getMessage());
    }

}
