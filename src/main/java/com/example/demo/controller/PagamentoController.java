package com.example.demo.controller;

import com.example.demo.models.Dto.GerarCobrancaRequestDTO;
import com.example.demo.models.Dto.PagamentoResponseDTO;
import com.example.demo.service.PagamentoService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pagamentos")
@RequiredArgsConstructor
public class PagamentoController {

    private final PagamentoService pagamentoService;

    // Apoiador gera uma cobrança PIX para sua assinatura
    @Operation(summary = "Gera cobrança pix para assinatura")
    @PostMapping("/gerar")
    @PreAuthorize("hasAnyRole('APOIADOR', 'ADMIN')")
    public ResponseEntity<PagamentoResponseDTO> gerarCobranca(
            @Valid @RequestBody GerarCobrancaRequestDTO request) {

        PagamentoResponseDTO response = pagamentoService.gerarCobranca(request.getAssinaturaId());
        return ResponseEntity.ok(response);
    }

    // Histórico de pagamentos de uma assinatura
    @Operation(summary = "Gera histórico de pagamentos de uma assinatura")
    @GetMapping("/assinatura/{assinaturaId}")
    @PreAuthorize("hasAnyRole('APOIADOR', 'CRIADOR', 'ADMIN')")
    public ResponseEntity<List<PagamentoResponseDTO>> listarPorAssinatura(
            @PathVariable String assinaturaId) {

        return ResponseEntity.ok(pagamentoService.listarPorAssinatura(assinaturaId));
    }
}