package com.example.demo.controller;

import com.example.demo.models.Dto.WebhookPayloadDTO;
import com.example.demo.service.PagamentoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/webhooks")
@RequiredArgsConstructor
public class WebhookController {

    private final PagamentoService pagamentoService;


    @PostMapping("/abacatepay")
    public ResponseEntity<Void> receberWebhook(@RequestBody WebhookPayloadDTO payload) {
        log.info("Webhook recebido | event={} txId={} status={} externalId={}",
                payload.getEvent(),
                payload.getTxId(),
                payload.getStatus(),
                payload.getExternalId());

        pagamentoService.processarWebhook(payload);

        return ResponseEntity.ok().build();
    }
}