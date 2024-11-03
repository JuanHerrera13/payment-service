package com.example.paymentservice.integration;

import com.example.paymentservice.dto.CartDto;
import com.example.paymentservice.exception.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
public class CartApiClient {

    private static final String CART_SERVICE_URL = "http://localhost:8084/cart-service/v1/carts/";

    public CartDto getCartById(String cartId) {
        RestTemplate restTemplate = new RestTemplate();
        try {
            log.info("Chamando cart service com cartId: {}", cartId);
            ResponseEntity<CartDto> response = restTemplate.getForEntity(CART_SERVICE_URL + cartId, CartDto.class);
            return response.getBody();
        } catch (HttpClientErrorException.NotFound e) {
            log.error("Carrinho não encontrado!");
            throw new NotFoundException("Carrinho não foi encontrado com o id "
                    + cartId);
        } catch (Exception e) {
            log.error(e.toString());
            throw new RestClientException(e.getMessage());
        }
    }
}
