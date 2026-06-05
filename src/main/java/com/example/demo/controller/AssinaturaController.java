package com.example.demo.controller;

import com.example.demo.models.Dto.AssinaturaRequestDTO;
import com.example.demo.models.Dto.AssinaturaResponseDTO;
import com.example.demo.service.AssinaturaService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/assinaturas")
public class AssinaturaController {

    private final AssinaturaService assinaturaService;

    public AssinaturaController(AssinaturaService assinaturaService) {
        this.assinaturaService = assinaturaService;
    }

    @Operation(summary = "Assinar um projeto")
    @PostMapping
    @PreAuthorize("hasAnyRole('APOIADOR', 'CRIADOR', 'ADMIN')")
    public ResponseEntity<AssinaturaResponseDTO> assinar(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody AssinaturaRequestDTO dto) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(assinaturaService.assinar(userDetails.getUsername(), dto));
    }

    @Operation(summary = "Listar minhas assinaturas ativas")
    @GetMapping("/minhas")
    public ResponseEntity<List<AssinaturaResponseDTO>> minhasAssinaturas(
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(
                assinaturaService.listarMinhasAssinaturas(userDetails.getUsername()));
    }

    @Operation(summary = "Buscar assinatura by ID")
    @GetMapping("/{id}")
    public ResponseEntity<AssinaturaResponseDTO> buscarPorId(
            @PathVariable String id,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(
                assinaturaService.buscarPorId(id, userDetails.getUsername()));
    }

    @Operation(summary = "Listar assinaturas ativas de um projeto")
    @GetMapping("/projeto/{projetoId}")
    public ResponseEntity<List<AssinaturaResponseDTO>> assinantesDoProjeto(
            @PathVariable String projetoId) {
        return ResponseEntity.ok(
                assinaturaService.listarAssinantesDoProjeto(projetoId));
    }

    @Operation(summary = "Contagem de assinantes ativos de um projeto")
    @GetMapping("/projeto/{projetoId}/contagem")
    public ResponseEntity<Long> contarAssinantes(@PathVariable String projetoId) {
        return ResponseEntity.ok(
                assinaturaService.contarAssinantesAtivos(projetoId));
    }

    @Operation(summary = "Receita total ativa do projeto")
    @GetMapping("/projeto/{projetoId}/receita")
    public ResponseEntity<BigDecimal> receitaAtiva(@PathVariable String projetoId) {
        return ResponseEntity.ok(
                assinaturaService.receitaAtivaDoProjeto(projetoId));
    }

    @Operation(summary = "Cancelar assinatura")
    @PatchMapping("/{id}/cancelar")
    public ResponseEntity<AssinaturaResponseDTO> cancelar(
            @PathVariable String id,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(
                assinaturaService.cancelar(id, userDetails.getUsername()));
    }
}