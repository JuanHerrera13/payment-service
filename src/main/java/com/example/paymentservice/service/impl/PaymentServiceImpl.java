package com.example.paymentservice.service.impl;

import com.example.paymentservice.domain.Payment;
import com.example.paymentservice.dto.CartDto;
import com.example.paymentservice.dto.PaymentCreationDto;
import com.example.paymentservice.dto.PaymentDto;
import com.example.paymentservice.dto.mapping.PaymentMapper;
import com.example.paymentservice.enumerator.Error;
import com.example.paymentservice.exception.NotFoundException;
import com.example.paymentservice.exception.PaymentCreationException;
import com.example.paymentservice.integration.BookApiClient;
import com.example.paymentservice.integration.CartApiClient;
import com.example.paymentservice.integration.NotificationApiClient;
import com.example.paymentservice.repository.PaymentRepository;
import com.example.paymentservice.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final CartApiClient cartApiClient;
    private final BookApiClient bookApiClient;
    private final NotificationApiClient notificationApiClient;
    private final PaymentRepository paymentRepository;
    private final PaymentMapper paymentMapper;

    @Override
    public List<PaymentDto> fetchAllPayments() {
        log.info("Fetching all payments");
        List<Payment> payments = paymentRepository.findAll();
        if (payments.isEmpty()) {
            log.error("No payment found");
            throw new NotFoundException(Error.NO_PAYMENT_FOUND.getErrorMessage());
        }
        return paymentMapper.paymentListToPaymentListDto(payments);
    }

    @Override
    public Payment findPaymentById(String id) {
        log.info("Fetching payment by id");
        return paymentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(Error.NO_PAYMENT_FOUND.getErrorMessage()));
    }

    @Override
    public PaymentDto addPayment(PaymentCreationDto paymentCreationDto) {
        paymentRepository.findTopByCartId(paymentCreationDto.getCartId()).ifPresent(_ -> {
            throw new NotFoundException("Pagamento já existe com o cartId ".concat(paymentCreationDto.getCartId()));
        });
        Float amount = 0F;
        try {
            final CartDto cartDto = cartApiClient.getCartById(paymentCreationDto.getCartId());
            for (String bookId : cartDto.getBooksIds()) {
                bookApiClient.getBookById(bookId);
                amount += bookApiClient.getBookById(bookId).getPrice();
            }
        } catch (NotFoundException e) {
            throw new NotFoundException("Pagamento não pôde ser concluído, pois: " + e.getMessage());
        } catch (Exception e) {
            throw new PaymentCreationException(e.getMessage());
        }
        Payment payment = paymentMapper.paymentCreationDtoToPayment(paymentCreationDto);
        payment.setAmount(amount);
        paymentRepository.save(payment);
        log.info("Pagamento concluído com sucesso para o carrinho com Id {}", payment.getId());
        notificationApiClient.sendEmail(paymentCreationDto.getUserEmail());
        return paymentMapper.paymentToPaymentDto(payment);
    }

    @Override
    public void deletePayment(String id) {
        final Payment payment = this.findPaymentById(id);
        paymentRepository.delete(payment);
        log.info("Payment {} deleted.", payment.getId());
    }

    @Override
    public PaymentDto findPaymentByCartId(String cartId) {
        Payment payment = paymentRepository.findTopByCartId(cartId)
                .orElseThrow(() -> new NotFoundException(Error.NO_PAYMENT_FOUND.getErrorMessage()));
        return paymentMapper.paymentToPaymentDto(payment);
    }
}
