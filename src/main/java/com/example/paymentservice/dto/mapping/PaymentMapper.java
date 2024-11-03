package com.example.paymentservice.dto.mapping;

import com.example.paymentservice.domain.Payment;
import com.example.paymentservice.dto.PaymentCreationDto;
import com.example.paymentservice.dto.PaymentDto;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

/**
 * Mapper interface for converting between domain objects and DTOs using MapStruct.
 */
@Mapper(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface PaymentMapper {

    Payment paymentCreationDtoToPayment(PaymentCreationDto paymentCreationDto);

    PaymentDto paymentToPaymentDto(Payment payment);

    List<PaymentDto> paymentListToPaymentListDto(List<Payment> payments);
}
