package com.jbank.accounts.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Data @AllArgsConstructor @Schema(name = "ErrorResponse", description = "Error Response")
public class ErrorResponseDto {

    @Schema(description = "API path", example = "/accounts")
    private String apiPath;

    @Schema(description = "Error code", example = "500")
    private HttpStatus errorCode;

    @Schema(description = "Error message", example = "An error occurred. Please try again or contact Dev team")
    private String errorMessage;

    @Schema(description = "Error time", example = "2023-08-17T12:34:56")
    private LocalDateTime errorTime;
}
