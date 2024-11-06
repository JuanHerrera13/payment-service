package com.example.paymentservice.service;

import com.example.paymentservice.domain.Payment;
import com.example.paymentservice.dto.PaymentCreationDto;
import com.example.paymentservice.dto.PaymentDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PaymentService {

    List<PaymentDto> fetchAllPayments();

    Payment findPaymentById(String id);

    PaymentDto addPayment(PaymentCreationDto paymentCreationDto);

    void deletePayment(String id);

    PaymentDto findPaymentByCartId(String cartId);
}
