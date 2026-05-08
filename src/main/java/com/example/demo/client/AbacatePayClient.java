package com.example.demo.client;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class AbacatePayClient {

    private final RestTemplate restTemplate;

    @Value("${abacatepay.api.key}")
    private String apiKey;

    @Value("${abacatepay.api.url}")
    private String baseUrl;

    // Criar cobrança PIX

    public CobrancaResponse criarCobrancaPix(BigDecimal valor, String assinaturaId) {
        String url = baseUrl + "/pixQrCode/create"; // <-- muda aqui

        long valorEmCentavos = valor.multiply(BigDecimal.valueOf(100)).longValue();

        Map<String, Object> body = Map.of(
                "amount",      valorEmCentavos,
                "expiresIn",   3600,
                "description", "Apoio via ApoiaCe",
                "metadata",    Map.of("assinaturaId", assinaturaId)
        );

        try {
            ResponseEntity<AbacatePayResponse> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    new HttpEntity<>(body, buildHeaders()),
                    AbacatePayResponse.class
            );

            AbacatePayResponse responseBody = response.getBody();

            if (responseBody == null || responseBody.getData() == null) {
                throw new RuntimeException("Resposta vazia da AbacatePay");
            }

            log.info("Cobrança PIX criada na AbacatePay | txId={} assinatura={}",
                    responseBody.getData().getId(), assinaturaId);

            return CobrancaResponse.from(responseBody.getData());

        } catch (HttpClientErrorException e) {
            log.error("Erro ao criar cobrança na AbacatePay | status={} body={}",
                    e.getStatusCode(), e.getResponseBodyAsString());
            throw new RuntimeException("Falha ao gerar cobrança PIX: " + e.getStatusCode());
        }
    }

    // headers

    private HttpHeaders buildHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);
        return headers;
    }

    // DTOs internos (mapeiam o contrato da AbacatePay)

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class AbacatePayResponse {
        private AbacatePayData data;
        private Boolean success;
        private String error;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class AbacatePayData {
        private String id;
        private Long amount;
        private String status;
        private String brCode;
        private String brCodeBase64;
        private Long platformFee;
        private String expiresAt;
        private String createdAt;
    }

    // DTO de saída para o PagamentoService

    @Data
    public static class CobrancaResponse {
        private String txId;
        private String qrCode;       // brCode — copia e cola
        private String qrCodeBase64; // imagem base64
        private Instant expiresAt;

        public static CobrancaResponse from(AbacatePayData data) {
            CobrancaResponse r = new CobrancaResponse();
            r.setTxId(data.getId());
            r.setQrCode(data.getBrCode());
            r.setQrCodeBase64(data.getBrCodeBase64());
            r.setExpiresAt(data.getExpiresAt() != null
                    ? Instant.parse(data.getExpiresAt())
                    : null);
            return r;
        }
    }


}