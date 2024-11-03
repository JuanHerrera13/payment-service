package com.example.paymentservice.enumerator;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Enum representing different error types and their messages.
 */
@Getter
@AllArgsConstructor
public enum Error {

    NO_PAYMENT_FOUND("NO_PAYMENT_FOUND", "No payment was found."),
    NO_PAYMENT_FOUND_BY_ID("NO_PAYMENT_FOUND_BY_ID", "No payment was found with the given id."),
    BOOK_ALREADY_REGISTERED("BOOK_ALREADY_REGISTERED",
            "Book is already registered.");

    private final String errorMessage;
    private final String errorDescription;
}
