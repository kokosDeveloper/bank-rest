package com.example.bankcards.config.openApi;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ValidationErrorRepresentation {
    private Map<String, String> fieldErrors;
}
