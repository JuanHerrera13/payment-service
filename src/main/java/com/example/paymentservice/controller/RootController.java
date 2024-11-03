package com.example.paymentservice.controller;

import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Abstract class that centralizes the path prefix "/payment-service/v1"
 * for controller classes that extend RootController.
 */
@RequestMapping(path = "/payment-service/v1")
public abstract class RootController {
}
