package com.example.demo.controller;

import com.example.demo.models.Dto.EnqueteRequestDTO;
import com.example.demo.models.Dto.EnqueteResponseDTO;
import com.example.demo.models.Dto.EnqueteUpdateRequestDTO;
import com.example.demo.service.EnqueteService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/enquetes")
@RequiredArgsConstructor
@Validated
public class EnqueteController {

    private final EnqueteService enqueteService;

    @Operation(summary = "Criar enquete")
    @PostMapping("/projeto/{projetoId}")
    public ResponseEntity<EnqueteResponseDTO> criar(
            @PathVariable @NotBlank String projetoId,
            @Valid @RequestBody EnqueteRequestDTO dto
    ) {
        EnqueteResponseDTO response = enqueteService.criar(projetoId, dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Listar enquetes de um projeto")
    @GetMapping("/projeto/{projetoId}")
    public ResponseEntity<List<EnqueteResponseDTO>> listarPorProjeto(
            @PathVariable @NotBlank String projetoId
    ) {
        List<EnqueteResponseDTO> response = enqueteService.listarPorProjeto(projetoId);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Buscar enquete by ID")
    @GetMapping("/{enqueteId}")
    public ResponseEntity<EnqueteResponseDTO> buscarPorId(
            @PathVariable @NotBlank String enqueteId
    ) {
        EnqueteResponseDTO response = enqueteService.buscarPorId(enqueteId);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Att enquete")
    @PatchMapping("/{enqueteId}")
    public ResponseEntity<EnqueteResponseDTO> atualizar(
            @PathVariable @NotBlank String enqueteId,
            @Valid @RequestBody EnqueteUpdateRequestDTO dto
    ) {
        EnqueteResponseDTO response = enqueteService.atualizar(enqueteId, dto);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Deletar enquete")
    @DeleteMapping("/{enqueteId}")
    public ResponseEntity<Void> deletar(
            @PathVariable @NotBlank String enqueteId
    ) {
        enqueteService.deletar(enqueteId);
        return ResponseEntity.noContent().build();
    }
}