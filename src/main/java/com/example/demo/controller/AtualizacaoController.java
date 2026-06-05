package com.example.demo.controller;

import com.example.demo.models.Dto.AtualizacaoRequestDTO;
import com.example.demo.models.Dto.AtualizacaoResponseDTO;
import com.example.demo.service.AtualizacaoService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/projetos/{projetoId}/atualizacoes")
public class AtualizacaoController {

    private final AtualizacaoService atualizacaoService;

    public AtualizacaoController(AtualizacaoService atualizacaoService) {
        this.atualizacaoService = atualizacaoService;
    }

    @Operation(summary = "Criar atualização")
    @PostMapping
    public ResponseEntity<AtualizacaoResponseDTO> criar(
            @PathVariable String projetoId,
            @Valid @RequestBody AtualizacaoRequestDTO dto) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(atualizacaoService.criar(projetoId, dto));
    }

    @Operation(summary = "Listar todas att de um projeto")
    @GetMapping
    public ResponseEntity<List<AtualizacaoResponseDTO>> listar(
            @PathVariable String projetoId) {
        return ResponseEntity.ok(atualizacaoService.listarPorProjeto(projetoId));
    }

    @Operation(summary = "Buscar att by ID")
    @GetMapping("/{id}")
    public ResponseEntity<AtualizacaoResponseDTO> buscarPorId(
            @PathVariable String projetoId,
            @PathVariable String id) {
        return ResponseEntity.ok(atualizacaoService.buscarPorId(projetoId, id));
    }

    @Operation(summary = "Atualizar att")
    @PutMapping("/{id}")
    public ResponseEntity<AtualizacaoResponseDTO> atualizar(
            @PathVariable String projetoId,
            @PathVariable String id,
            @Valid @RequestBody AtualizacaoRequestDTO dto) {
        return ResponseEntity.ok(atualizacaoService.atualizar(projetoId, id, dto));
    }

    @Operation(summary = "Deletar att")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(
            @PathVariable String projetoId,
            @PathVariable String id) {
        atualizacaoService.deletar(projetoId, id);
        return ResponseEntity.noContent().build();
    }
}