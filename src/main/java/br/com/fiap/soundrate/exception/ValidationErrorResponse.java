package br.com.fiap.soundrate.exception;

import java.time.LocalDateTime;
import java.util.Map;

public record ValidationErrorResponse(int status, String message, LocalDateTime timestamp, Map<String, String> errors) {}