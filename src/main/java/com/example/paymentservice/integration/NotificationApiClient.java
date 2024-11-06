package com.example.paymentservice.integration;

import com.example.paymentservice.dto.EmailDto;
import com.example.paymentservice.exception.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
public class NotificationApiClient {

    private static final String NOTIFICATION_SERVICE_URL = "http://localhost:8083/email-service/v1/send-email";

    public void sendEmail(String to) {
        RestTemplate restTemplate = new RestTemplate();

        EmailDto emailDto = new EmailDto(to, "Pagamento concluído",
                "O seu pagamento foi concluído com sucesso.");

        try {
            log.info("Chamando notification-service");
            ResponseEntity<Void> response = restTemplate.postForEntity(NOTIFICATION_SERVICE_URL, emailDto, Void.class);
            if (response.getStatusCode() == HttpStatus.OK) {
                log.info("Email enviado com sucesso!");
            }
        } catch (HttpClientErrorException.NotFound e) {
            log.error("Erro: Serviço de notificação não encontrado!");
            throw new NotFoundException("Serviço de notificação não encontrado para o endereço " + NOTIFICATION_SERVICE_URL);
        } catch (Exception e) {
            log.error("Erro ao enviar email: " + e.getMessage());
            throw new RestClientException(e.getMessage());
        }
    }
}
