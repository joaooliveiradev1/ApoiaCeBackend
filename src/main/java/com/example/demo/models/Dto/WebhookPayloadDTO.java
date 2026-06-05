package com.example.demo.models.Dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class WebhookPayloadDTO {

    private String event;  // "billing.paid"
    private WebhookDataDTO data;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class WebhookDataDTO {
        private String id;      // esse é o txId — "pix_char_..."
        private String status;  // "PAID"
    }

    public String getTxId() {
        return data != null ? data.getId() : null;
    }

    public String getStatus() {
        return data != null ? data.getStatus() : null;
    }
}