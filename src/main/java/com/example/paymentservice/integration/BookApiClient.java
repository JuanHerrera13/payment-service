package com.example.paymentservice.integration;

import com.example.paymentservice.dto.BookDto;
import com.example.paymentservice.exception.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
public class BookApiClient {

    private static final String BOOK_SERVICE_URL = "http://localhost:8080/book-service/v1/books/id/";

    public BookDto getBookById(String bookId) {
        RestTemplate restTemplate = new RestTemplate();
        try {
            log.info("Chamando book service com bookId: {}", bookId);
            ResponseEntity<BookDto> response = restTemplate.getForEntity(BOOK_SERVICE_URL + bookId, BookDto.class);
            return response.getBody();
        } catch (HttpClientErrorException.NotFound e) {
            log.error("Livro não encontrado!");
            throw new NotFoundException("Livro não foi encontrado com o id "
                    + bookId);
        } catch (Exception e) {
            log.error(e.toString());
            throw new RestClientException(e.getMessage());
        }
    }
}
