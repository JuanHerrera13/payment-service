package com.example.paymentservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * Data Transfer Object (DTO) of book.
 */
@Data
public class PaymentDto {

    @NotBlank
    private String id;

    @NotBlank
    private String cartId;

    @NotNull
    private Float amount;
}