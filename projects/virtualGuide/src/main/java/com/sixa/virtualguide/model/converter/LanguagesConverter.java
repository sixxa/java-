package com.sixa.virtualguide.model.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Converter
public class LanguagesConverter implements AttributeConverter<List<String>, String> {

    @Override
    public String convertToDatabaseColumn(List<String> languages) {
        if (languages == null || languages.isEmpty()) {
            return "";
        }
        // Convert the list to a comma-separated string
        return String.join(",", languages);
    }

    @Override
    public List<String> convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isEmpty()) {
            return List.of(); // Return an empty list if the value is empty
        }
        // Convert the comma-separated string back to a list
        return Arrays.stream(dbData.split(","))
                .map(String::trim)
                .collect(Collectors.toList());
    }
}
