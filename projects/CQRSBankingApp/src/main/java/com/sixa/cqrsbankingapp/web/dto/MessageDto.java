package com.sixa.cqrsbankingapp.web.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Getter
@Service
@AllArgsConstructor
public class MessageDto {

    private String message;
    private Map<String, String> errors;

    public MessageDto(String message) {
        this.message = message;
        this.errors = new HashMap<>();
    }
}
