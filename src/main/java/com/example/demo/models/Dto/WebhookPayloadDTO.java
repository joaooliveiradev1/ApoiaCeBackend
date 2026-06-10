package com.example.demo.models.Dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class WebhookPayloadDTO {

    private String event;
    private Boolean devMode;
    private WebhookDataDTO data;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class WebhookDataDTO {
        private PaymentDTO payment;
        private BillingDTO billing;
        private PixQrCodeDTO pixQrCode;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class PaymentDTO {
        private String method;
        private Integer fee;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class BillingDTO {
        private String id;
        private String externalId;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class PixQrCodeDTO {
        private String id;
        private String status;
        private Integer amount;
        private String kind;
    }

    public String getTxId() {
        return data != null && data.getPixQrCode() != null
                ? data.getPixQrCode().getId()
                : null;
    }

    public String getStatus() {
        return data != null && data.getPixQrCode() != null
                ? data.getPixQrCode().getStatus()
                : null;
    }

    public String getExternalId() {
        return data != null && data.getBilling() != null
                ? data.getBilling().getExternalId()
                : null;
    }
}