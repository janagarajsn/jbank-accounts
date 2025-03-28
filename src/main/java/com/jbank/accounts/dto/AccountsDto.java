package com.jbank.accounts.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
@Schema(name = "Accounts", description = "Schema to hold Accounts details")
public class AccountsDto {

    @Pattern(regexp = "^[0-9]+$", message = "Account number must contain 10 digits")
    @Schema(description = "Account number", example = "1234567890")
    private Long accountNumber;

    @NotEmpty(message = "Account type cannot be empty")
    @Schema(description = "Account type", example = "Savings")
    private String accountType;

    @NotEmpty(message = "Branch address cannot be empty")
    @Schema(description = "Branch address")
    private String branchAddress;
}
