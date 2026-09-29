package br.com.tupidigital.dto.response;

import java.util.Map;

public record ErrorResponseDTO(
        String error,
        String message,
        Map<String, String> fieldErrors
) {
    public ErrorResponseDTO(String error, String message) {
        this(error, message, null);
    }
}
